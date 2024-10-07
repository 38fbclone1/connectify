package com.connectify.connectify.enums;

public enum EReactionType {
    LIKE("LIKE"),
    LOVE("LOVE"),
    FUNNY("FUNNY"),
    SAD("SAD"),
    ANGRY("ANGRY");

    private final String message;

    EReactionType (String message) {
        this.message = message;
    }

    public String getMessage () {
        return this.message;
    }
}
