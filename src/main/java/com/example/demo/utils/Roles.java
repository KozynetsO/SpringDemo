package com.example.demo.utils;

public enum Roles {
    ROLE_USER,
    ROLE_ADMIN;

    public String removePrefix() {
        return this.name().replace("ROLE_", "");
    }
}