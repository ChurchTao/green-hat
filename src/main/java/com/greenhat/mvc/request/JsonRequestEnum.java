package com.greenhat.mvc.request;

public enum JsonRequestEnum {
    SERVICE("Service"),
    METHOD("Method"),
    BODY("Body");

    private String type ;

    private JsonRequestEnum( String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
