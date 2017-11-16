package com.greenhat.mvc.request;

public enum JsonRequestEnum {
    SERVICE("X-Service"),
    METHOD("X-Method"),
    TOKEN("X-Token");

    private String type ;

    private JsonRequestEnum( String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
