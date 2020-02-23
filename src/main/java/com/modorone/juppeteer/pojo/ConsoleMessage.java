package com.modorone.juppeteer.pojo;

import java.util.Arrays;

/**
 * author: Shawn
 * time  : 2/23/20 9:20 PM
 * desc  :
 * update: Shawn 2/23/20 9:20 PM
 */
public class ConsoleMessage {

    private String type;
    private String text;
    private String[] args;
    private String location;

    public String getType() {
        return type;
    }

    public ConsoleMessage setType(String type) {
        this.type = type;
        return this;
    }

    public String getText() {
        return text;
    }

    public ConsoleMessage setText(String text) {
        this.text = text;
        return this;
    }

    public String[] getArgs() {
        return args;
    }

    public ConsoleMessage setArgs(String[] args) {
        this.args = args;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public ConsoleMessage setLocation(String location) {
        this.location = location;
        return this;
    }

    @Override
    public String toString() {
        return "ConsoleMessage{" +
                "type='" + type + '\'' +
                ", text='" + text + '\'' +
                ", args=" + Arrays.toString(args) +
                ", location='" + location + '\'' +
                '}';
    }
}
