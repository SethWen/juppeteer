package com.modorone.juppeteer.pojo;

/**
 * author: Shawn
 * time  : 2/23/20 6:45 PM
 * desc  : todo 这个不知道干啥用的
 * update: Shawn 2/23/20 6:45 PM
 */
public class MediaFeature {

    // prefers-color-scheme|prefers-reduced-motion
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public MediaFeature setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public MediaFeature setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return "MediaFeature{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
