package com.greenhat.mvc.request;

public enum ContentType {
    JSON("application/json"),
    FORM("application/x-www-form-urlencoded");


    private String type ;

    private ContentType( String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
