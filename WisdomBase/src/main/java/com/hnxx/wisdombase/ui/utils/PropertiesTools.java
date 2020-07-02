package com.hnxx.wisdombase.ui.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class PropertiesTools
{
    public static Properties loadConfig(String file)
    {
        Properties properties = new Properties();
        try
        {
            FileInputStream s = new FileInputStream(file);
            properties.load(s);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return properties;
    }

    public static void saveConfig(String file, Properties properties)
    {
        try
        {
            FileOutputStream s = new FileOutputStream(file, false);
            properties.store(s, "");
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
