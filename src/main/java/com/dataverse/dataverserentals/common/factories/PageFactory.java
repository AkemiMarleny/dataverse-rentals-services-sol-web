package com.dataverse.dataverserentals.common.factories;

import com.dataverse.dataverserentals.common.dto.Page;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageFactory {

    public static <T> Page<T> from(org.springframework.data.domain.Page<T> page) {
        return new Page<>(
                page.getPageable().getPageNumber(),
                page.getPageable().getPageSize(),
                page.getTotalElements(),
                page.getContent());
    }
}
