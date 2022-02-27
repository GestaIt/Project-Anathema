package com.company;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.FileInputStream;
import java.io.IOException;

@SuppressWarnings("unused")
public class Options {
    private final PropertiesConfiguration properties = new PropertiesConfiguration();

    public Options() throws IOException, ConfigurationException {
        this.properties.load(new FileInputStream(String.format("%s\\config.properties",
                System.getProperty("user.dir"))));
    }

    public String readProperty(String property) {
        return this.properties.getProperty(property).toString();
    }

    public void overwriteProperty(String property, String value) throws ConfigurationException {
        this.properties.setProperty(property, value);
        this.properties.save(String.format("%s\\config.properties",
                System.getProperty("user.dir")));
    }
}