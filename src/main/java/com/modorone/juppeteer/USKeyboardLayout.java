package com.modorone.juppeteer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * author: Shawn
 * time  : 2/18/20 12:41 AM
 * desc  :
 * update: Shawn 2/18/20 12:41 AM
 */
public class USKeyboardLayout {

    private static Map<String, JSONObject> keyMap = new HashMap<>();

    public static JSONObject getKey(String keyName) {
        if (keyMap.size() == 0) {
            String LAYOUT = "{\n" +
                    "  '0': {'keyCode': 48, 'key': '0', 'code': 'Digit0'},\n" +
                    "  '1': {'keyCode': 49, 'key': '1', 'code': 'Digit1'},\n" +
                    "  '2': {'keyCode': 50, 'key': '2', 'code': 'Digit2'},\n" +
                    "  '3': {'keyCode': 51, 'key': '3', 'code': 'Digit3'},\n" +
                    "  '4': {'keyCode': 52, 'key': '4', 'code': 'Digit4'},\n" +
                    "  '5': {'keyCode': 53, 'key': '5', 'code': 'Digit5'},\n" +
                    "  '6': {'keyCode': 54, 'key': '6', 'code': 'Digit6'},\n" +
                    "  '7': {'keyCode': 55, 'key': '7', 'code': 'Digit7'},\n" +
                    "  '8': {'keyCode': 56, 'key': '8', 'code': 'Digit8'},\n" +
                    "  '9': {'keyCode': 57, 'key': '9', 'code': 'Digit9'},\n" +
                    "  'Power': {'key': 'Power', 'code': 'Power'},\n" +
                    "  'Eject': {'key': 'Eject', 'code': 'Eject'},\n" +
                    "  'Abort': {'keyCode': 3, 'code': 'Abort', 'key': 'Cancel'},\n" +
                    "  'Help': {'keyCode': 6, 'code': 'Help', 'key': 'Help'},\n" +
                    "  'Backspace': {'keyCode': 8, 'code': 'Backspace', 'key': 'Backspace'},\n" +
                    "  'Tab': {'keyCode': 9, 'code': 'Tab', 'key': 'Tab'},\n" +
                    "  'Numpad5': {'keyCode': 12, 'shiftKeyCode': 101, 'key': 'Clear', 'code': 'Numpad5', 'shiftKey': '5', 'location': 3},\n" +
                    "  'NumpadEnter': {'keyCode': 13, 'code': 'NumpadEnter', 'key': 'Enter', 'text': '\\r', 'location': 3},\n" +
                    "  'Enter': {'keyCode': 13, 'code': 'Enter', 'key': 'Enter', 'text': '\\r'},\n" +
                    "  '\\r': {'keyCode': 13, 'code': 'Enter', 'key': 'Enter', 'text': '\\r'},\n" +
                    "  '\\n': {'keyCode': 13, 'code': 'Enter', 'key': 'Enter', 'text': '\\r'},\n" +
                    "  'ShiftLeft': {'keyCode': 16, 'code': 'ShiftLeft', 'key': 'Shift', 'location': 1},\n" +
                    "  'ShiftRight': {'keyCode': 16, 'code': 'ShiftRight', 'key': 'Shift', 'location': 2},\n" +
                    "  'ControlLeft': {'keyCode': 17, 'code': 'ControlLeft', 'key': 'Control', 'location': 1},\n" +
                    "  'ControlRight': {'keyCode': 17, 'code': 'ControlRight', 'key': 'Control', 'location': 2},\n" +
                    "  'AltLeft': {'keyCode': 18, 'code': 'AltLeft', 'key': 'Alt', 'location': 1},\n" +
                    "  'AltRight': {'keyCode': 18, 'code': 'AltRight', 'key': 'Alt', 'location': 2},\n" +
                    "  'Pause': {'keyCode': 19, 'code': 'Pause', 'key': 'Pause'},\n" +
                    "  'CapsLock': {'keyCode': 20, 'code': 'CapsLock', 'key': 'CapsLock'},\n" +
                    "  'Escape': {'keyCode': 27, 'code': 'Escape', 'key': 'Escape'},\n" +
                    "  'Convert': {'keyCode': 28, 'code': 'Convert', 'key': 'Convert'},\n" +
                    "  'NonConvert': {'keyCode': 29, 'code': 'NonConvert', 'key': 'NonConvert'},\n" +
                    "  'Space': {'keyCode': 32, 'code': 'Space', 'key': ' '},\n" +
                    "  'Numpad9': {'keyCode': 33, 'shiftKeyCode': 105, 'key': 'PageUp', 'code': 'Numpad9', 'shiftKey': '9', 'location': 3},\n" +
                    "  'PageUp': {'keyCode': 33, 'code': 'PageUp', 'key': 'PageUp'},\n" +
                    "  'Numpad3': {'keyCode': 34, 'shiftKeyCode': 99, 'key': 'PageDown', 'code': 'Numpad3', 'shiftKey': '3', 'location': 3},\n" +
                    "  'PageDown': {'keyCode': 34, 'code': 'PageDown', 'key': 'PageDown'},\n" +
                    "  'End': {'keyCode': 35, 'code': 'End', 'key': 'End'},\n" +
                    "  'Numpad1': {'keyCode': 35, 'shiftKeyCode': 97, 'key': 'End', 'code': 'Numpad1', 'shiftKey': '1', 'location': 3},\n" +
                    "  'Home': {'keyCode': 36, 'code': 'Home', 'key': 'Home'},\n" +
                    "  'Numpad7': {'keyCode': 36, 'shiftKeyCode': 103, 'key': 'Home', 'code': 'Numpad7', 'shiftKey': '7', 'location': 3},\n" +
                    "  'ArrowLeft': {'keyCode': 37, 'code': 'ArrowLeft', 'key': 'ArrowLeft'},\n" +
                    "  'Numpad4': {'keyCode': 37, 'shiftKeyCode': 100, 'key': 'ArrowLeft', 'code': 'Numpad4', 'shiftKey': '4', 'location': 3},\n" +
                    "  'Numpad8': {'keyCode': 38, 'shiftKeyCode': 104, 'key': 'ArrowUp', 'code': 'Numpad8', 'shiftKey': '8', 'location': 3},\n" +
                    "  'ArrowUp': {'keyCode': 38, 'code': 'ArrowUp', 'key': 'ArrowUp'},\n" +
                    "  'ArrowRight': {'keyCode': 39, 'code': 'ArrowRight', 'key': 'ArrowRight'},\n" +
                    "  'Numpad6': {'keyCode': 39, 'shiftKeyCode': 102, 'key': 'ArrowRight', 'code': 'Numpad6', 'shiftKey': '6', 'location': 3},\n" +
                    "  'Numpad2': {'keyCode': 40, 'shiftKeyCode': 98, 'key': 'ArrowDown', 'code': 'Numpad2', 'shiftKey': '2', 'location': 3},\n" +
                    "  'ArrowDown': {'keyCode': 40, 'code': 'ArrowDown', 'key': 'ArrowDown'},\n" +
                    "  'Select': {'keyCode': 41, 'code': 'Select', 'key': 'Select'},\n" +
                    "  'Open': {'keyCode': 43, 'code': 'Open', 'key': 'Execute'},\n" +
                    "  'PrintScreen': {'keyCode': 44, 'code': 'PrintScreen', 'key': 'PrintScreen'},\n" +
                    "  'Insert': {'keyCode': 45, 'code': 'Insert', 'key': 'Insert'},\n" +
                    "  'Numpad0': {'keyCode': 45, 'shiftKeyCode': 96, 'key': 'Insert', 'code': 'Numpad0', 'shiftKey': '0', 'location': 3},\n" +
                    "  'Delete': {'keyCode': 46, 'code': 'Delete', 'key': 'Delete'},\n" +
                    "  'NumpadDecimal': {'keyCode': 46, 'shiftKeyCode': 110, 'code': 'NumpadDecimal', 'key': '\\u0000', 'shiftKey': '.', 'location': 3},\n" +
                    "  'Digit0': {'keyCode': 48, 'code': 'Digit0', 'shiftKey': ')', 'key': '0'},\n" +
                    "  'Digit1': {'keyCode': 49, 'code': 'Digit1', 'shiftKey': '!', 'key': '1'},\n" +
                    "  'Digit2': {'keyCode': 50, 'code': 'Digit2', 'shiftKey': '@', 'key': '2'},\n" +
                    "  'Digit3': {'keyCode': 51, 'code': 'Digit3', 'shiftKey': '#', 'key': '3'},\n" +
                    "  'Digit4': {'keyCode': 52, 'code': 'Digit4', 'shiftKey': '$', 'key': '4'},\n" +
                    "  'Digit5': {'keyCode': 53, 'code': 'Digit5', 'shiftKey': '%', 'key': '5'},\n" +
                    "  'Digit6': {'keyCode': 54, 'code': 'Digit6', 'shiftKey': '^', 'key': '6'},\n" +
                    "  'Digit7': {'keyCode': 55, 'code': 'Digit7', 'shiftKey': '&', 'key': '7'},\n" +
                    "  'Digit8': {'keyCode': 56, 'code': 'Digit8', 'shiftKey': '*', 'key': '8'},\n" +
                    "  'Digit9': {'keyCode': 57, 'code': 'Digit9', 'shiftKey': '(', 'key': '9'},\n" +
                    "  'KeyA': {'keyCode': 65, 'code': 'KeyA', 'shiftKey': 'A', 'key': 'a'},\n" +
                    "  'KeyB': {'keyCode': 66, 'code': 'KeyB', 'shiftKey': 'B', 'key': 'b'},\n" +
                    "  'KeyC': {'keyCode': 67, 'code': 'KeyC', 'shiftKey': 'C', 'key': 'c'},\n" +
                    "  'KeyD': {'keyCode': 68, 'code': 'KeyD', 'shiftKey': 'D', 'key': 'd'},\n" +
                    "  'KeyE': {'keyCode': 69, 'code': 'KeyE', 'shiftKey': 'E', 'key': 'e'},\n" +
                    "  'KeyF': {'keyCode': 70, 'code': 'KeyF', 'shiftKey': 'F', 'key': 'f'},\n" +
                    "  'KeyG': {'keyCode': 71, 'code': 'KeyG', 'shiftKey': 'G', 'key': 'g'},\n" +
                    "  'KeyH': {'keyCode': 72, 'code': 'KeyH', 'shiftKey': 'H', 'key': 'h'},\n" +
                    "  'KeyI': {'keyCode': 73, 'code': 'KeyI', 'shiftKey': 'I', 'key': 'i'},\n" +
                    "  'KeyJ': {'keyCode': 74, 'code': 'KeyJ', 'shiftKey': 'J', 'key': 'j'},\n" +
                    "  'KeyK': {'keyCode': 75, 'code': 'KeyK', 'shiftKey': 'K', 'key': 'k'},\n" +
                    "  'KeyL': {'keyCode': 76, 'code': 'KeyL', 'shiftKey': 'L', 'key': 'l'},\n" +
                    "  'KeyM': {'keyCode': 77, 'code': 'KeyM', 'shiftKey': 'M', 'key': 'm'},\n" +
                    "  'KeyN': {'keyCode': 78, 'code': 'KeyN', 'shiftKey': 'N', 'key': 'n'},\n" +
                    "  'KeyO': {'keyCode': 79, 'code': 'KeyO', 'shiftKey': 'O', 'key': 'o'},\n" +
                    "  'KeyP': {'keyCode': 80, 'code': 'KeyP', 'shiftKey': 'P', 'key': 'p'},\n" +
                    "  'KeyQ': {'keyCode': 81, 'code': 'KeyQ', 'shiftKey': 'Q', 'key': 'q'},\n" +
                    "  'KeyR': {'keyCode': 82, 'code': 'KeyR', 'shiftKey': 'R', 'key': 'r'},\n" +
                    "  'KeyS': {'keyCode': 83, 'code': 'KeyS', 'shiftKey': 'S', 'key': 's'},\n" +
                    "  'KeyT': {'keyCode': 84, 'code': 'KeyT', 'shiftKey': 'T', 'key': 't'},\n" +
                    "  'KeyU': {'keyCode': 85, 'code': 'KeyU', 'shiftKey': 'U', 'key': 'u'},\n" +
                    "  'KeyV': {'keyCode': 86, 'code': 'KeyV', 'shiftKey': 'V', 'key': 'v'},\n" +
                    "  'KeyW': {'keyCode': 87, 'code': 'KeyW', 'shiftKey': 'W', 'key': 'w'},\n" +
                    "  'KeyX': {'keyCode': 88, 'code': 'KeyX', 'shiftKey': 'X', 'key': 'x'},\n" +
                    "  'KeyY': {'keyCode': 89, 'code': 'KeyY', 'shiftKey': 'Y', 'key': 'y'},\n" +
                    "  'KeyZ': {'keyCode': 90, 'code': 'KeyZ', 'shiftKey': 'Z', 'key': 'z'},\n" +
                    "  'MetaLeft': {'keyCode': 91, 'code': 'MetaLeft', 'key': 'Meta', 'location': 1},\n" +
                    "  'MetaRight': {'keyCode': 92, 'code': 'MetaRight', 'key': 'Meta', 'location': 2},\n" +
                    "  'ContextMenu': {'keyCode': 93, 'code': 'ContextMenu', 'key': 'ContextMenu'},\n" +
                    "  'NumpadMultiply': {'keyCode': 106, 'code': 'NumpadMultiply', 'key': '*', 'location': 3},\n" +
                    "  'NumpadAdd': {'keyCode': 107, 'code': 'NumpadAdd', 'key': '+', 'location': 3},\n" +
                    "  'NumpadSubtract': {'keyCode': 109, 'code': 'NumpadSubtract', 'key': '-', 'location': 3},\n" +
                    "  'NumpadDivide': {'keyCode': 111, 'code': 'NumpadDivide', 'key': '/', 'location': 3},\n" +
                    "  'F1': {'keyCode': 112, 'code': 'F1', 'key': 'F1'},\n" +
                    "  'F2': {'keyCode': 113, 'code': 'F2', 'key': 'F2'},\n" +
                    "  'F3': {'keyCode': 114, 'code': 'F3', 'key': 'F3'},\n" +
                    "  'F4': {'keyCode': 115, 'code': 'F4', 'key': 'F4'},\n" +
                    "  'F5': {'keyCode': 116, 'code': 'F5', 'key': 'F5'},\n" +
                    "  'F6': {'keyCode': 117, 'code': 'F6', 'key': 'F6'},\n" +
                    "  'F7': {'keyCode': 118, 'code': 'F7', 'key': 'F7'},\n" +
                    "  'F8': {'keyCode': 119, 'code': 'F8', 'key': 'F8'},\n" +
                    "  'F9': {'keyCode': 120, 'code': 'F9', 'key': 'F9'},\n" +
                    "  'F10': {'keyCode': 121, 'code': 'F10', 'key': 'F10'},\n" +
                    "  'F11': {'keyCode': 122, 'code': 'F11', 'key': 'F11'},\n" +
                    "  'F12': {'keyCode': 123, 'code': 'F12', 'key': 'F12'},\n" +
                    "  'F13': {'keyCode': 124, 'code': 'F13', 'key': 'F13'},\n" +
                    "  'F14': {'keyCode': 125, 'code': 'F14', 'key': 'F14'},\n" +
                    "  'F15': {'keyCode': 126, 'code': 'F15', 'key': 'F15'},\n" +
                    "  'F16': {'keyCode': 127, 'code': 'F16', 'key': 'F16'},\n" +
                    "  'F17': {'keyCode': 128, 'code': 'F17', 'key': 'F17'},\n" +
                    "  'F18': {'keyCode': 129, 'code': 'F18', 'key': 'F18'},\n" +
                    "  'F19': {'keyCode': 130, 'code': 'F19', 'key': 'F19'},\n" +
                    "  'F20': {'keyCode': 131, 'code': 'F20', 'key': 'F20'},\n" +
                    "  'F21': {'keyCode': 132, 'code': 'F21', 'key': 'F21'},\n" +
                    "  'F22': {'keyCode': 133, 'code': 'F22', 'key': 'F22'},\n" +
                    "  'F23': {'keyCode': 134, 'code': 'F23', 'key': 'F23'},\n" +
                    "  'F24': {'keyCode': 135, 'code': 'F24', 'key': 'F24'},\n" +
                    "  'NumLock': {'keyCode': 144, 'code': 'NumLock', 'key': 'NumLock'},\n" +
                    "  'ScrollLock': {'keyCode': 145, 'code': 'ScrollLock', 'key': 'ScrollLock'},\n" +
                    "  'AudioVolumeMute': {'keyCode': 173, 'code': 'AudioVolumeMute', 'key': 'AudioVolumeMute'},\n" +
                    "  'AudioVolumeDown': {'keyCode': 174, 'code': 'AudioVolumeDown', 'key': 'AudioVolumeDown'},\n" +
                    "  'AudioVolumeUp': {'keyCode': 175, 'code': 'AudioVolumeUp', 'key': 'AudioVolumeUp'},\n" +
                    "  'MediaTrackNext': {'keyCode': 176, 'code': 'MediaTrackNext', 'key': 'MediaTrackNext'},\n" +
                    "  'MediaTrackPrevious': {'keyCode': 177, 'code': 'MediaTrackPrevious', 'key': 'MediaTrackPrevious'},\n" +
                    "  'MediaStop': {'keyCode': 178, 'code': 'MediaStop', 'key': 'MediaStop'},\n" +
                    "  'MediaPlayPause': {'keyCode': 179, 'code': 'MediaPlayPause', 'key': 'MediaPlayPause'},\n" +
                    "  'Semicolon': {'keyCode': 186, 'code': 'Semicolon', 'shiftKey': ':', 'key': ';'},\n" +
                    "  'Equal': {'keyCode': 187, 'code': 'Equal', 'shiftKey': '+', 'key': '='},\n" +
                    "  'NumpadEqual': {'keyCode': 187, 'code': 'NumpadEqual', 'key': '=', 'location': 3},\n" +
                    "  'Comma': {'keyCode': 188, 'code': 'Comma', 'shiftKey': '<', 'key': ','},\n" +
                    "  'Minus': {'keyCode': 189, 'code': 'Minus', 'shiftKey': '_', 'key': '-'},\n" +
                    "  'Period': {'keyCode': 190, 'code': 'Period', 'shiftKey': '>', 'key': '.'},\n" +
                    "  'Slash': {'keyCode': 191, 'code': 'Slash', 'shiftKey': '?', 'key': '/'},\n" +
                    "  'Backquote': {'keyCode': 192, 'code': 'Backquote', 'shiftKey': '~', 'key': '`'},\n" +
                    "  'BracketLeft': {'keyCode': 219, 'code': 'BracketLeft', 'shiftKey': '{', 'key': '['},\n" +
                    "  'Backslash': {'keyCode': 220, 'code': 'Backslash', 'shiftKey': '|', 'key': '\\\\'},\n" +
                    "  'BracketRight': {'keyCode': 221, 'code': 'BracketRight', 'shiftKey': '}', 'key': ']'},\n" +
                    "  'Quote': {'keyCode': 222, 'code': 'Quote', 'shiftKey': '\"', 'key': '\\''},\n" +
                    "  'AltGraph': {'keyCode': 225, 'code': 'AltGraph', 'key': 'AltGraph'},\n" +
                    "  'Props': {'keyCode': 247, 'code': 'Props', 'key': 'CrSel'},\n" +
                    "  'Cancel': {'keyCode': 3, 'key': 'Cancel', 'code': 'Abort'},\n" +
                    "  'Clear': {'keyCode': 12, 'key': 'Clear', 'code': 'Numpad5', 'location': 3},\n" +
                    "  'Shift': {'keyCode': 16, 'key': 'Shift', 'code': 'ShiftLeft', 'location': 1},\n" +
                    "  'Control': {'keyCode': 17, 'key': 'Control', 'code': 'ControlLeft', 'location': 1},\n" +
                    "  'Alt': {'keyCode': 18, 'key': 'Alt', 'code': 'AltLeft', 'location': 1},\n" +
                    "  'Accept': {'keyCode': 30, 'key': 'Accept'},\n" +
                    "  'ModeChange': {'keyCode': 31, 'key': 'ModeChange'},\n" +
                    "  ' ': {'keyCode': 32, 'key': ' ', 'code': 'Space'},\n" +
                    "  'Print': {'keyCode': 42, 'key': 'Print'},\n" +
                    "  'Execute': {'keyCode': 43, 'key': 'Execute', 'code': 'Open'},\n" +
                    "  '\\u0000': {'keyCode': 46, 'key': '\\u0000', 'code': 'NumpadDecimal', 'location': 3},\n" +
                    "  'a': {'keyCode': 65, 'key': 'a', 'code': 'KeyA'},\n" +
                    "  'b': {'keyCode': 66, 'key': 'b', 'code': 'KeyB'},\n" +
                    "  'c': {'keyCode': 67, 'key': 'c', 'code': 'KeyC'},\n" +
                    "  'd': {'keyCode': 68, 'key': 'd', 'code': 'KeyD'},\n" +
                    "  'e': {'keyCode': 69, 'key': 'e', 'code': 'KeyE'},\n" +
                    "  'f': {'keyCode': 70, 'key': 'f', 'code': 'KeyF'},\n" +
                    "  'g': {'keyCode': 71, 'key': 'g', 'code': 'KeyG'},\n" +
                    "  'h': {'keyCode': 72, 'key': 'h', 'code': 'KeyH'},\n" +
                    "  'i': {'keyCode': 73, 'key': 'i', 'code': 'KeyI'},\n" +
                    "  'j': {'keyCode': 74, 'key': 'j', 'code': 'KeyJ'},\n" +
                    "  'k': {'keyCode': 75, 'key': 'k', 'code': 'KeyK'},\n" +
                    "  'l': {'keyCode': 76, 'key': 'l', 'code': 'KeyL'},\n" +
                    "  'm': {'keyCode': 77, 'key': 'm', 'code': 'KeyM'},\n" +
                    "  'n': {'keyCode': 78, 'key': 'n', 'code': 'KeyN'},\n" +
                    "  'o': {'keyCode': 79, 'key': 'o', 'code': 'KeyO'},\n" +
                    "  'p': {'keyCode': 80, 'key': 'p', 'code': 'KeyP'},\n" +
                    "  'q': {'keyCode': 81, 'key': 'q', 'code': 'KeyQ'},\n" +
                    "  'r': {'keyCode': 82, 'key': 'r', 'code': 'KeyR'},\n" +
                    "  's': {'keyCode': 83, 'key': 's', 'code': 'KeyS'},\n" +
                    "  't': {'keyCode': 84, 'key': 't', 'code': 'KeyT'},\n" +
                    "  'u': {'keyCode': 85, 'key': 'u', 'code': 'KeyU'},\n" +
                    "  'v': {'keyCode': 86, 'key': 'v', 'code': 'KeyV'},\n" +
                    "  'w': {'keyCode': 87, 'key': 'w', 'code': 'KeyW'},\n" +
                    "  'x': {'keyCode': 88, 'key': 'x', 'code': 'KeyX'},\n" +
                    "  'y': {'keyCode': 89, 'key': 'y', 'code': 'KeyY'},\n" +
                    "  'z': {'keyCode': 90, 'key': 'z', 'code': 'KeyZ'},\n" +
                    "  'Meta': {'keyCode': 91, 'key': 'Meta', 'code': 'MetaLeft', 'location': 1},\n" +
                    "  '*': {'keyCode': 106, 'key': '*', 'code': 'NumpadMultiply', 'location': 3},\n" +
                    "  '+': {'keyCode': 107, 'key': '+', 'code': 'NumpadAdd', 'location': 3},\n" +
                    "  '-': {'keyCode': 109, 'key': '-', 'code': 'NumpadSubtract', 'location': 3},\n" +
                    "  '/': {'keyCode': 111, 'key': '/', 'code': 'NumpadDivide', 'location': 3},\n" +
                    "  ';': {'keyCode': 186, 'key': ';', 'code': 'Semicolon'},\n" +
                    "  '=': {'keyCode': 187, 'key': '=', 'code': 'Equal'},\n" +
                    "  ',': {'keyCode': 188, 'key': ',', 'code': 'Comma'},\n" +
                    "  '.': {'keyCode': 190, 'key': '.', 'code': 'Period'},\n" +
                    "  '`': {'keyCode': 192, 'key': '`', 'code': 'Backquote'},\n" +
                    "  '[': {'keyCode': 219, 'key': '[', 'code': 'BracketLeft'},\n" +
                    "  '\\\\': {'keyCode': 220, 'key': '\\\\', 'code': 'Backslash'},\n" +
                    "  ']': {'keyCode': 221, 'key': ']', 'code': 'BracketRight'},\n" +
                    "  '\\'': {'keyCode': 222, 'key': '\\'', 'code': 'Quote'},\n" +
                    "  'Attn': {'keyCode': 246, 'key': 'Attn'},\n" +
                    "  'CrSel': {'keyCode': 247, 'key': 'CrSel', 'code': 'Props'},\n" +
                    "  'ExSel': {'keyCode': 248, 'key': 'ExSel'},\n" +
                    "  'EraseEof': {'keyCode': 249, 'key': 'EraseEof'},\n" +
                    "  'Play': {'keyCode': 250, 'key': 'Play'},\n" +
                    "  'ZoomOut': {'keyCode': 251, 'key': 'ZoomOut'},\n" +
                    "  ')': {'keyCode': 48, 'key': ')', 'code': 'Digit0'},\n" +
                    "  '!': {'keyCode': 49, 'key': '!', 'code': 'Digit1'},\n" +
                    "  '@': {'keyCode': 50, 'key': '@', 'code': 'Digit2'},\n" +
                    "  '#': {'keyCode': 51, 'key': '#', 'code': 'Digit3'},\n" +
                    "  '$': {'keyCode': 52, 'key': '$', 'code': 'Digit4'},\n" +
                    "  '%': {'keyCode': 53, 'key': '%', 'code': 'Digit5'},\n" +
                    "  '^': {'keyCode': 54, 'key': '^', 'code': 'Digit6'},\n" +
                    "  '&': {'keyCode': 55, 'key': '&', 'code': 'Digit7'},\n" +
                    "  '(': {'keyCode': 57, 'key': '(', 'code': 'Digit9'},\n" +
                    "  'A': {'keyCode': 65, 'key': 'A', 'code': 'KeyA'},\n" +
                    "  'B': {'keyCode': 66, 'key': 'B', 'code': 'KeyB'},\n" +
                    "  'C': {'keyCode': 67, 'key': 'C', 'code': 'KeyC'},\n" +
                    "  'D': {'keyCode': 68, 'key': 'D', 'code': 'KeyD'},\n" +
                    "  'E': {'keyCode': 69, 'key': 'E', 'code': 'KeyE'},\n" +
                    "  'F': {'keyCode': 70, 'key': 'F', 'code': 'KeyF'},\n" +
                    "  'G': {'keyCode': 71, 'key': 'G', 'code': 'KeyG'},\n" +
                    "  'H': {'keyCode': 72, 'key': 'H', 'code': 'KeyH'},\n" +
                    "  'I': {'keyCode': 73, 'key': 'I', 'code': 'KeyI'},\n" +
                    "  'J': {'keyCode': 74, 'key': 'J', 'code': 'KeyJ'},\n" +
                    "  'K': {'keyCode': 75, 'key': 'K', 'code': 'KeyK'},\n" +
                    "  'L': {'keyCode': 76, 'key': 'L', 'code': 'KeyL'},\n" +
                    "  'M': {'keyCode': 77, 'key': 'M', 'code': 'KeyM'},\n" +
                    "  'N': {'keyCode': 78, 'key': 'N', 'code': 'KeyN'},\n" +
                    "  'O': {'keyCode': 79, 'key': 'O', 'code': 'KeyO'},\n" +
                    "  'P': {'keyCode': 80, 'key': 'P', 'code': 'KeyP'},\n" +
                    "  'Q': {'keyCode': 81, 'key': 'Q', 'code': 'KeyQ'},\n" +
                    "  'R': {'keyCode': 82, 'key': 'R', 'code': 'KeyR'},\n" +
                    "  'S': {'keyCode': 83, 'key': 'S', 'code': 'KeyS'},\n" +
                    "  'T': {'keyCode': 84, 'key': 'T', 'code': 'KeyT'},\n" +
                    "  'U': {'keyCode': 85, 'key': 'U', 'code': 'KeyU'},\n" +
                    "  'V': {'keyCode': 86, 'key': 'V', 'code': 'KeyV'},\n" +
                    "  'W': {'keyCode': 87, 'key': 'W', 'code': 'KeyW'},\n" +
                    "  'X': {'keyCode': 88, 'key': 'X', 'code': 'KeyX'},\n" +
                    "  'Y': {'keyCode': 89, 'key': 'Y', 'code': 'KeyY'},\n" +
                    "  'Z': {'keyCode': 90, 'key': 'Z', 'code': 'KeyZ'},\n" +
                    "  ':': {'keyCode': 186, 'key': ':', 'code': 'Semicolon'},\n" +
                    "  '<': {'keyCode': 188, 'key': '<', 'code': 'Comma'},\n" +
                    "  '_': {'keyCode': 189, 'key': '_', 'code': 'Minus'},\n" +
                    "  '>': {'keyCode': 190, 'key': '>', 'code': 'Period'},\n" +
                    "  '?': {'keyCode': 191, 'key': '?', 'code': 'Slash'},\n" +
                    "  '~': {'keyCode': 192, 'key': '~', 'code': 'Backquote'},\n" +
                    "  '{': {'keyCode': 219, 'key': '{', 'code': 'BracketLeft'},\n" +
                    "  '|': {'keyCode': 220, 'key': '|', 'code': 'Backslash'},\n" +
                    "  '}': {'keyCode': 221, 'key': '}', 'code': 'BracketRight'},\n" +
                    "  '\"': {'keyCode': 222, 'key': '\"', 'code': 'Quote'},\n" +
                    "  'SoftLeft': {'key': 'SoftLeft', 'code': 'SoftLeft', 'location': 4},\n" +
                    "  'SoftRight': {'key': 'SoftRight', 'code': 'SoftRight', 'location': 4},\n" +
                    "  'Camera': {'keyCode': 44, 'key': 'Camera', 'code': 'Camera', 'location': 4},\n" +
                    "  'Call': {'key': 'Call', 'code': 'Call', 'location': 4},\n" +
                    "  'EndCall': {'keyCode': 95, 'key': 'EndCall', 'code': 'EndCall', 'location': 4},\n" +
                    "  'VolumeDown': {'keyCode': 182, 'key': 'VolumeDown', 'code': 'VolumeDown', 'location': 4},\n" +
                    "  'VolumeUp': {'keyCode': 183, 'key': 'VolumeUp', 'code': 'VolumeUp', 'location': 4},\n" +
                    "}";
            JSONObject json = JSON.parseObject(LAYOUT);
            json.forEach((key, value) -> keyMap.put(key, (JSONObject) value));
        };
        return keyMap.get(keyName);
    }

}
