package com.dataverse.dataverserentals.common.dto;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;

@Getter
public class Page<Y> {
    @Schema(description = "Número de página (base 0)", example = "0")
    private int page;
    @Schema(description = "Tamaño de página", example = "20")
    private int pageSize;
    @Schema(description = "Cantidad total de registros", example = "150")
    private long total;
    @Schema(description = "Contenido de la página")
    private List<Y> data;

    public Page(int page, int pageSize, long total, List<Y> data) {
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
        this.data = data;
    }

    public <A> Page<A> map(Function<Y, A> mapper) {
        List<A> newData = data.stream().map(mapper).collect(Collectors.toList());

        return new Page<>(page, pageSize, total, newData);
    }
}