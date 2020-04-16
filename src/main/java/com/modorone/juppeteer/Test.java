package com.modorone.juppeteer;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.component.Browser;
import com.modorone.juppeteer.component.Page;
import com.modorone.juppeteer.component.network.Response;
import com.modorone.juppeteer.pojo.Cookie;
import com.modorone.juppeteer.pojo.HtmlTag;
import com.modorone.juppeteer.pojo.Viewport;
import com.modorone.juppeteer.util.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

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
            long start = System.currentTimeMillis();
            Browser browser = Juppeteer.getInstance().launch(new SpawnRunner(), LaunchOptions.getDefault());
//            Browser browser = Juppeteer.getInstance().connect("ws://127.0.0.1:36393/devtools/browser/a48b33d9-6a01-4aec-92c2-d62fb0172348", new LaunchOptions());
            Page page = browser.getPages().get(0);
            page.setIgnoreHTTPSErrors(true);
//            page.evaluateOnNewDocument("function start(name, i, j) {\n" +
//                    "    setInterval(() => {\n" +
//                    "        console.info(\"---->\", name, i + j)\n" +
//                    "    }, 3000)\n" +
//                    "}", "shawn", 5, 3);

            Response response = page.navigate("https://www.baidu.com", CommandOptions.getDefault());
            String text = response.getText();
            System.out.println("spent: " + (System.currentTimeMillis() - start));
//            ElementHandle handle1 = page.waitForSelector("#kw", CommandOptions.getDefault());
//            ElementHandle handle2 = page.waitForXpath("//input", CommandOptions.getDefault());
//            handle.type("nodejs", null);

//            page.waitForFunction("function check() {\n" +
//                    "    return window.shawn === true\n" +
//                    "}", CommandOptions.getDefault().setTimeout(3000));

//            page.waitFor(3000);
//            page.evaluateOnNewDocument("((name, i, j) => {\n" +
//                    "console.log('test evaluate....')\n" +
//                    "    window.shawn = 'xixix'\n" +
//                    "})()");
//            page.waitForRequest(request -> StringUtil.contains(request.getUrl(), "sina"), CommandOptions.getDefault());
//            page.waitForResponse(res -> StringUtil.contains(res.getUrl(), "sina"), CommandOptions.getDefault());
//            String s = page.takeScreenshot("jpeg", "./", 80);
            long spent = System.currentTimeMillis() - start;
            System.out.println("");


//            SystemUtil.sleep(3000);
//            browser.close();
//            if (Objects.isNull(page)) page = browser.newPage();
//
////            page = browser.newPage();
//
//            Response response = page.navigate("https://ip.cn",
//                    CommandOptions.getDefault()
//                            .setWaitUntil(WaitUntil.NETWORK_IDLE)
//                            .setTimeout(5000));
//
//
//            System.out.println(page.isClosed());
//
//            page.close(true);

//            page.waitFor(6000);
//            page.bringToFront();
//            page.setBypassCSP(true);
//            page.emulateMediaType(MediaType.SCREEN);
//            page.emulateMediaFeatures(null);
//            page.emulateMediaFeatures(Arrays.asList(new MediaFeature(){{
//                setName("prefers-reduced-motion");
//                setValue("reduce");
//            }}));

//            String[] availableIDs = TimeZone.getAvailableIDs();
//            System.out.println(Arrays.toString(availableIDs));
//            page.emulateTimezone(availableIDs[0]);



//            Response reload = page.reload(CommandOptions.getDefault());
//            System.out.println("reload------" + reload.getText());

//            List<JSHandle.ElementHandle> els = page.$$("a");
//            System.out.println(els.size());

//            Object o = page.$eval("#su", "(element) => console.log(element.value)");
//            System.out.println(o);

//            Object a = page.$$eval("#su", "(element) => console.log('tttttttt')");
//            List<ElementHandle> handles = page.$x("//a");
//            System.out.println(handles.size());

