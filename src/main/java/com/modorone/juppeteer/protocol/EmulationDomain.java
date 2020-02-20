package com.modorone.juppeteer.protocol;

public interface EmulationDomain {

    // command
    String setGeolocationOverrideCommand = "Emulation.setGeolocationOverride";
    String setScriptExecutionDisabledCommand = "Emulation.setScriptExecutionDisabled";
    String setDeviceMetricsOverrideCommand = "Emulation.setDeviceMetricsOverride";
    String setTouchEmulationEnabledCommand = "Emulation.setTouchEmulationEnabled";
}
