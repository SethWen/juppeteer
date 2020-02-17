package com.modorone.juppeteer.protocol;

/**
 * author: Shawn
 * time  : 2/14/20 6:43 PM
 * desc  :
 * update: Shawn 2/14/20 6:43 PM
 */
public interface NetWorkDomain {

    // event
    String requestWillBeSentEvent = "Network.requestWillBeSent";
    String requestServedFromCacheEvent = "Network.requestServedFromCache";
    String responseReceivedEvent = "Network.responseReceived";
    String loadingFinishedEvent = "Network.loadingFinished";
    String loadingFailedEvent = "Network.loadingFailed";

    // command
    String enableCommand = "Network.enable";
    String setCacheDisabledCommand = "Network.setCacheDisabled";
    String setExtraHTTPHeadersCommand = "Network.setExtraHTTPHeaders";
    String emulateNetworkConditionsCommand = "Network.emulateNetworkConditions";
    String setUserAgentOverrideCommand = "Network.setUserAgentOverride";
}
