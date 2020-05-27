package com.modorone.juppeteer;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.component.Browser;
import com.modorone.juppeteer.component.Dialog;
import com.modorone.juppeteer.component.ElementHandle;
import com.modorone.juppeteer.component.Page;
import com.modorone.juppeteer.pojo.Cookie;
import com.modorone.juppeteer.pojo.HtmlTag;
import com.modorone.juppeteer.util.StringUtil;
import com.modorone.juppeteer.util.SystemUtil;
import com.modorone.juppeteer.util.ThreadExecutor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

/**
 * author: Shawn
 * time  : 4/20/20 6:11 PM
 * desc  :
 * update: Shawn 4/20/20 6:11 PM
 */
public class PageTest {

    private final String baidu = "https://www.baidu.com/";
    private final String sina = "http://sports.sina.com.cn/";
    private Browser browser;
    private Page page;

    @Before
    public void setUp() throws Exception {
        browser = Juppeteer.getInstance().launch(new SpawnRunner(), LaunchOptions.getDefault());
        page = browser.getPages().get(0);
    }

    @After
    public void tearDown() {
        try {
            page.close(false);
        } catch (Exception ignore) {
        }
        try {
            browser.close();
        } catch (Exception ignore) {
        }
    }

    @Test
    public void testNavigator() throws InterruptedException, ExecutionException, TimeoutException {
        page.navigate(baidu, CommandOptions.getDefault().setTimeout(30000));
        Assert.assertEquals(baidu, page.getUrl());
        page.goBack(CommandOptions.getDefault());
        Assert.assertEquals("about:blank", page.getUrl());
        page.goForward(CommandOptions.getDefault());
        Assert.assertEquals(baidu, page.getUrl());
    }

    @Test
    public void testSelector() throws TimeoutException {
        page.navigate(baidu, CommandOptions.getDefault().setTimeout(30000));
        Assert.assertNotNull(page.$("#kw"));
        Assert.assertNotNull(page.$$("a"));
        Assert.assertNotNull(page.$x("//a"));
        Assert.assertTrue(page.$$("a").size() > 0);

        String s = (String) page.$eval("#kw", "(element) => element.value = 'puppeteer'");
        Assert.assertEquals("puppeteer", s);
        int i = (int) page.$$eval("a", "(elements) => elements.length");
        Assert.assertTrue(i > 0);
    }

    @Test
    public void testEvaluation() throws TimeoutException, InterruptedException {
        String pageFun = "(function(n) {\n" +
                "        console.log('content=' + n);\n" +
                "        return  n + 2;\n" +
                "    })(%d)";
        String pageFun3 = "function(n, m) {\n" +
                "        console.log('content=' + n);\n" +
                "        return  n + m;\n" +
                "    }";
        int e1 = (int) page.evaluateCodeBlock4Value(String.format(pageFun, 5));
        Assert.assertEquals(7, e1);
        int e2 = (int) page.evaluateFunction4Value(pageFun3, 4, 8);
        Assert.assertEquals(12, e2);
    }

    @Test
    public void testAction() throws TimeoutException {
        page.navigate(baidu, CommandOptions.getDefault().setWaitUntil(WaitUntil.NETWORK_IDLE).setTimeout(30000));
        page.type("#kw", "puppeteer", new JSONObject());
        page.click("#su", new JSONObject());
    }

    @Test
    public void testWaiter() throws TimeoutException {
        page.navigate(baidu, CommandOptions.getDefault().setWaitUntil(WaitUntil.NETWORK_IDLE).setTimeout(30000));
        ElementHandle input1 = page.waitForSelector("#kw", CommandOptions.getDefault());
        Assert.assertNotNull(input1);
        ElementHandle input2 = page.waitForXpath("//input", CommandOptions.getDefault());
        Assert.assertNotNull(input2);

        ThreadExecutor.getInstance().execute(() -> {
            try {
                SystemUtil.sleep(1000);
                page.evaluateCodeBlock4Value("window.juppeteer = true");
            } catch (Exception ignore) {
            }
        });
        long start = System.currentTimeMillis();
        page.waitForFunction("function check() {\n" +
                "    return window.juppeteer === true\n" +
                "}", CommandOptions.getDefault().setTimeout(3000));
        Assert.assertTrue((System.currentTimeMillis() - start) > 1000);

        ThreadExecutor.getInstance().execute(() -> {
            try {
                SystemUtil.sleep(1000);
                page.navigate(sina, CommandOptions.getDefault());
            } catch (Exception ignore) {
            }
        });

        start = System.currentTimeMillis();
        page.waitForRequest(request -> StringUtil.contains(request.getUrl(), "sina"), CommandOptions.getDefault());
        // FIXME: 5/27/20 目前连续 navigate 会出现问题
//        page.waitForResponse(res -> StringUtil.contains(res.getUrl(), "sina"), CommandOptions.getDefault());
        Assert.assertTrue((System.currentTimeMillis() - start) > 1000);
    }

    @Test
    public void testHook() throws TimeoutException, InterruptedException {
        page.evaluateOnNewDocument("() => window.juppeteer = true");

        // websocket 一个线程收消息, 这里会发生阻塞
//        AtomicInteger atomicInteger = new AtomicInteger();
//        page.setRequestInterception(true);
//        page.addRequestInterceptor(request -> {
//            try {
//                atomicInteger.incrementAndGet();
//                request.proceed(RequestOption.newBuilder().build());
//            } catch (TimeoutException e) {
//                e.printStackTrace();
//            }
//        });

        page.navigate(baidu, CommandOptions.getDefault().setWaitUntil(WaitUntil.NETWORK_IDLE).setTimeout(30000));
        boolean b1 = (boolean) page.evaluateCodeBlock4Value("(() => window.juppeteer)()");
        Assert.assertTrue(b1);
//        Assert.assertTrue(atomicInteger.get() > 0);

//        page.navigate(sina, CommandOptions.getDefault().setWaitUntil(WaitUntil.NETWORK_IDLE).setTimeout(30000));
//        boolean b2 = (boolean) page.evaluateCodeBlock4Value("(() => window.juppeteer)()");
//        Assert.assertTrue(b2);

        page.setDialogConsumer(new Consumer<Dialog>() {
            @Override
            public void accept(Dialog dialog) {
                dialog.dismiss();
            }
        });
    }

    @Test
    public void testTakeScreenshot() {
//        String s = page.takeScreenshot("jpeg", "./", 80);
    }

    public void testAddTag(Page page) throws TimeoutException, InterruptedException {
        page.addScriptTag(new HtmlTag() {{
//                setUrl("https://csdnimg.cn/public/common/libs/jquery/jquery-1.9.1.min.js");
            setContent("document.shawn='wcy'");
        }});

        page.addStyleTag(new HtmlTag() {{
            setUrl("https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css");
        }});
    }

    public void testCookie(Page page) throws TimeoutException {
        System.out.println(page.getUrl());
        List<Cookie> cookies = page.getCookies();
        System.out.println(cookies.size());


        page.setCookie(new Cookie() {{
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


    public void testOthers() {
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
    }

}
