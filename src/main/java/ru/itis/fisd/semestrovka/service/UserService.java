package ru.itis.fisd.semestrovka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.fisd.semestrovka.entity.orm.User;
import ru.itis.fisd.semestrovka.exception.DuplicateUserException;
import ru.itis.fisd.semestrovka.exception.UserNotFoundException;
import ru.itis.fisd.semestrovka.repository.UserRepository;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public Page<User> findAll(int page, int size) {
        log.debug("Finding all users");
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
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

    public void save(User user) {
        log.debug("Saving user: {}", user);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateUserException(user.getUsername());
        }
    }
}
