package com.kalvad.test.model.enums;

public enum ErrorCode {
    NOT_FOUND("not.found", "Entity not found"),
    INVALID_REQUEST("invalid.request", "Invalid request");
    private final String code;

    private final String description;

    ErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
