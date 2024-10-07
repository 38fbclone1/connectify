package com.connectify.connectify.enums;

public enum EError {
    BAD_REQUEST("Bad request!"),
    EXISTED_BY_USERNAME("Username is existed!"),
    EXISTED_BY_EMAIL("Email is existed!"),
    EXISTED_BY_PHONE_NUMBER("Phone number is existed!"),
    EXISTED_BY_IDENTIFICATION_NUMBER("Phone number is existed!"),
    USER_NOT_EXISTED("User is not existed!"),
    INCORRECT_PASSWORD("Password is not correct!"),
    UNAUTHENTICATED("Account is not authenticated!"),
    UNAUTHORIZED("Account is not authenticated!"),
    ROLE_NOT_FOUND("Role not found!");

    private final String message;

    EError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}


