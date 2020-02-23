package com.modorone.juppeteer;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.modorone.juppeteer.component.Page;
import com.modorone.juppeteer.component.network.Response;
import com.modorone.juppeteer.pojo.Cookie;
import com.modorone.juppeteer.pojo.HtmlTag;
import com.modorone.juppeteer.pojo.Viewport;
import com.modorone.juppeteer.util.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * author: Shawn
 * time  : 1/13/20 4:39 PM
 * desc  :
 * update: Shawn 1/13/20 4:39 PM
 */
public class Test {
    private static final Logger logger = LoggerFactory.getLogger(Test.class);

//    public static void testR(int i) {
//        if (i < 5) {
//            testR(++i);;
//        System.out.println("----------------->" + i);
//        }
//    }

    public static void main(String[] args) {
        logger.info("main: ={}", "start...");
        try {
            long start = System.currentTimeMillis();
            Browser browser = Juppeteer.getInstance().launch(new Options());
            Page page = browser.getPages().get(0);
            System.out.println("spent: " + (System.currentTimeMillis() - start));
            if (Objects.isNull(page)) page = browser.newPage();

            Response response = page.navigate("https://ip.cn",
                    CommandOptions.getDefault()
                            .setWaitUntil(WaitUntil.NETWORK_IDLE)
                            .setTimeout(5000));


            Response reload = page.reload(CommandOptions.getDefault());
            System.out.println("reload------" + reload.getText());

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
        page.setViewPort(new Viewport() {{
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
