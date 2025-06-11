package ru.itis.fisd.semestrovka.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.itis.fisd.semestrovka.dto.ApartmentDto;
import ru.itis.fisd.semestrovka.entity.orm.Apartment;
import ru.itis.fisd.semestrovka.entity.orm.User;
import ru.itis.fisd.semestrovka.service.ApartmentService;
import ru.itis.fisd.semestrovka.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/favorites")
@RequiredArgsConstructor
@Slf4j
public class FavoritesController {

    private final UserService userService;
    private final ApartmentService apartmentService;

    @GetMapping
    public ResponseEntity<?> getFavorites(@AuthenticationPrincipal UserDetails userDetails,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "5") int size) {

        log.info("Getting favorites");

        User user = userService.findByUsername(userDetails.getUsername());

        Page<Apartment> favorites = apartmentService.findFavoritesByUser(user, PageRequest.of(page, size));

        List<ApartmentDto> apartmentDtos = favorites.getContent().stream()
                .map(ApartmentDto::from)
                .collect(Collectors.toList());

        log.info("Found {} favorites", apartmentDtos.size());

        return ResponseEntity.ok().body(apartmentDtos);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> addToFavorites(@AuthenticationPrincipal UserDetails userDetails,
                                            @PathVariable Long id) {
        log.info("Adding to favorites");
        User user = userService.findByUsername(userDetails.getUsername());
        Apartment apartment = apartmentService.findByIdAvailable(id);

        user.getFavoriteApartments().add(apartment);
        userService.save(user);

        log.info("Added to favorites");

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeFromFavorites(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable Long id) {
        log.info("Removing from favorites");

        User user = userService.findByUsername(userDetails.getUsername());
        Apartment apartment = apartmentService.findByIdAvailable(id);

        user.getFavoriteApartments().remove(apartment);
        userService.save(user);

        log.info("Removed from favorites");

        return ResponseEntity.noContent().build();
    }
}
