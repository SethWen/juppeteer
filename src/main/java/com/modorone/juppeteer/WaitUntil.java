package com.modorone.juppeteer;

public enum WaitUntil {

    // 但是请注意， page 的 lifecycle 并非完全线性， puppeteer 实现中 waitUtil 是个数组，比如：firstMeaningfulPaint，就不太方便插入到下面
    // init -> commit -> DOMContentLoaded -> load -> networkAlmostIdle - networkIdle
    LOAD("load"),
    DOM_CONTENT_LOADED("DOMContentLoaded"),
    NETWORK_ALMOST_IDLE("networkAlmostIdle"),
    NETWORK_IDLE("networkIdle");

    private String value;

    WaitUntil(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "WaitUntil{" +
                "value='" + value + '\'' +
                '}';
    }
}
