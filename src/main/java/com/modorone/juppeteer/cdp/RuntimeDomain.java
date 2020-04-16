package com.modorone.juppeteer.cdp;

/**
 * author: Shawn
 * time  : 2/14/20 12:54 PM
 * desc  :
 * update: Shawn 2/14/20 12:54 PM
 */
public interface RuntimeDomain {

    // event
    String executionContextCreatedEvent = "Runtime.executionContextCreated";
    String executionContextDestroyedEvent = "Runtime.executionContextDestroyed";
    String executionContextsClearedEvent = "Runtime.executionContextsCleared";
    String consoleAPICalledEvent = "Runtime.consoleAPICalled";

    // command
    String enableCommand = "Runtime.enable";
    String evaluateCommand = "Runtime.evaluate";
    String getPropertiesCommand = "Runtime.getProperties";
    String releaseObjectCommand = "Runtime.releaseObject";
    String callFunctionOnCommand = "Runtime.callFunctionOn";
}
