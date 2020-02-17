package com.modorone.juppeteer.protocol;

public interface FetchDomain {

    // event
    String requestPausedEvent = "Fetch.requestPaused";
    String authRequiredEvent = "Fetch.authRequired";

    // command
    String enableCommand = "Fetch.enable";
    String disableCommand = "Fetch.disable";
    String failRequestCommand = "Fetch.failRequest";
    String continueRequestCommand = "Fetch.continueRequest";
}
