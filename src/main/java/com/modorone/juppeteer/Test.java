package com.modorone.juppeteer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.component.Page;
import com.modorone.juppeteer.util.StringUtil;
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
            Browser browser = Juppeteer.getInstance().launch(new Options());
            Page page = browser.newPage();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        String reply = "{\"id\":1,\"result\":{\"targetInfos\":[{\"targetId\":\"D0D5C7EE9534839C03E5C2C530A887E6\",\"type\":\"page\",\"title\":\"New Tab\",\"url\":\"chrome://newtab/\",\"attached\":false,\"browserContextId\":\"EC1A9D09DA184048EB30105E32E22B57\"}]}} ";
//        JSONObject json = JSON.parseObject(reply);
//        JSONArray jsonArray = json.getJSONObject("result").getJSONArray("targetInfos");
//        for (Object o : jsonArray) {
//            JSONObject j = (JSONObject) o;
//            if (StringUtil.equals("page", j.getString("type"))) {
//                System.out.println(j.getString("targetId"));
//            }
//        }

    }
}
