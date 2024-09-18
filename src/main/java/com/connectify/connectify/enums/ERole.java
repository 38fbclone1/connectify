package com.connectify.connectify.enums;

public enum ERole {
    SUPER_ADMIN("All permissions"),
    SUPER_MODERATOR(""),
    USER("Normal permissions"),
    ADMIN("All permissions of specific context"),
    MODERATOR("Add, remove user(exclude Admin); Approve, delete post of other members of specific context"),
    MEMBER("CRUD post/ message of a specific context");

    private final String description;

    ERole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

