package com.kalvad.test.model.exception;

import com.kalvad.test.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private final String errorCode;
    private final String errorMessage;
    private HttpStatus httpStatus;

    public ApiException(ErrorCode errorCode) {
        this(errorCode, HttpStatus.INTERNAL_SERVER_ERROR);

    }
    public ApiException(ErrorCode errorCode, HttpStatus httpStatus) {
        this.errorCode = errorCode.getCode();
        this.errorMessage = errorCode.getDescription();
        this.httpStatus = httpStatus;
    }

    public ApiException(String errorCode, String errorMessage, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return this.errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
