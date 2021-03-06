package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.cdp.CDPSession;
import com.modorone.juppeteer.cdp.DOMDomain;
import com.modorone.juppeteer.cdp.PageDomain;
import com.modorone.juppeteer.exception.ElementNotFoundException;
import com.modorone.juppeteer.exception.JuppeteerException;

import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class ElementHandle extends JSHandle {

        private FrameManager mFrameManager;
        private Page mPage;


        public ElementHandle(ExecutionContext context, CDPSession session, JSONObject remoteObject, FrameManager frameManager) {
            super(context, session, remoteObject);
            mFrameManager = frameManager;
            mPage = mFrameManager.getPage();
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
            String fn = "async(element, pageJavascriptEnabled) => {\n" +
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
                    "}";
            Object result = evaluateFunction4Value(fn, mFrameManager.getPage().isJavascriptEnabled());
            if (result instanceof String) throw new JuppeteerException((String) result);
        }

        private Map<String, Integer> clickablePoint() throws TimeoutException {
            // {"quads":[[1196.5,294,1220.5,294,1220.5,338,1196.5,338]]}
            JSONObject content = mSession.doCall(DOMDomain.getContentQuadsCommand, new JSONObject() {{
                put("objectId", mRemoteObject.getString("objectId"));
            }}).getJSONObject("result");

            // {"layoutViewport":{"pageX":0,"pageY":0,"clientWidth":1912,"clientHeight":926}
            JSONObject layoutMetrics = mSession.doCall(PageDomain.getLayoutMetricsCommand).getJSONObject("result");

            if (Objects.isNull(content) || content.getJSONArray("quads").size() == 0) {
                throw new JuppeteerException("Node is either not visible or not an HTMLElement");
            }

            float width = layoutMetrics.getJSONObject("layoutViewport").getFloat("clientWidth");
            float height = layoutMetrics.getJSONObject("layoutViewport").getFloat("clientHeight");

            List<List<HashMap<String, Float>>> quads = content.getJSONArray("quads").stream()
                    .map(quad -> fromProtocolQuad((JSONArray) quad))
                    .map(quad -> intersectQuadWithViewport(quad, width, height))
                    .filter(quad -> computeQuadArea(quad) > 1)
                    .collect(Collectors.toList());

            if (quads.size() == 0) throw new JuppeteerException("Node is either not visible or not an HTMLElement");

            List<HashMap<String, Float>> quad = quads.get(0);
            float x = 0, y = 0;
            for (HashMap<String, Float> point : quad) {
                x += point.get("x");
                y += point.get("y");
            }
            float finalX = x, finalY = y;
            return new HashMap<String, Integer>() {{
                put("x", (int) (finalX / 4));
                put("y", (int) (finalY / 4));
            }};
        }

        private Float computeQuadArea(List<HashMap<String, Float>> quad) {
            // Compute sum of all directed areas of adjacent triangles
            // https://en.wikipedia.org/wiki/Polygon#Simple_polygons
            float area = 0;
            for (int i = 0; i < quad.size(); i++) {
                HashMap<String, Float> p1 = quad.get(i);
                HashMap<String, Float> p2 = quad.get((i + 1) % quad.size());
                area += (p1.get("x") * p2.get("y") - p2.get("x") * p1.get("y")) / 2;
            }
            return Math.abs(area);
        }

        private List<HashMap<String, Float>> intersectQuadWithViewport(List<Map<String, Float>> quad, float width, float height) {
            return quad.stream().map(point -> new HashMap<String, Float>() {{
                put("x", Math.min(Math.max(point.get("x"), 0), width));
                put("y", Math.min(Math.max(point.get("y"), 0), height));
            }}).collect(Collectors.toList());
        }

        private List<Map<String, Float>> fromProtocolQuad(JSONArray quad) {
            return new ArrayList<Map<String, Float>>() {{
                add(new HashMap<String, Float>() {{
                    put("x", quad.getFloat(0));
                    put("y", quad.getFloat(1));
                }});
                add(new HashMap<String, Float>() {{
                    put("x", quad.getFloat(2));
                    put("y", quad.getFloat(3));
                }});
                add(new HashMap<String, Float>() {{
                    put("x", quad.getFloat(4));
                    put("y", quad.getFloat(5));
                }});
                add(new HashMap<String, Float>() {{
                    put("x", quad.getFloat(6));
                    put("y", quad.getFloat(7));
                }});
            }};
        }

        private JSONObject getBoxModel() throws TimeoutException {
            return mSession.doCall(DOMDomain.getBoxModelCommand, new JSONObject() {{
                put("objectId", mRemoteObject.getString("objectId"));
            }}).getJSONObject("result");
        }

        public void hover() throws TimeoutException {
            scrollIntoViewIfNeeded();
            Map<String, Integer> point = clickablePoint();
            mPage.getMouse().move(point.get("x"), point.get("y"), new JSONObject());
        }

        public void click(JSONObject options) throws TimeoutException {
            scrollIntoViewIfNeeded();
            Map<String, Integer> point = clickablePoint();
            mPage.getMouse().click(point.get("x"), point.get("y"), new JSONObject());
        }

        public void focus() throws TimeoutException {
            evaluateFunction4Value("(element) => { element.focus(); return true; }");
        }

        public void type(String text, JSONObject options) throws TimeoutException {
            focus();
            mPage.getKeyboard().type(text, options);
        }

        public void tap() throws TimeoutException {
            scrollIntoViewIfNeeded();
            Map<String, Integer> point = clickablePoint();
            mPage.getTouchscreen().tap(point.get("x"), point.get("y"));
        }

        public void press(String key, JSONObject options) throws TimeoutException {
            focus();
            mPage.getKeyboard().press(key, options);
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
            return new HashMap<String, Integer>() {{
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

            return new HashMap<String, Integer>() {{
                put("content", 1);
                put("padding", 1);
                put("border", 1);
                put("margin", 1);
                put("width", model.getInteger("width"));
                put("height", model.getInteger("height"));
            }};
        }


        public ElementHandle $(String selector) throws TimeoutException {
            JSHandle handle = evaluateFunction4Handle(
                    "(element, selector) => element.querySelector(selector)", selector);

            JSHandle element = handle.asElement();
            if (Objects.nonNull(element)) return (ElementHandle) element;

            handle.dispose();
            return null;
        }

        public List<ElementHandle> $$(String selector) throws TimeoutException {
            JSHandle arrayHandle = evaluateFunction4Handle(
                    "(element, selector) => element.querySelectorAll(selector)", selector);
            Map<String, JSHandle> properties = arrayHandle.getProperties();
            arrayHandle.dispose();
            List<ElementHandle> handles = new ArrayList<>();
            for (JSHandle value : properties.values()) {
                ElementHandle elementHandle = (ElementHandle) value.asElement();
                if (Objects.nonNull(elementHandle)) handles.add(elementHandle);
            }
            return handles;
        }

        public Object $eval(String selector, String pageFunction, Object... args) throws TimeoutException {
            ElementHandle elementHandle = $(selector);
            if (Objects.isNull(elementHandle))
                throw new ElementNotFoundException("failed to find element matching selector: " + selector);


            Object result = elementHandle.evaluateFunction4Value(pageFunction, args);
            elementHandle.dispose();
            return result;
        }

        public Object $$eval(String selector, String pageFunction, Object... args) throws TimeoutException {
            JSHandle arrayHandle = evaluateFunction4Handle(
                    "(element, selector) => Array.from(element.querySelectorAll(selector))", selector);

            Object o = arrayHandle.evaluateFunction4Value(pageFunction, args);
            arrayHandle.dispose();
            return o;
        }

        public List<ElementHandle> $x(String expression) throws TimeoutException {
            JSHandle arrayHandle = evaluateFunction4Handle(
                    "(element, expression) => {\n" +
                            "    const document = element.ownerDocument || element;\n" +
                            "    const iterator = document.evaluate(expression, element, null, XPathResult.ORDERED_NODE_ITERATOR_TYPE);\n" +
                            "    const array = [];\n" +
                            "    let item;\n" +
                            "    while ((item = iterator.iterateNext()))\n" +
                            "        array.push(item);\n" +
                            "    return array;\n" +
                            "}", expression);
            Map<String, JSHandle> properties = arrayHandle.getProperties();
            arrayHandle.dispose();
            List<ElementHandle> handles = new ArrayList<>();
            for (JSHandle value : properties.values()) {
                ElementHandle elementHandle = (ElementHandle) value.asElement();
                if (Objects.nonNull(elementHandle)) handles.add(elementHandle);
            }
            return handles;
        }
    }