package com.modorone.juppeteer.protocol;

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
    
    // command
    String enableCommand = "Runtime.enable";
    String evaluateCommand = "Runtime.evaluate";
}
