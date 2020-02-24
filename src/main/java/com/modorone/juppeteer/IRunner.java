package com.modorone.juppeteer;

import java.util.List;

/**
 * author: Shawn
 * time  : 2/23/20 10:25 PM
 * desc  :
 * update: Shawn 2/23/20 10:25 PM
 */
public interface IRunner {

    String run(List<String> args) throws Exception;

    void terminate() throws Exception;
}
