package com.hnxx.wisdombase.ui.setting;

import java.util.List;

public abstract class ZLConfig
{
    public static ZLConfig Instance()
    {
        return ourInstance;
    }

    private static ZLConfig ourInstance;

    protected ZLConfig()
    {
        ourInstance = this;
    }

    public abstract List<String> listGroups();

    public abstract List<String> listNames(String group);

    public abstract String getValue(String group, String name, String defaultValue);

    public abstract void setValue(String group, String name, String value);

    public abstract void unsetValue(String group, String name);

    public abstract void removeGroup(String name);
}
