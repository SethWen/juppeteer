package com.modorone.juppeteer.component;

import com.alibaba.fastjson.JSONObject;
import com.modorone.juppeteer.cdp.CDPSession;

import java.util.Objects;

/**
 * author: Shawn
 * time  : 2/18/20 11:42 PM
 * desc  :
 * update: Shawn 2/18/20 11:42 PM
 */
public class ExecutionContext {

    private static final String EVALUATION_SCRIPT_URL = "__juppeteer_evaluation_script__";
    private CDPSession mSession;
    private DomWorld mWorld;
    private String mContextId;

    public ExecutionContext(CDPSession session, DomWorld world, JSONObject contextPayload) {
        mSession = session;
        mWorld = world;
        mContextId = contextPayload.getString("id");
    }

    public Frame getFrame() {
        if (Objects.nonNull(mWorld)) {
            return mWorld.getFrame();
        } else {
            return null;
        }
    }

    public void evaluate() {

    }

    private void evaluateInternal() {
        String suffix = "//# sourceURL=" + EVALUATION_SCRIPT_URL;
    }

//    async _evaluateInternal(returnByValue, pageFunction, ...args) {
//    const suffix = `//# sourceURL=${EVALUATION_SCRIPT_URL}`;
//
//        if (helper.isString(pageFunction)) {
//      const contextId = this._contextId;
//      const expression = /** @type {string} */ (pageFunction);
//      const expressionWithSourceUrl = SOURCE_URL_REGEX.test(expression) ? expression : expression + '\n' + suffix;
//      const {exceptionDetails, result: remoteObject} = await this._client.send('Runtime.evaluate', {
//                    expression: expressionWithSourceUrl,
//                    contextId,
//                    returnByValue,
//                    awaitPromise: true,
//                    userGesture: true
//      }).catch(rewriteError);
//            if (exceptionDetails)
//                throw new Error('Evaluation failed: ' + helper.getExceptionMessage(exceptionDetails));
//            return returnByValue ? helper.valueFromRemoteObject(remoteObject) : createJSHandle(this, remoteObject);
//        }
//
//        if (typeof pageFunction !== 'function')
//        throw new Error(`Expected to get |string| or |function| as the first argument, but got "${pageFunction}" instead.`);
//
//        let functionText = pageFunction.toString();
//        try {
//            new Function('(' + functionText + ')');
//        } catch (e1) {
//            // This means we might have a function shorthand. Try another
//            // time prefixing 'function '.
//            if (functionText.startsWith('async '))
//                functionText = 'async function ' + functionText.substring('async '.length);
//            else
//                functionText = 'function ' + functionText;
//            try {
//                new Function('(' + functionText  + ')');
//            } catch (e2) {
//                // We tried hard to serialize, but there's a weird beast here.
//                throw new Error('Passed function is not well-serializable!');
//            }
//        }
//        let callFunctionOnPromise;
//        try {
//            callFunctionOnPromise = this._client.send('Runtime.callFunctionOn', {
//                    functionDeclaration: functionText + '\n' + suffix + '\n',
//                    executionContextId: this._contextId,
//                    arguments: args.map(convertArgument.bind(this)),
//                    returnByValue,
//                    awaitPromise: true,
//                    userGesture: true
//      });
//        } catch (err) {
//            if (err instanceof TypeError && err.message.startsWith('Converting circular structure to JSON'))
//                err.message += ' Are you passing a nested JSHandle?';
//            throw err;
//        }
//    const { exceptionDetails, result: remoteObject } = await callFunctionOnPromise.catch(rewriteError);
//        if (exceptionDetails)
//            throw new Error('Evaluation failed: ' + helper.getExceptionMessage(exceptionDetails));
//        return returnByValue ? helper.valueFromRemoteObject(remoteObject) : createJSHandle(this, remoteObject);
//
//        /**
//         * @param {*} arg
//         * @return {*}
//         * @this {ExecutionContext}
//         */
//        function convertArgument(arg) {
//        if (typeof arg === 'bigint') // eslint-disable-line valid-typeof
//        return { unserializableValue: `${arg.toString()}n` };
//        if (Object.is(arg, -0))
//            return { unserializableValue: '-0' };
//        if (Object.is(arg, Infinity))
//            return { unserializableValue: 'Infinity' };
//        if (Object.is(arg, -Infinity))
//            return { unserializableValue: '-Infinity' };
//        if (Object.is(arg, NaN))
//            return { unserializableValue: 'NaN' };
//      const objectHandle = arg && (arg instanceof JSHandle) ? arg : null;
//        if (objectHandle) {
//            if (objectHandle._context !== this)
//                throw new Error('JSHandles can be evaluated only in the context they were created!');
//            if (objectHandle._disposed)
//                throw new Error('JSHandle is disposed!');
//            if (objectHandle._remoteObject.unserializableValue)
//                return { unserializableValue: objectHandle._remoteObject.unserializableValue };
//            if (!objectHandle._remoteObject.objectId)
//                return { value: objectHandle._remoteObject.value };
//            return { objectId: objectHandle._remoteObject.objectId };
//        }
//        return { value: arg };

//    constructor(client, contextPayload, world) {
//        this._client = client;
//        this._world = world;
//        this._contextId = contextPayload.id;
//    }
}
