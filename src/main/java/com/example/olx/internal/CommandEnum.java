package com.example.olx.internal;

public enum CommandEnum {

    HTTP("http"), CLEAN("clean"), PARSE_ALL("parse"), ERROR("error_input"), BIKES("bikes"),
    ;

    private final String name;

    CommandEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
