package com.muhou.backend.common.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class PageQuery {

    @Min(value = 1, message = "pageNum must be greater than or equal to 1")
    private int pageNum = 1;

    @Min(value = 1, message = "pageSize must be greater than or equal to 1")
    @Max(value = 100, message = "pageSize must be less than or equal to 100")
    private int pageSize = 10;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
