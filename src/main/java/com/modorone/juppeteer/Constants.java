package com.modorone.juppeteer;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.HashMap;
import java.util.Map;

/**
 * author: Shawn
 * time  : 1/13/20 2:42 PM
 * desc  :
 * update: Shawn 1/13/20 2:42 PM
 */
public class Constants {

    public static final long INFINITY = -1;
    public static final int DEFAULT_RPC_TIMEOUT = 30000;

    public static final String EVALUATION_SCRIPT_URL = "//# sourceURL=__juppeteer_evaluation_script__";

    //    public static final String executablePath = "/home/shawn/WorkSpace/xinde/marcelo/node_modules/playwright-core/.local-chromium/linux-733125/chrome-linux/chrome";
    public static final String executablePath = "/home/shawn/WorkSpace/xinde/marcelo/node_modules/puppeteer/.local-chromium/linux-722234/chrome-linux/chrome";

    public static String[] DEFAULT_ARGS = new String[]{
            "--disable-background-networking",
            "--enable-features=NetworkService,NetworkServiceInProcess",
            "--disable-background-timer-throttling",
            "--disable-backgrounding-occluded-windows",
            "--disable-breakpad",
            "--disable-client-side-phishing-detection",
            "--disable-component-extensions-with-background-pages",
            "--disable-default-apps",
            "--disable-dev-shm-usage",
            "--disable-extensions",
            // BlinkGenPropertyTrees disabled due to crbug.com/937609
            "--disable-features=TranslateUI,BlinkGenPropertyTrees",
            "--disable-hang-monitor",
            "--disable-ipc-flooding-protection",
            "--disable-popup-blocking",
            "--disable-prompt-on-repost",
            "--disable-renderer-backgrounding",
            "--disable-sync",
            "--force-color-profile=srgb",
            "--metrics-recording-only",
            "--no-first-run",
            "--enable-automation",
            "--password-store=basic",
            "--use-mock-keychain",
    };


    public static String PREDICATE_FUNCTION = "function predicate(selectorOrXPath, isXPath, waitForVisible, waitForHidden) {\n" +
            "    const node = isXPath\n" +
            "        ? document.evaluate(selectorOrXPath, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue\n" +
            "        : document.querySelector(selectorOrXPath);\n" +
            "    if (!node)\n" +
            "        return waitForHidden;\n" +
            "    if (!waitForVisible && !waitForHidden)\n" +
            "        return node;\n" +
            "    const element = (node.nodeType === Node.TEXT_NODE ? node.parentElement : node);\n" +
            "\n" +
            "    const style = window.getComputedStyle(element);\n" +
            "    const isVisible = style && style.visibility !== 'hidden' && hasVisibleBoundingBox();\n" +
            "    const success = (waitForVisible === isVisible || waitForHidden === !isVisible);\n" +
            "    return success ? node : null;\n" +
            "\n" +
            "    function hasVisibleBoundingBox() {\n" +
            "        const rect = element.getBoundingClientRect();\n" +
            "        return !!(rect.top || rect.bottom || rect.width || rect.height);\n" +
            "    }\n" +
            "}";

    public static String WAIT_FUNCTION = "async function waitForPredicatePageFunction(predicateBody, polling, timeout, ...args) {\n" +
            "  const predicate = new Function('...args', predicateBody);\n" +
            "  let timedOut = false;\n" +
            "  if (timeout)\n" +
            "    setTimeout(() => timedOut = true, timeout);\n" +
            "  if (polling === 'raf')\n" +
            "    return await pollRaf();\n" +
            "  if (polling === 'mutation')\n" +
            "    return await pollMutation();\n" +
            "  if (typeof polling === 'number')\n" +
            "    return await pollInterval(polling);\n" +
            "\n" +
            "  /**\n" +
            "   * @return {!Promise<*>}\n" +
            "   */\n" +
            "  function pollMutation() {\n" +
            "    const success = predicate.apply(null, ...args);\n" +
            "    if (success)\n" +
            "      return Promise.resolve(success);\n" +
            "\n" +
            "    let fulfill;\n" +
            "    const result = new Promise(x => fulfill = x);\n" +
            "    const observer = new MutationObserver(mutations => {\n" +
            "      if (timedOut) {\n" +
            "        observer.disconnect();\n" +
            "        fulfill();\n" +
            "      }\n" +
            "      const success = predicate.apply(null, ...args);\n" +
            "      if (success) {\n" +
            "        observer.disconnect();\n" +
            "        fulfill(success);\n" +
            "      }\n" +
            "    });\n" +
            "    observer.observe(document, {\n" +
            "      childList: true,\n" +
            "      subtree: true,\n" +
            "      attributes: true\n" +
            "    });\n" +
            "    return result;\n" +
            "  }\n" +
            "\n" +
            "  /**\n" +
            "   * @return {!Promise<*>}\n" +
            "   */\n" +
            "  function pollRaf() {\n" +
            "    let fulfill;\n" +
            "    const result = new Promise(x => fulfill = x);\n" +
            "    onRaf();\n" +
            "    return result;\n" +
            "\n" +
            "    function onRaf() {\n" +
            "      if (timedOut) {\n" +
            "        fulfill();\n" +
            "        return;\n" +
            "      }\n" +
            "      const success = predicate.apply(null, ...args);\n" +
            "      if (success)\n" +
            "        fulfill(success);\n" +
            "      else\n" +
            "        requestAnimationFrame(onRaf);\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  /**\n" +
            "   * @param {number} pollInterval\n" +
            "   * @return {!Promise<*>}\n" +
            "   */\n" +
            "  function pollInterval(pollInterval) {\n" +
            "    let fulfill;\n" +
            "    const result = new Promise(x => fulfill = x);\n" +
            "    onTimeout();\n" +
            "    return result;\n" +
            "\n" +
            "    function onTimeout() {\n" +
            "      if (timedOut) {\n" +
            "        fulfill();\n" +
            "        return;\n" +
            "      }\n" +
            "      const success = predicate.apply(null, ...args);\n" +
            "      if (success)\n" +
            "        fulfill(success);\n" +
            "      else\n" +
            "        setTimeout(onTimeout, pollInterval);\n" +
            "    }\n" +
            "  }\n" +
            "}";
}
