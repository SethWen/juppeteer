package com.modorone.juppeteer.protocol;

/**
 * author: Shawn
 * time  : 2/14/20 12:57 PM
 * desc  :
 * update: Shawn 2/14/20 12:57 PM
 */
public interface PageDomain {

    // event
    String frameAttachedEvent = "Page.frameAttached";
    String frameNavigatedEvent = "Page.frameNavigated";
    String navigatedWithinDocumentEvent = "Page.navigatedWithinDocument";
    String frameDetachedEvent = "Page.frameDetached";
    String frameStoppedLoadingEvent = "Page.frameStoppedLoading";
    String lifecycleEventEvent = "Page.lifecycleEvent";

    // command
    String setInterceptFileChooserDialogCommand = "Page.setInterceptFileChooserDialog";
    String enableCommand = "Page.enable";
    String getFrameTreeCommand = "Page.getFrameTree";
    String setLifecycleEventsEnabledCommand = "Page.setLifecycleEventsEnabled";
}
