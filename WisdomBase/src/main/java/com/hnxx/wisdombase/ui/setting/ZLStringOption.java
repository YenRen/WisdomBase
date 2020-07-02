package com.hnxx.wisdombase.ui.setting;

public final class ZLStringOption extends ZLOption {
    private final String myDefaultValue;
    private String myValue;

    public ZLStringOption(String group, String optionName, String defaultValue) {
        super(group, optionName);
        myDefaultValue = (defaultValue != null) ? defaultValue.intern() : "";
        myValue = myDefaultValue;
    }

    public String getValue() {
        if (!myIsSynchronized) {
            String value = getConfigValue(myDefaultValue);
            if (value != null) {
                myValue = value;
            }
            myIsSynchronized = true;
        }
        return myValue;
    }

    public void setValue(String value) {
        if (value == null) {
            return;
        }
        value = value.intern();
        if (myIsSynchronized && (myValue == value)) {
            return;
        }
        myValue = value;
        if (value == myDefaultValue) {
            unsetConfigValue();
        } else {
            setConfigValue(value);
        }
        myIsSynchronized = true;
    }

    public void resetValue() {
        setValue(myDefaultValue);
    }
}
