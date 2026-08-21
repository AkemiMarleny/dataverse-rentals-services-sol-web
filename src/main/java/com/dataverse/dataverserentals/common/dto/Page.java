package com.dataverse.dataverserentals.common.dto;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Getter;

@Getter
public class Page<Y> {
    private int page;
    private int pageSize;
    private long total;
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
