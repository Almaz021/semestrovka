package ru.itis.fisd.semestrovka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.itis.fisd.semestrovka.entity.dto.PurchaseDto;
import ru.itis.fisd.semestrovka.entity.orm.Apartment;
import ru.itis.fisd.semestrovka.entity.orm.Purchase;
import ru.itis.fisd.semestrovka.entity.orm.User;
import ru.itis.fisd.semestrovka.exception.ApartmentAlreadySoldException;
import ru.itis.fisd.semestrovka.exception.PurchaseNotFoundException;
import ru.itis.fisd.semestrovka.mapper.PurchaseMapper;
import ru.itis.fisd.semestrovka.repository.PurchaseRepository;
import ru.itis.fisd.semestrovka.util.EmailContentBuilder;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ApartmentService apartmentService;
    private final UserService userService;
    private final PurchaseMapper purchaseMapper;
    private final WebClient webClient;
    private final EmailContentBuilder emailContentBuilder;


    @Transactional
    public void purchaseApartment(String username, Long apartmentId, String email) {
        log.debug("Purchase Apartment with id {}, User with username = {}. Email = {}", apartmentId, username, email);
        User user = userService.findByUsername(username);
        Apartment apartment = apartmentService.findByIdAvailable(apartmentId);
        if (!apartment.getStatus().equals("AVAILABLE")) {
            throw new ApartmentAlreadySoldException(apartment.getId());
        }
        apartment.setStatus("SOLD");
        apartmentService.save(apartment);

        Purchase purchase = Purchase.builder()
                .user(user)
                .apartment(apartment)
                .purchaseDate(LocalDateTime.now())
                .build();

        purchaseRepository.save(purchase);

        String subject = "Покупка квартиры";

        String htmlBody = emailContentBuilder.buildPurchaseEmail(
                user.getUsername(),
                apartment.getTitle(),
                apartment.getAddress(),
                apartment.getPrice()
        );

        sendEmail(email, subject, htmlBody);
    }

    public Page<PurchaseDto> findAll(int page, int size) {
        log.debug("Find all purchases");
        Pageable pageable = PageRequest.of(page, size);
        return purchaseRepository.findAll(pageable).map(purchaseMapper::toDto);
    }

    public Purchase findById(Long purchaseId) {
        log.debug("Find purchase with id {}", purchaseId);
        return purchaseRepository.findById(purchaseId).orElseThrow(() -> new PurchaseNotFoundException(purchaseId));
    }

    public Page<PurchaseDto> findAllByUsername(String username, Pageable pageable) {
        log.debug("Find all purchases by username {}", username);
        User user = userService.findByUsername(username);
        return purchaseRepository.findAllByUser(user, pageable).map(purchaseMapper::toDto);
    }

    public PurchaseDto getUserPurchase(Long purchaseId, String username) {
        log.debug("Get user purchase with id {}", purchaseId);
        Purchase purchase = findById(purchaseId);
        User user = userService.findByUsername(username);

        if (!purchase.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("User does not own this purchase");
        }

        return purchaseMapper.toDto(purchase);
    }

    private void sendEmail(String to, String subject, String body) {
        Map<String, String> payload = Map.of(
                "to", to,
                "subject", subject,
                "body", body
        );

        webClient.post()
                .uri("/send")
                .bodyValue(payload)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new RuntimeException("Ошибка при отправке email: " + error))))
                .toBodilessEntity()
                .block();
    }
}
