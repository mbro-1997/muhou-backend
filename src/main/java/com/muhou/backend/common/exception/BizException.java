package com.muhou.backend.common.exception;

import com.muhou.backend.common.api.ResultCode;

public class BizException extends RuntimeException {

    private final ResultCode resultCode;

    public BizException(String message) {
        this(ResultCode.CONFLICT, message);
    }

    public BizException(ResultCode resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