//            System.out.println(page.getMetrics().toString(SerializerFeature.PrettyFormat));
//            page.setContent("hahah", CommandOptions.getDefault());

//            page.type("input", "github", new JSONObject());
//            JSHandle.ElementHandle submit = page.$("#su");
//            submit.click(new JSONObject());


//            System.out.println();


//            try {
//                Response response = page.navigate("https://ip.cn",
//                        CommandOptions.getDefault()
//                                .setWaitUntil(WaitUntil.NETWORK_IDLE)
//                                .setTimeout(5000));
//                System.out.println("222222222222222---------" + response.getText());
//            } catch (Exception e){
//                e.printStackTrace();
//            }
//            System.out.println("finished2222222222222222................");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testGo(Page page) throws TimeoutException, ExecutionException, InterruptedException {
        Response response = page.navigate("https://ip.cn",
                CommandOptions.getDefault()
                        .setWaitUntil(WaitUntil.NETWORK_IDLE)
                        .setTimeout(5000));

        response = page.navigate("https://www.baidu.com",
                CommandOptions.getDefault()
                        .setWaitUntil(WaitUntil.NETWORK_IDLE)
                        .setTimeout(5000));

        page.waitFor(3000);
        page.goBack(CommandOptions.getDefault());
        page.waitFor(3000);
        page.goForward(CommandOptions.getDefault());
    }

    private static void testAddTag(Page page) throws TimeoutException, InterruptedException {
        page.addScriptTag(new HtmlTag(){{
//                setUrl("https://csdnimg.cn/public/common/libs/jquery/jquery-1.9.1.min.js");
            setContent("document.shawn='wcy'");
        }});

        page.addStyleTag(new HtmlTag(){{
            setUrl("https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css");
        }});
    }

    private static void testCookie(Page page) throws TimeoutException {
        System.out.println(page.getUrl());
        List<Cookie> cookies = page.getCookies();
        System.out.println(cookies.size());


        page.setCookie(new Cookie(){{
            setName("DOC");
            setValue("testtest");
            setDomain("www.baidu.com");
        }});

        cookies = page.getCookies();
        System.out.println("222--------------" + cookies.size());

        page.deleteCookie(new Cookie() {{
            setName("DOC");
            setDomain("www.baidu.com");
        }});
        System.out.println("333--------------" + page.getCookies().size());
    }

    private static void testPage(Page page) throws Exception {
        page.type("#kw", "麻痹", new JSONObject());
        SystemUtil.sleep(3000);
        System.out.println(page.getUrl());
        System.out.println(page.getContent());
        System.out.println(page.getTitle());
        System.out.println(page.getMainFrame().getFrameInfo());

        page.type("#kw", "shawn", new JSONObject());
        page.click("#su", null);
        SystemUtil.sleep(3000);
        page.hover("a[name='tj_settingicon']");
        page.setViewport(new Viewport() {{
            setWidth(300);
            setHeight(400);
        }});

    }

    private static void testEvaluate(Page page) throws TimeoutException, InterruptedException {
        String pageFun = "(function(n) {\n" +
                "        console.log('content=' + n);\n" +
                "        return  n + 2;\n" +
                "    })(%d)";

        String pageFun2 = "(async(n) => {\n" +
                "        console.log('content=' + n);\n" +
                "        return  n + 2;\n" +
                "    })(%d)";
        String pageFun3 = "function(n, m) {\n" +
                "        console.log('content=' + n);\n" +
                "        return  n + m;\n" +
                "    }";
        int e1 = (int) page.evaluateCodeBlock4Value(String.format(pageFun, 5));
        System.out.println("-----e1----->" + e1);

        int e2 = (int) page.evaluateFunction4Value(pageFun3, 4, 8);
        System.out.println("-----e2----->" + e2);
    }

    private static void testBrowser(Browser browser) throws Exception {
        System.out.println(browser.getWSEndPoint());
        System.out.println(browser.getRunner());
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
