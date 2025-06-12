package ru.itis.fisd.semestrovka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.fisd.semestrovka.dto.request.ProfileEditRequest;
import ru.itis.fisd.semestrovka.entity.dto.ApartmentDto;
import ru.itis.fisd.semestrovka.entity.dto.UserDto;
import ru.itis.fisd.semestrovka.entity.orm.Apartment;
import ru.itis.fisd.semestrovka.entity.orm.User;
import ru.itis.fisd.semestrovka.exception.DuplicateUserException;
import ru.itis.fisd.semestrovka.exception.UserNotFoundException;
import ru.itis.fisd.semestrovka.mapper.ApartmentMapper;
import ru.itis.fisd.semestrovka.mapper.UserMapper;
import ru.itis.fisd.semestrovka.repository.UserRepository;
import ru.itis.fisd.semestrovka.security.UserDetailsImpl;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ApartmentService apartmentService;
    private final ApartmentMapper apartmentMapper;
    private final UserMapper userMapper;

    public Page<UserDto> findAll(int page, int size) {
        log.debug("Finding all users");
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable).map(userMapper::toDto);
    }

    public UserDto findDtoByUsername(String username) {
        log.debug("Finding user dto with username: {}", username);
        return userRepository.findByUsername(username).map(userMapper::toDto).orElseThrow(() -> new UserNotFoundException(username));
    }

    public User findByUsername(String username) {
        log.debug("Finding user with username: {}", username);
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    @Transactional
    public void updateRole(Long userId, String role) {
        log.debug("Updating user with id {}. Set role: {}", userId, role);
        User user = userRepository.findById(userId).orElseThrow();
        user.setRole(role);
        userRepository.save(user);
    }

    public void save(String username, String password) {
        log.debug("Saving user: {}", username);
        String encodedPassword = passwordEncoder.encode(password);
        User user = User.builder()
                .username(username)
                .passwordHash(encodedPassword)
                .role("USER")
                .build();
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateUserException(user.getUsername());
        }
    }

    @Transactional
    public void addApartmentToFavorites(String username, Long apartmentId) {
        log.debug("Adding apartment to favorites with username = {} and apartment id = {}", username, apartmentId);
        User user = findByUsername(username);
        Apartment apartment = apartmentService.findByIdAvailable(apartmentId);

        user.getFavoriteApartments().add(apartment);
        userRepository.save(user);
    }

    @Transactional
    public void removeApartmentFromFavorites(String username, Long apartmentId) {
        log.debug("Remove apartment from favorites with username = {} and apartment id = {}", username, apartmentId);
        User user = findByUsername(username);
        Apartment apartment = apartmentService.findByIdAvailable(apartmentId);

        user.getFavoriteApartments().remove(apartment);
        userRepository.save(user);
    }

    public Page<ApartmentDto> getFavorites(String username, int page, int size) {
        log.debug("Get favorites for user with username = {}", username);
        User user = findByUsername(username);
        return apartmentService.findFavoritesByUser(user, PageRequest.of(page, size)).map(apartmentMapper::toDto);
    }

    public boolean isApartmentFavoriteForUser(UserDetails userDetails, ApartmentDto apartment) {
        log.debug("Check if apartment is favorite for user with username = {}, apartment with id = {}", userDetails.getUsername(), apartment.id());
        UserDto user = findDtoByUsername(userDetails.getUsername());
        return user.favoriteApartments().contains(apartment);
    }

    public void updateProfile(String currentUsername, ProfileEditRequest request) {
        log.debug("Updating profile with username = {}", currentUsername);
        User user = findByUsername(currentUsername);

        if (request.username() != null && !request.username().isBlank()) {
            user.setUsername(request.username());
        }

        if (request.password() != null && !request.password().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.password()));
        }

        userRepository.save(user);

        UserDetails updatedUserDetails = new UserDetailsImpl(findByUsername(user.getUsername()));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        updatedUserDetails,
                        updatedUserDetails.getPassword(),
                        updatedUserDetails.getAuthorities()
                )
        );
    }

}
