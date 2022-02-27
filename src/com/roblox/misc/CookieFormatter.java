package com.roblox.misc;

import com.company.LocalFileReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class CookieFormatter {
    private static LocalFileReader fileReader;

    static {
        try {
            fileReader = new LocalFileReader("\\cookies.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void formatCookies() throws IOException {
        PrintWriter fileWriter = new PrintWriter(System.getProperty("user.dir") + "\\formatted.txt");
        ArrayList<String> cookies = fileReader.readLines();

        fileWriter.print("");
        cookies.forEach(cookie -> fileWriter.println(cookie.replaceAll("\\w+:\\d+:", "")));
        fileWriter.close();
    }
}