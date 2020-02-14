package com.modorone.juppeteer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * author: Shawn
 * time  : 1/13/20 4:39 PM
 * desc  :
 * update: Shawn 1/13/20 4:39 PM
 */
public class Test {
    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {
        logger.info("main: ={}", "start...");
        try {
            Juppeteer.getInstance().launch(new Options());
        } catch (Exception e) {
            e.printStackTrace();
        }

//        final JsonObject payload = new JsonObject();
//        payload.add("id", new JsonPrimitive(valueOf(id)));
//        if ( sessionId != null ) {
//            payload.add("sessionId", new JsonPrimitive(sessionId));
//        }
//        payload.add("method", new JsonPrimitive(method));
//        if (hasArgs) {
//            payload.add("params", gson.toJsonTree(params));
//        }
    }
}
