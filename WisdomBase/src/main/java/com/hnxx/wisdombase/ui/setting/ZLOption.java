package com.hnxx.wisdombase.ui.setting;

public abstract class ZLOption {
    public static final String PLATFORM_GROUP = "PlatformOptions";

    private final String myGroup;
    private final String myOptionName;
    protected boolean myIsSynchronized;

    protected ZLOption(String group, String optionName) {
        myGroup = group.intern();
        myOptionName = optionName.intern();
        myIsSynchronized = false;
    }

    protected final String getConfigValue(String defaultValue) {
        ZLConfig config = ZLConfig.Instance();
        return (config != null) ? config.getValue(myGroup, myOptionName, defaultValue) : defaultValue;
    }

    protected final void setConfigValue(String value) {
        ZLConfig config = ZLConfig.Instance();
        if (config != null) {
            config.setValue(myGroup, myOptionName, value);
        }
    }

    protected final void unsetConfigValue() {
        ZLConfig config = ZLConfig.Instance();
        if (config != null) {
            config.unsetValue(myGroup, myOptionName);
        }
    }

    protected final void removeConfigGroup() {
        ZLConfig config = ZLConfig.Instance();
        if (config != null) {
            config.removeGroup(myGroup);
        }
    }
}
