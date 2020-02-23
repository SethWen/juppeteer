package com.modorone.juppeteer.cdp;

public interface EmulationDomain {

    // command
    String setGeolocationOverrideCommand = "Emulation.setGeolocationOverride";
    String setScriptExecutionDisabledCommand = "Emulation.setScriptExecutionDisabled";
    String setDeviceMetricsOverrideCommand = "Emulation.setDeviceMetricsOverride";
    String setTouchEmulationEnabledCommand = "Emulation.setTouchEmulationEnabled";
    String setEmulatedMediaCommand = "Emulation.setEmulatedMedia";
    String setTimezoneOverrideCommand = "Emulation.setTimezoneOverride";
}
