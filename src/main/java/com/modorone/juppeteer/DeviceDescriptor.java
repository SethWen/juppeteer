package com.modorone.juppeteer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * author: Shawn
 * time  : 1/13/20 2:54 PM
 * desc  :
 * update: Shawn 1/13/20 2:54 PM
 */
public class DeviceDescriptor {

    private static Map<String, JSONObject> deviceMap = new HashMap<>();

    public static JSONObject getDevice(String name) {
        if (deviceMap.size() == 0) {
            String DEVICES = "[\n" +
                    "  {\n" +
                    "    'name': 'Blackberry PlayBook',\n" +
                    "    'userAgent': 'Mozilla/5.0 (PlayBook; U; RIM Tablet OS 2.1.0; en-US) AppleWebKit/536.2+ (KHTML like Gecko) Version/7.2.1.0 Safari/536.2+',\n" +
                    "    'viewport': {\n" +
                    "      'width': 600,\n" +
                    "      'height': 1024,\n" +
                    "      'deviceScaleFactor': 1,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Blackberry PlayBook landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (PlayBook; U; RIM Tablet OS 2.1.0; en-US) AppleWebKit/536.2+ (KHTML like Gecko) Version/7.2.1.0 Safari/536.2+',\n" +
                    "    'viewport': {\n" +
                    "      'width': 1024,\n" +
                    "      'height': 600,\n" +
                    "      'deviceScaleFactor': 1,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'BlackBerry Z30',\n" +
                    "    'userAgent': 'Mozilla/5.0 (BB10; Touch) AppleWebKit/537.10+ (KHTML, like Gecko) Version/10.0.9.2372 Mobile Safari/537.10+',\n" +
                    "    'viewport': {\n" +
                    "      'width': 360,\n" +
                    "      'height': 640,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'BlackBerry Z30 landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (BB10; Touch) AppleWebKit/537.10+ (KHTML, like Gecko) Version/10.0.9.2372 Mobile Safari/537.10+',\n" +
                    "    'viewport': {\n" +
                    "      'width': 640,\n" +
                    "      'height': 360,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Galaxy Note 3',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; U; Android 4.3; en-us; SM-N900T Build/JSS15J) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30',\n" +
                    "    'viewport': {\n" +
                    "      'width': 360,\n" +
                    "      'height': 640,\n" +
                    "      'deviceScaleFactor': 3,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Galaxy Note 3 landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; U; Android 4.3; en-us; SM-N900T Build/JSS15J) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30',\n" +
                    "    'viewport': {\n" +
                    "      'width': 640,\n" +
                    "      'height': 360,\n" +
                    "      'deviceScaleFactor': 3,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Galaxy Note II',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; U; Android 4.1; en-us; GT-N7100 Build/JRO03C) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30',\n" +
                    "    'viewport': {\n" +
                    "      'width': 360,\n" +
                    "      'height': 640,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Galaxy Note II landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; U; Android 4.1; en-us; GT-N7100 Build/JRO03C) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30',\n" +
                    "    'viewport': {\n" +
                    "      'width': 640,\n" +
                    "      'height': 360,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Galaxy S III',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; U; Android 4.0; en-us; GT-I9300 Build/IMM76D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30',\n" +
                    "    'viewport': {\n" +
                    "      'width': 360,\n" +
                    "      'height': 640,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Galaxy S III landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; U; Android 4.0; en-us; GT-I9300 Build/IMM76D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30',\n" +
                    "    'viewport': {\n" +
                    "      'width': 640,\n" +
                    "      'height': 360,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Galaxy S5',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3765.0 Mobile Safari/537.36',\n" +
                    "    'viewport': {\n" +
                    "      'width': 360,\n" +
                    "      'height': 640,\n" +
                    "      'deviceScaleFactor': 3,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Galaxy S5 landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3765.0 Mobile Safari/537.36',\n" +
                    "    'viewport': {\n" +
                    "      'width': 640,\n" +
                    "      'height': 360,\n" +
                    "      'deviceScaleFactor': 3,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPad',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPad; CPU OS 11_0 like Mac OS X) AppleWebKit/604.1.34 (KHTML, like Gecko) Version/11.0 Mobile/15A5341f Safari/604.1',\n" +
                    "    'viewport': {\n" +
                    "      'width': 768,\n" +
                    "      'height': 1024,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPad landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPad; CPU OS 11_0 like Mac OS X) AppleWebKit/604.1.34 (KHTML, like Gecko) Version/11.0 Mobile/15A5341f Safari/604.1',\n" +
                    "    'viewport': {\n" +
                    "      'width': 1024,\n" +
                    "      'height': 768,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPad Mini',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPad; CPU OS 11_0 like Mac OS X) AppleWebKit/604.1.34 (KHTML, like Gecko) Version/11.0 Mobile/15A5341f Safari/604.1',\n" +
                    "    'viewport': {\n" +
                    "      'width': 768,\n" +
                    "      'height': 1024,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPad Mini landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPad; CPU OS 11_0 like Mac OS X) AppleWebKit/604.1.34 (KHTML, like Gecko) Version/11.0 Mobile/15A5341f Safari/604.1',\n" +
                    "    'viewport': {\n" +
                    "      'width': 1024,\n" +
                    "      'height': 768,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPad Pro',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPad; CPU OS 11_0 like Mac OS X) AppleWebKit/604.1.34 (KHTML, like Gecko) Version/11.0 Mobile/15A5341f Safari/604.1',\n" +
                    "    'viewport': {\n" +
                    "      'width': 1024,\n" +
                    "      'height': 1366,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPad Pro landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPad; CPU OS 11_0 like Mac OS X) AppleWebKit/604.1.34 (KHTML, like Gecko) Version/11.0 Mobile/15A5341f Safari/604.1',\n" +
                    "    'viewport': {\n" +
                    "      'width': 1366,\n" +
                    "      'height': 1024,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPhone 4',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPhone; CPU iPhone OS 7_1_2 like Mac OS X) AppleWebKit/537.51.2 (KHTML, like Gecko) Version/7.0 Mobile/11D257 Safari/9537.53',\n" +
                    "    'viewport': {\n" +
                    "      'width': 320,\n" +
                    "      'height': 480,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPhone 4 landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPhone; CPU iPhone OS 7_1_2 like Mac OS X) AppleWebKit/537.51.2 (KHTML, like Gecko) Version/7.0 Mobile/11D257 Safari/9537.53',\n" +
                    "    'viewport': {\n" +
                    "      'width': 480,\n" +
                    "      'height': 320,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPhone 5',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.0 Mobile/14E304 Safari/602.1',\n" +
                    "    'viewport': {\n" +
                    "      'width': 320,\n" +
                    "      'height': 568,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPhone 5 landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.0 Mobile/14E304 Safari/602.1',\n" +
                    "    'viewport': {\n" +
                    "      'width': 568,\n" +
                    "      'height': 320,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPhone 6',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1',\n" +
                    "    'viewport': {\n" +
                    "      'width': 375,\n" +
                    "      'height': 667,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPhone 6 landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1',\n" +
                    "    'viewport': {\n" +
                    "      'width': 667,\n" +
                    "      'height': 375,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPhone 6 Plus',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1',\n" +
                    "    'viewport': {\n" +
                    "      'width': 414,\n" +
                    "      'height': 736,\n" +
                    "      'deviceScaleFactor': 3,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPhone 6 Plus landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1',\n" +
                    "    'viewport': {\n" +
                    "      'width': 736,\n" +
                    "      'height': 414,\n" +
                    "      'deviceScaleFactor': 3,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPhone 7',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1',\n" +
                    "    'viewport': {\n" +
                    "      'width': 375,\n" +
                    "      'height': 667,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPhone 7 landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1',\n" +
                    "    'viewport': {\n" +
                    "      'width': 667,\n" +
                    "      'height': 375,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPhone 7 Plus',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1',\n" +
                    "    'viewport': {\n" +
                    "      'width': 414,\n" +
                    "      'height': 736,\n" +
                    "      'deviceScaleFactor': 3,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPhone 7 Plus landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1',\n" +
                    "    'viewport': {\n" +
                    "      'width': 736,\n" +
                    "      'height': 414,\n" +
                    "      'deviceScaleFactor': 3,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPhone 8',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1',\n" +
                    "    'viewport': {\n" +
                    "      'width': 375,\n" +
                    "      'height': 667,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPhone 8 landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1',\n" +
                    "    'viewport': {\n" +
                    "      'width': 667,\n" +
                    "      'height': 375,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPhone 8 Plus',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1',\n" +
                    "    'viewport': {\n" +
                    "      'width': 414,\n" +
                    "      'height': 736,\n" +
                    "      'deviceScaleFactor': 3,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPhone 8 Plus landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1',\n" +
                    "    'viewport': {\n" +
                    "      'width': 736,\n" +
                    "      'height': 414,\n" +
                    "      'deviceScaleFactor': 3,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPhone SE',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.0 Mobile/14E304 Safari/602.1',\n" +
                    "    'viewport': {\n" +
                    "      'width': 320,\n" +
                    "      'height': 568,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPhone SE landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.0 Mobile/14E304 Safari/602.1',\n" +
                    "    'viewport': {\n" +
                    "      'width': 568,\n" +
                    "      'height': 320,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPhone X',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1',\n" +
                    "    'viewport': {\n" +
                    "      'width': 375,\n" +
                    "      'height': 812,\n" +
                    "      'deviceScaleFactor': 3,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPhone X landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1',\n" +
                    "    'viewport': {\n" +
                    "      'width': 812,\n" +
                    "      'height': 375,\n" +
                    "      'deviceScaleFactor': 3,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPhone XR',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.0 Mobile/15E148 Safari/604.1',\n" +
                    "    'viewport': {\n" +
                    "      'width': 414,\n" +
                    "      'height': 896,\n" +
                    "      'deviceScaleFactor': 3,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'iPhone XR landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.0 Mobile/15E148 Safari/604.1',\n" +
                    "    'viewport': {\n" +
                    "      'width': 896,\n" +
                    "      'height': 414,\n" +
                    "      'deviceScaleFactor': 3,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'JioPhone 2',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Mobile; LYF/F300B/LYF-F300B-001-01-15-130718-i;Android; rv:48.0) Gecko/48.0 Firefox/48.0 KAIOS/2.5',\n" +
                    "    'viewport': {\n" +
                    "      'width': 240,\n" +
                    "      'height': 320,\n" +
                    "      'deviceScaleFactor': 1,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'JioPhone 2 landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Mobile; LYF/F300B/LYF-F300B-001-01-15-130718-i;Android; rv:48.0) Gecko/48.0 Firefox/48.0 KAIOS/2.5',\n" +
                    "    'viewport': {\n" +
                    "      'width': 320,\n" +
                    "      'height': 240,\n" +
                    "      'deviceScaleFactor': 1,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Kindle Fire HDX',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; U; en-us; KFAPWI Build/JDQ39) AppleWebKit/535.19 (KHTML, like Gecko) Silk/3.13 Safari/535.19 Silk-Accelerated=true',\n" +
                    "    'viewport': {\n" +
                    "      'width': 800,\n" +
                    "      'height': 1280,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Kindle Fire HDX landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; U; en-us; KFAPWI Build/JDQ39) AppleWebKit/535.19 (KHTML, like Gecko) Silk/3.13 Safari/535.19 Silk-Accelerated=true',\n" +
                    "    'viewport': {\n" +
                    "      'width': 1280,\n" +
                    "      'height': 800,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'LG Optimus L70',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; U; Android 4.4.2; en-us; LGMS323 Build/KOT49I.MS32310c) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/75.0.3765.0 Mobile Safari/537.36',\n" +
                    "    'viewport': {\n" +
                    "      'width': 384,\n" +
                    "      'height': 640,\n" +
                    "      'deviceScaleFactor': 1.25,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'LG Optimus L70 landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; U; Android 4.4.2; en-us; LGMS323 Build/KOT49I.MS32310c) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/75.0.3765.0 Mobile Safari/537.36',\n" +
                    "    'viewport': {\n" +
                    "      'width': 640,\n" +
                    "      'height': 384,\n" +
                    "      'deviceScaleFactor': 1.25,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Microsoft Lumia 550',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Windows Phone 10.0; Android 4.2.1; Microsoft; Lumia 550) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Mobile Safari/537.36 Edge/14.14263',\n" +
                    "    'viewport': {\n" +
                    "      'width': 640,\n" +
                    "      'height': 360,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Microsoft Lumia 950',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Windows Phone 10.0; Android 4.2.1; Microsoft; Lumia 950) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Mobile Safari/537.36 Edge/14.14263',\n" +
                    "    'viewport': {\n" +
                    "      'width': 360,\n" +
                    "      'height': 640,\n" +
                    "      'deviceScaleFactor': 4,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Microsoft Lumia 950 landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Windows Phone 10.0; Android 4.2.1; Microsoft; Lumia 950) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Mobile Safari/537.36 Edge/14.14263',\n" +
                    "    'viewport': {\n" +
                    "      'width': 640,\n" +
                    "      'height': 360,\n" +
                    "      'deviceScaleFactor': 4,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Nexus 10',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; Android 6.0.1; Nexus 10 Build/MOB31T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3765.0 Safari/537.36',\n" +
                    "    'viewport': {\n" +
                    "      'width': 800,\n" +
                    "      'height': 1280,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Nexus 10 landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; Android 6.0.1; Nexus 10 Build/MOB31T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3765.0 Safari/537.36',\n" +
                    "    'viewport': {\n" +
                    "      'width': 1280,\n" +
                    "      'height': 800,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Nexus 4',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; Android 4.4.2; Nexus 4 Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3765.0 Mobile Safari/537.36',\n" +
                    "    'viewport': {\n" +
                    "      'width': 384,\n" +
                    "      'height': 640,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Nexus 4 landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; Android 4.4.2; Nexus 4 Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3765.0 Mobile Safari/537.36',\n" +
                    "    'viewport': {\n" +
                    "      'width': 640,\n" +
                    "      'height': 384,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Nexus 5',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3765.0 Mobile Safari/537.36',\n" +
                    "    'viewport': {\n" +
                    "      'width': 360,\n" +
                    "      'height': 640,\n" +
                    "      'deviceScaleFactor': 3,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Nexus 5 landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3765.0 Mobile Safari/537.36',\n" +
                    "    'viewport': {\n" +
                    "      'width': 640,\n" +
                    "      'height': 360,\n" +
                    "      'deviceScaleFactor': 3,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Nexus 5X',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; Android 8.0.0; Nexus 5X Build/OPR4.170623.006) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3765.0 Mobile Safari/537.36',\n" +
                    "    'viewport': {\n" +
                    "      'width': 412,\n" +
                    "      'height': 732,\n" +
                    "      'deviceScaleFactor': 2.625,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Nexus 5X landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; Android 8.0.0; Nexus 5X Build/OPR4.170623.006) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3765.0 Mobile Safari/537.36',\n" +
                    "    'viewport': {\n" +
                    "      'width': 732,\n" +
                    "      'height': 412,\n" +
                    "      'deviceScaleFactor': 2.625,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Nexus 6',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; Android 7.1.1; Nexus 6 Build/N6F26U) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3765.0 Mobile Safari/537.36',\n" +
                    "    'viewport': {\n" +
                    "      'width': 412,\n" +
                    "      'height': 732,\n" +
                    "      'deviceScaleFactor': 3.5,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Nexus 6 landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; Android 7.1.1; Nexus 6 Build/N6F26U) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3765.0 Mobile Safari/537.36',\n" +
                    "    'viewport': {\n" +
                    "      'width': 732,\n" +
                    "      'height': 412,\n" +
                    "      'deviceScaleFactor': 3.5,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Nexus 6P',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; Android 8.0.0; Nexus 6P Build/OPP3.170518.006) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3765.0 Mobile Safari/537.36',\n" +
                    "    'viewport': {\n" +
                    "      'width': 412,\n" +
                    "      'height': 732,\n" +
                    "      'deviceScaleFactor': 3.5,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Nexus 6P landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; Android 8.0.0; Nexus 6P Build/OPP3.170518.006) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3765.0 Mobile Safari/537.36',\n" +
                    "    'viewport': {\n" +
                    "      'width': 732,\n" +
                    "      'height': 412,\n" +
                    "      'deviceScaleFactor': 3.5,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Nexus 7',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; Android 6.0.1; Nexus 7 Build/MOB30X) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3765.0 Safari/537.36',\n" +
                    "    'viewport': {\n" +
                    "      'width': 600,\n" +
                    "      'height': 960,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Nexus 7 landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; Android 6.0.1; Nexus 7 Build/MOB30X) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3765.0 Safari/537.36',\n" +
                    "    'viewport': {\n" +
                    "      'width': 960,\n" +
                    "      'height': 600,\n" +
                    "      'deviceScaleFactor': 2,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Nokia Lumia 520',\n" +
                    "    'userAgent': 'Mozilla/5.0 (compatible; MSIE 10.0; Windows Phone 8.0; Trident/6.0; IEMobile/10.0; ARM; Touch; NOKIA; Lumia 520)',\n" +
                    "    'viewport': {\n" +
                    "      'width': 320,\n" +
                    "      'height': 533,\n" +
                    "      'deviceScaleFactor': 1.5,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Nokia Lumia 520 landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (compatible; MSIE 10.0; Windows Phone 8.0; Trident/6.0; IEMobile/10.0; ARM; Touch; NOKIA; Lumia 520)',\n" +
                    "    'viewport': {\n" +
                    "      'width': 533,\n" +
                    "      'height': 320,\n" +
                    "      'deviceScaleFactor': 1.5,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Nokia N9',\n" +
                    "    'userAgent': 'Mozilla/5.0 (MeeGo; NokiaN9) AppleWebKit/534.13 (KHTML, like Gecko) NokiaBrowser/8.5.0 Mobile Safari/534.13',\n" +
                    "    'viewport': {\n" +
                    "      'width': 480,\n" +
                    "      'height': 854,\n" +
                    "      'deviceScaleFactor': 1,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Nokia N9 landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (MeeGo; NokiaN9) AppleWebKit/534.13 (KHTML, like Gecko) NokiaBrowser/8.5.0 Mobile Safari/534.13',\n" +
                    "    'viewport': {\n" +
                    "      'width': 854,\n" +
                    "      'height': 480,\n" +
                    "      'deviceScaleFactor': 1,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Pixel 2',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3765.0 Mobile Safari/537.36',\n" +
                    "    'viewport': {\n" +
                    "      'width': 411,\n" +
                    "      'height': 731,\n" +
                    "      'deviceScaleFactor': 2.625,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Pixel 2 landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3765.0 Mobile Safari/537.36',\n" +
                    "    'viewport': {\n" +
                    "      'width': 731,\n" +
                    "      'height': 411,\n" +
                    "      'deviceScaleFactor': 2.625,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Pixel 2 XL',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; Android 8.0.0; Pixel 2 XL Build/OPD1.170816.004) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3765.0 Mobile Safari/537.36',\n" +
                    "    'viewport': {\n" +
                    "      'width': 411,\n" +
                    "      'height': 823,\n" +
                    "      'deviceScaleFactor': 3.5,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': false\n" +
                    "    }\n" +
                    "  },\n" +
                    "  {\n" +
                    "    'name': 'Pixel 2 XL landscape',\n" +
                    "    'userAgent': 'Mozilla/5.0 (Linux; Android 8.0.0; Pixel 2 XL Build/OPD1.170816.004) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3765.0 Mobile Safari/537.36',\n" +
                    "    'viewport': {\n" +
                    "      'width': 823,\n" +
                    "      'height': 411,\n" +
                    "      'deviceScaleFactor': 3.5,\n" +
                    "      'isMobile': true,\n" +
                    "      'hasTouch': true,\n" +
                    "      'isLandscape': true\n" +
                    "    }\n" +
                    "  }\n" +
                    "]";
            JSONArray array = JSON.parseArray(DEVICES);
            array.forEach(json -> {
                JSONObject o = (JSONObject) json;
                deviceMap.put(o.getString("name"), o);
            });
        }
        return deviceMap.get(name);
    }
}
