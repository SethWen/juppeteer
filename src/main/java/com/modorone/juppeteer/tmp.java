package com.modorone.juppeteer;


import com.modorone.juppeteer.util.SystemUtil;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;


/**
 * author: Shawn
 * time  : 2/21/20 11:32 AM
 * desc  :
 * update: Shawn 2/21/20 11:32 AM
 */
public class tmp {


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        testFuture();
    }

    private static void testFuture() throws InterruptedException, ExecutionException {
        CompletableFuture.allOf(
                new ArrayList<Supplier<String>>() {{
                    add(() -> {
                        SystemUtil.sleep(3000);
                        System.out.println("111");
                        return "111";
                    });
                    add(() -> {
                        SystemUtil.sleep(4000);
                        System.out.println("2222");
                        return "222";
                    });
                }}.stream().map(CompletableFuture::supplyAsync).toArray(CompletableFuture[]::new)
        ).get();

//        new ArrayList<Supplier<Boolean>>(){{
//            add(() -> {});
//        }}
//
//        CompletableFuture.anyOf(n)
    }

    static CompletableFuture<String> downloadWebPage(String pageLink) {
        return CompletableFuture.supplyAsync(() -> {
            // Code to download and return the web page's content
            SystemUtil.sleep(3000);
            System.out.println(Thread.currentThread().getName() + " - link: " + pageLink);
            return pageLink;
        });
    }

    public void test() {

        new Thread(() -> {

            synchronized (tmp.this) {
                HashMap<Object, Object> map1 = new HashMap<>();
                Object o = map1.get(null);
                System.out.println("1----------" + o);
                SystemUtil.sleep(6000);
            }
        }).start();
        Object o = new Object();
        new Thread(() -> {
            synchronized (o) {
                ConcurrentHashMap<Object, Object> map2 = new ConcurrentHashMap<>();
                Object o2 = map2.get("null");
                System.out.println("2----------" + o2);
            }
        }).start();

    }
}
