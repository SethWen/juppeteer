package com.modorone.juppeteer;

public enum MediaType {

    SCREEN("screen"), PRINT("print");

    private String type;

    MediaType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public MediaType setType(String type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return "MediaType{" +
                "type='" + type + '\'' +
                '}';
    }
}
