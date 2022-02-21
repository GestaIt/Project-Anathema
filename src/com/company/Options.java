package com.company;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@SuppressWarnings("unused")
public class Options {
    private final Properties properties = new Properties();

    public Options() throws IOException {
        this.properties.load(new FileInputStream(String.format("%s\\src\\com\\resources\\config.properties",
                System.getProperty("user.dir"))));
    }

    public String readProperty(String property) {
        return this.properties.getProperty(property);
    }

    public void overwriteProperty(String property, String value) {
        this.properties.setProperty(property, value);
    }
}