package ru.itis.fisd.semestrovka.mapper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.itis.fisd.semestrovka.dto.response.PageResponse;

@Component
public class PageMapper {

    public <T> PageResponse<T> toPageResponse(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast(),
                page.isFirst()
        );
    }
}
