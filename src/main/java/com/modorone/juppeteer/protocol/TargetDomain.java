package com.modorone.juppeteer.protocol;

/**
 * author: Shawn
 * time  : 2/14/20 12:54 PM
 * desc  :
 * update: Shawn 2/14/20 12:54 PM
 */
public interface TargetDomain {
    // type


    // event
    String attachedToTargetEvent = "Target.attachedToTarget";
    String detachedToTargetEvent = "Target.detachedFromTarget";
    String targetCreatedEvent = "Target.targetCreated";
    String targetDestroyedEvent = "Target.targetDestroyed";
    String targetChangedEvent = "Target.targetInfoChanged";

    // command
    String setDiscoverTargetsCommand = "Target.setDiscoverTargets";
    String createTargetCommand = "Target.createTarget";
    String attachToTargetCommand = "Target.attachToTarget";
    String detachToTargetCommand = "Target.detachFromTarget";
    String setAutoAttachCommand = "Target.setAutoAttach";

}
