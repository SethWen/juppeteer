package com.modorone.juppeteer;

import com.modorone.juppeteer.component.Browser;
import com.modorone.juppeteer.component.Page;
import org.junit.*;
import org.junit.Test;

/**
 * author: Shawn
 * time  : 4/20/20 6:13 PM
 * desc  :
 * update: Shawn 4/20/20 6:13 PM
 */
public class JuppeteerTest {

    private static Browser browser;
    private static Page page;

    @BeforeClass
    public static void setUp() throws Exception {
        browser = Juppeteer.getInstance().launch(new SpawnRunner(), LaunchOptions.getDefault());
        page = browser.getPages().get(0);
        page.navigate("https://www.baidu.com", CommandOptions.getDefault());
    }

    /**
     * Tears down the test fixture.
     * (Called after every test case method.)
     */
    @AfterClass
    public static void tearDown() throws Exception {
        browser.close();
    }

    @Test
    public void test1() {
        System.out.println("aaaaaaaaaa");
        Assert.assertNotEquals(6, "");
    }

    @Test
    public void test2() {
        System.out.println("xxxxxxxxxxxx");
        Assert.assertNotEquals(6, "");
    }
}
