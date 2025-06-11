package ru.itis.fisd.semestrovka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.itis.fisd.semestrovka.entity.orm.User;
import ru.itis.fisd.semestrovka.exception.DuplicateUserException;
import ru.itis.fisd.semestrovka.exception.UserNotFoundException;
import ru.itis.fisd.semestrovka.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Page<User> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    public void updateRole(Long userId, String role) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setRole(role);
        userRepository.save(user);
    }

    public void save(User user) {
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateUserException(user.getUsername());
        }
    }
}
