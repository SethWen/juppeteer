package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.exception.JuppeteerException;
import com.modorone.juppeteer.protocol.DOMDomain;
import com.modorone.juppeteer.protocol.PageDomain;
import com.modorone.juppeteer.protocol.RuntimeDomain;
import com.modorone.juppeteer.util.StringUtil;

import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

/**
 * author: Shawn
 * time  : 2/14/20 6:09 PM
 * desc  :
 * update: Shawn 2/14/20 6:09 PM
 */
public class JSHandle {

    protected ExecutionContext mContext;
    protected CDPSession mSession;
    protected JSONObject mRemoteObject;
    protected boolean mDisposed;
//     this._context = context;
//    this._client = client;
//    this._remoteObject = remoteObject;
//    this._disposed = false;

    public static JSHandle create(ExecutionContext context, JSONObject remoteObject) {
        Frame frame = context.getFrame();
        if (StringUtil.equals("node", remoteObject.getString("subtype")) && Objects.nonNull(frame)) {
            return new ElementHandle(context, context.getSession(), remoteObject, frame.getFrameManager());
        }
        return new JSHandle(context, context.getSession(), remoteObject);
    }

    public JSHandle(ExecutionContext context, CDPSession session, JSONObject remoteObject) {
        mContext = context;
        mSession = session;
        mRemoteObject = remoteObject;

    }

    public ExecutionContext getContext() {
        return mContext;
    }

    public Object evaluate(String pageFunction) throws TimeoutException {
        return mContext.evaluate(pageFunction);
    }

    public JSHandle evaluateHandle(String pageFunction) throws TimeoutException {
        return (JSHandle) mContext.evaluateHandle(pageFunction);
    }

    public Object getJsonValue() {
        try {
            if (StringUtil.isEmpty(mRemoteObject.getString("objectId"))) {
                JSONObject json = mSession.doCall(RuntimeDomain.callFunctionOnCommand, new JSONObject() {{
                    put("functionDeclaration", "function() { return this; }");
                    put("objectId", mRemoteObject.getString("objectId"));
                    put("returnByValue", true);
                    put("awaitPromise", true);
                }});
                return Helper.parseValueFromRemoteObject(json.getJSONObject("result").getJSONObject("result"));
            }
        } catch (TimeoutException ignore) {
        }
        return Helper.parseValueFromRemoteObject(mRemoteObject);
    }

    public JSHandle getProperty(String propertyName) throws TimeoutException {
        return getProperties().get(propertyName);

        // fixme 原 puppeteer 这段代码有问题看起来
//    async getProperty(propertyName) {
//    const objectHandle = await this.evaluateHandle((object, propertyName) => {
//      const result = {__proto__: null};
//            result[propertyName] = object[propertyName];
//            return result;
//        }, propertyName); // 少传一个 object 参数啊！！！
//    const properties = await objectHandle.getProperties();
//    const result = properties.get(propertyName) || null;
//        await objectHandle.dispose();
//        return result;
//    }
    }


    public Map<String, JSHandle> getProperties() throws TimeoutException {
        JSONObject json = mSession.doCall(RuntimeDomain.getPropertiesCommand, new JSONObject() {{
            put("objectId", mRemoteObject.getString("objectId"));
            put("ownProperties", true);
        }});
        JSONArray results = json.getJSONObject("result").getJSONArray("result");
        Map<String, JSHandle> properties = new HashMap<>();
        results.forEach(o -> {
            JSONObject property = (JSONObject) o;
            if (property.getBoolean("enumerable")) {
                properties.put(property.getString("name"), create(mContext, property.getJSONObject("value")));
            }
        });
        return properties;
    }

    public JSHandle asElement() {
        return null;
    }

    public void dispose() {
        if (mDisposed) return;

        mDisposed = true;
        Helper.releaseObject(mSession, mRemoteObject.getString("objectId"));
    }

    @Override
    public String toString() {
        if (StringUtil.isEmpty(mRemoteObject.getString("objectId"))) {
            return "JSHandle:" + Helper.parseValueFromRemoteObject(mRemoteObject);
        } else {
            String type = Objects.nonNull(mRemoteObject.getString("subtype")) ?
                    mRemoteObject.getString("subtype") : mRemoteObject.getString("type");
            return "JSHandle@" + type;
        }
    }

    public static class ElementHandle extends JSHandle {

