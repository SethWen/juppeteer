package com.modorone.juppeteer;


import com.modorone.juppeteer.util.SystemUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * author: Shawn
 * time  : 2/21/20 11:32 AM
 * desc  :
 * update: Shawn 2/21/20 11:32 AM
 */
public class tmp {


    public static void main(String[] args) {
//        WaitUntil load = WaitUntil.LOAD;
//        System.out.println(load);
        Set<String> objects = ConcurrentHashMap.newKeySet();
        objects.add(null);

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
