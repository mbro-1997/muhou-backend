package com.muhou.backend.common.model;

import java.util.Collections;
import java.util.List;

public class PageResponse<T> {

    private List<T> list;
    private long total;

    public PageResponse() {
    }

    public PageResponse(List<T> list, long total) {
        this.list = list;
        this.total = total;
    }

    public static <T> PageResponse<T> empty() {
        return new PageResponse<>(Collections.emptyList(), 0L);
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