        private FrameManager mFrameManager;


        public ElementHandle(ExecutionContext context, CDPSession session, JSONObject remoteObject, FrameManager frameManager) {
            super(context, session, remoteObject);
            mFrameManager = frameManager;
        }

        @Override
        public ElementHandle asElement() {
            return this;
        }

        public Frame getContentFrame() throws TimeoutException {
            JSONObject nodeInfo = mSession.doCall(DOMDomain.describeNodeCommand, new JSONObject() {{
                put("objectId", mRemoteObject.getString("objectId"));
            }}).getJSONObject("result");

            return mFrameManager.getFrame(nodeInfo.getJSONObject("node").getString("frameId"));
        }

        private void scrollIntoViewIfNeeded() throws TimeoutException {
            String fn = "(async(element, pageJavascriptEnabled) => {\n" +
                    "    if (!element.isConnected)\n" +
                    "        return 'Node is detached from document';\n" +
                    "    if (element.nodeType !== Node.ELEMENT_NODE)\n" +
                    "        return 'Node is not of type HTMLElement';\n" +
                    "    // force-scroll if page's javascript is disabled.\n" +
                    "    if (!pageJavascriptEnabled) {\n" +
                    "        element.scrollIntoView({block: 'center', inline: 'center', behavior: 'instant'});\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "    const visibleRatio = await new Promise(resolve => {\n" +
                    "        const observer = new IntersectionObserver(entries => {\n" +
                    "            resolve(entries[0].intersectionRatio);\n" +
                    "            observer.disconnect();\n" +
                    "        });\n" +
                    "        observer.observe(element);\n" +
                    "    });\n" +
                    "    if (visibleRatio !== 1.0)\n" +
                    "        element.scrollIntoView({block: 'center', inline: 'center', behavior: 'instant'});\n" +
                    "    return false;\n" +
                    "})(%b)";
            Object result = evaluate(String.format(fn, mFrameManager.getPage().isJavascriptEnabled()));
            if (result instanceof String) throw new JuppeteerException((String) result);
        }

        private Map<String, Integer> clickablePoint() throws TimeoutException {
            JSONObject content = mSession.doCall(DOMDomain.getContentQuadsCommand, new JSONObject() {{
                put("objectId", mRemoteObject.getString("objectId"));
            }}).getJSONObject("result");
            JSONObject layoutMetrics = mSession.doCall(PageDomain.getLayoutMetricsCommand).getJSONObject("result");

            if (Objects.isNull(content) || content.getJSONArray("quads").size() == 0) {
                throw new JuppeteerException("Node is either not visible or not an HTMLElement");
            }

            int width = layoutMetrics.getJSONObject("layoutViewport").getInteger("clientWidth");
            int height = layoutMetrics.getJSONObject("layoutViewport").getInteger("clientHeight");

            JSONArray quads = content.getJSONArray("quads");
//            quads.stream()
//                    .map(quad -> fromProtocolQuad(quad)
//                    .map(quad -> intersectQuadWithViewport(quad, width, height)
//                    .filter(quad -> computeQuadArea(quad) > 1)
//                    .toArray();

//                    new ArrayList<String>().stream().map(ss -> {fromProtocolQuad(ss)})
            return new HashMap<>();

        }

        private List<Map<String, Integer>> fromProtocolQuad(JSONArray quad) {
            return new ArrayList<Map<String, Integer>>() {{
                add(new HashMap<String, Integer>() {{
                    put("x", quad.getInteger(0));
                    put("y", quad.getInteger(1));
                }});
                add(new HashMap<String, Integer>() {{
                    put("x", quad.getInteger(2));
                    put("y", quad.getInteger(3));
                }});
                add(new HashMap<String, Integer>() {{
                    put("x", quad.getInteger(4));
                    put("y", quad.getInteger(5));
                }});
                add(new HashMap<String, Integer>() {{
                    put("x", quad.getInteger(6));
                    put("y", quad.getInteger(7));
                }});
            }};
        }

        private Map<String, Integer> intersectQuadWithViewport(Map<String, Integer> quad, int width, int height) {
            return new HashMap<String, Integer>() {{
                put("x", Math.min(Math.max(quad.get("x"), 0), width));
                put("y", Math.min(Math.max(quad.get("y"), 0), height));
            }};
        }

        private int computeQuadArea(Map<String, Integer> quad) {
            // Compute sum of all directed areas of adjacent triangles
            // https://en.wikipedia.org/wiki/Polygon#Simple_polygons
            int area = 0;
//            Set<Map.Entry<String, Integer>> entries = quad.entrySet();
//            for (int i = 0; i < entries.size(); ++i) {
//            }
            // TODO: 2/19/20
            return 0;
//            let area = 0;
//            for (let i = 0; i < quad.length; ++i) {
//    const p1 = quad[i];
//    const p2 = quad[(i + 1) % quad.length];
//                area += (p1.x * p2.y - p2.x * p1.y) / 2;
//            }
//            return Math.abs(area);
        }

        private JSONObject getBoxModel() throws TimeoutException {
            return mSession.doCall(DOMDomain.getBoxModelCommand, new JSONObject() {{
                put("objectId", mRemoteObject.getString("objectId"));
            }}).getJSONObject("result");
        }

        public void hover() throws TimeoutException {
            scrollIntoViewIfNeeded();
            Map<String, Integer> stringIntegerMap = clickablePoint();
//            mFrameManager.getPage().

            // TODO: 2/19/20 mouse move
//            await this._scrollIntoViewIfNeeded();
//    const {x, y} = await this._clickablePoint();
//            await this._page.mouse.move(x, y);
        }

        public void click(JSONObject options) throws TimeoutException {
            scrollIntoViewIfNeeded();
            Map<String, Integer> stringIntegerMap = clickablePoint();
//            await this._page.mouse.click(x, y, options);
        }

        public void tap() throws TimeoutException {
            scrollIntoViewIfNeeded();
            Map<String, Integer> stringIntegerMap = clickablePoint();
//            await this._page.touchscreen.tap(x, y);
        }

        public void focus() throws TimeoutException {
            // FIXME: 2/19/20 没有参数？
            evaluate("(element) => element.focus()");
        }

        public void type(String text, JSONObject options) throws TimeoutException {
            focus();
//            await this._page.keyboard.type(text, options);
        }

        public void press(String key, JSONObject options) throws TimeoutException {
            focus();
//            await this._page.keyboard.press(key, options);
        }

        public Map<String, Integer> boundingBox() throws TimeoutException {
            JSONObject boxModel = getBoxModel();
            if (Objects.isNull(boxModel)) return null;

            JSONArray quad = boxModel.getJSONObject("model").getJSONArray("border");
            int x = Math.min(
                    Math.min(quad.getInteger(0), quad.getInteger(2)),
                    Math.min(quad.getInteger(4), quad.getInteger(6))
            );
            int y = Math.min(
                    Math.min(quad.getInteger(1), quad.getInteger(3)),
                    Math.min(quad.getInteger(5), quad.getInteger(7))
            );
            int width = Math.max(
                    Math.max(quad.getInteger(0), quad.getInteger(2)),
                    Math.max(quad.getInteger(4), quad.getInteger(6))
            ) - x;
            int height = Math.max(
                    Math.max(quad.getInteger(1), quad.getInteger(3)),
                    Math.max(quad.getInteger(5), quad.getInteger(7))
            ) - y;
            return new HashMap<String, Integer>(){{
                put("x", x);
                put("y", y);
                put("width", width);
                put("height", height);
            }};
        }

        public Map<String, Integer> boxModel() throws TimeoutException {
            JSONObject boxModel = getBoxModel();
            if (Objects.isNull(boxModel)) return null;

            JSONObject model = boxModel.getJSONObject("model");
//    const {content, padding, border, margin, width, height} = result.model;
//            return {
//                    content: this._fromProtocolQuad(content),
//                    padding: this._fromProtocolQuad(padding),
//                    border: this._fromProtocolQuad(border),
//                    margin: this._fromProtocolQuad(margin),
//                    width,
//                    height

            return new HashMap<String, Integer>(){{
                put("content", 1);
                put("padding", 1);
                put("border", 1);
                put("margin", 1);
                put("width", model.getInteger("width"));
                put("height", model.getInteger("height"));
            }};
        }


        public ElementHandle $(String selector) throws TimeoutException {
            ElementHandle handle = (ElementHandle) evaluateHandle("");

            if (Objects.nonNull(handle)) {
                return handle;
            }
            // 好奇怪这段代码
//            handle.dispose();
            return null;
        }
    }
}
