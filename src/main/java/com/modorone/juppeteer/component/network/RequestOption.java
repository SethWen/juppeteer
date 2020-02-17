package com.modorone.juppeteer.component.network;

import java.util.Map;

public class RequestOption {
    private String url;
    private String method;
    private String postData;
    private Map<String, String> headers;

    public static Builder newBuilder() {
        return new Builder(new RequestOption());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPostData() {
        return postData;
    }

    public void setPostData(String postData) {
        this.postData = postData;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public static class Builder {
        private RequestOption option;

        public Builder(RequestOption option) {
            this.option = option;
        }

        public Builder url(String url) {
            this.option.setUrl(url);
            return this;
        }

        public Builder method(String method) {
            this.option.setMethod(method);
            return this;
        }

        public Builder postData(String postData) {
            this.option.setPostData(postData);
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.option.setHeaders(headers);
            return this;
        }

        public RequestOption build() {
            return option;
        }
    }
}