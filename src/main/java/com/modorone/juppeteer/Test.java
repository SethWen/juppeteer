package com.modorone.juppeteer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.component.Page;
import com.modorone.juppeteer.component.Target;
import com.modorone.juppeteer.util.StringUtil;
import com.modorone.juppeteer.util.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.midi.Soundbank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

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
//        JSONObject json = JSON.parseObject("{\"method\":\"Network.requestWillBeSent\",\"params\":{\"requestId\":\"B7533B6252FFF6FFB02768B0E61A9350\",\"loaderId\":\"B7533B6252FFF6FFB02768B0E61A9350\",\"documentURL\":\"https://www.baidu.com/\",\"request\":{\"url\":\"https://www.baidu.com/\",\"method\":\"GET\",\"headers\":{\"Upgrade-Insecure-Requests\":\"1\",\"User-Agent\":\"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.0 Safari/537.36\",\"Sec-Fetch-Dest\":\"document\"},\"mixedContentType\":\"none\",\"initialPriority\":\"VeryHigh\",\"referrerPolicy\":\"no-referrer-when-downgrade\"},\"timestamp\":17615.078838,\"wallTime\":1581922620.017033,\"initiator\":{\"type\":\"other\"},\"type\":\"Document\",\"frameId\":\"C43347C629294C9DB73F52999BF26ACE\",\"hasUserGesture\":false},\"sessionId\":\"AF9AED5531E57B299F739EE62E6179ED\"}");
//
//        json.getJSONObject("params").getJSONObject("request").getJSONObject("headers").forEach((key, value) -> {
//            System.out.println(key + ":" + value);
//        });
        try {
            Browser browser = Juppeteer.getInstance().launch(new Options());
            Page page = browser.getPages().get(0);
//            page.setGeolocation(100, 50, 2);
            page.navigate("https://www.baidu.com");
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

    private static void testBrowser(Browser browser) throws TimeoutException {
        System.out.println(browser.getWSEndPoint());
        System.out.println(browser.getProcess());
        System.out.println(browser.getTargets());
        System.out.println(browser.getTarget().getTargetInfo());
        System.out.println(browser.getVersion());
        System.out.println(browser.getUserAgent());

//            browser.newIncognitoPage();
//            System.out.println("pages==============" + browser.getPages());
//            Set<String> contexts = browser.getContexts();
//            String id = (String) contexts.toArray()[0];
//            System.out.println("pages==============" + browser.getPages() + "---" + id);
//            System.out.println("pages==============" + browser.getPages() + "---" + id);
//            System.out.println("pages==============" + browser.getPages() + "---" + id);
//            System.out.println("pages==============" + browser.getPages() + "---" + id);
//            System.out.println("pages==============" + browser.getPages() + "---" + id);

//            SystemUtil.sleep(5000);
//            browser.disposeContext(id);
//            System.out.println("pages==============" + browser.getPages() + "---" + id);
//            System.out.println("pages==============" + browser.getContexts() + "---" + id);
////            Page page = browser.newPage();
//            List<Target> targets = browser.getTargets();
//            Predicate<Target.TargetInfo> targetInfoPredicate = o -> o.getTargetId() == targets.get(0).getTargetInfo().getTargetId();
//            Predicate<Target.TargetInfo> targetInfoPredicate = o -> false;
//            browser.waitForTarget(targetInfoPredicate, 5000);

        browser.close();
        System.out.println(browser.isAlive());
    }
}
