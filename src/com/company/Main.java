package com.company;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main {
    // Our programs entrypoint
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        Prompter.initialize();
    }
}