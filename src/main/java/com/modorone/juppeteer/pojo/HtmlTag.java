package com.modorone.juppeteer.pojo;

/**
 * author: Shawn
 * time  : 2/23/20 4:05 PM
 * desc  :
 * update: Shawn 2/23/20 4:05 PM
 */
public class HtmlTag {

    private String url;
    private String path;
    private String content;
    private String type;

    public String getUrl() {
        return url;
    }

    public HtmlTag setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getPath() {
        return path;
    }

    public HtmlTag setPath(String path) {
        this.path = path;
        return this;
    }

    public String getContent() {
        return content;
    }

    public HtmlTag setContent(String content) {
        this.content = content;
        return this;
    }

    public String getType() {
        return type;
    }

    public HtmlTag setType(String type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return "ScriptTag{" +
                "url='" + url + '\'' +
                ", path='" + path + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
