package ru.itis.fisd.semestrovka.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.itis.fisd.semestrovka.dto.response.PageResponse;
import ru.itis.fisd.semestrovka.entity.dto.ApartmentDto;
import ru.itis.fisd.semestrovka.mapper.PageMapper;
import ru.itis.fisd.semestrovka.service.UserService;

@RestController
@RequestMapping("api/favorites")
@RequiredArgsConstructor
@Slf4j
public class FavoritesController {

    private final UserService userService;
    private final PageMapper pageMapper;

    @GetMapping
    public ResponseEntity<?> getFavorites(@AuthenticationPrincipal UserDetails userDetails,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "5") int size) {

        log.info("Getting favorites");

        Page<ApartmentDto> favorites = userService.getFavorites(userDetails.getUsername(), page, size);

        log.info("Found {} favorites", favorites.getTotalElements());

        PageResponse<ApartmentDto> response = pageMapper.toPageResponse(favorites);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/{id}")
    public ResponseEntity<?> addToFavorites(@AuthenticationPrincipal UserDetails userDetails,
                                            @PathVariable Long id) {
        log.info("Adding to favorites");

        userService.addApartmentToFavorites(userDetails.getUsername(), id);

        log.info("Added to favorites");

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeFromFavorites(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable Long id) {
        log.info("Removing from favorites");

        userService.removeApartmentFromFavorites(userDetails.getUsername(), id);

        log.info("Removed from favorites");
        return ResponseEntity.noContent().build();
    }
}
