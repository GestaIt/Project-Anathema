package com.company;

import com.roblox.botting.Cycles;
import com.roblox.misc.CookieFormatter;
import com.roblox.requests.Resources;
import org.apache.commons.configuration.ConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

@SuppressWarnings("unused")
public class Prompter {
    private static Options options;

    static {
        try {
            options = new Options();
        } catch (IOException | ConfigurationException e) {
            e.printStackTrace();
        }
    }

    private static final Resources resources = new Resources(options.readProperty("RobloxCookie"),
            options.readProperty("RobloxId"));
    private static Cycles cycles;

    static {
        try {
            cycles = new Cycles();
        } catch (IOException | ConfigurationException e) {
            e.printStackTrace();
        }
    }

    private static LocalFileReader cookiesFile;
    private static LocalFileReader proxiesFile;

    static {
        try {
            cookiesFile = new LocalFileReader("\\cookies.txt");
            proxiesFile = new LocalFileReader("\\proxies.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<String> cookies = null;
    private static ArrayList<String> proxies = null;

    static {
        try {
            cookies = cookiesFile.readLines();
            proxies = proxiesFile.readLines();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void initialize() throws IOException, ParserConfigurationException, SAXException, NoSuchMethodException, ConfigurationException, InterruptedException {
        System.out.println("""
                    \u001B[33m// | |                                                                  \s
                   //__| |      __      ___    __  ___ / __      ___      _   __      ___   \s
                  / ___  |   //   ) ) //   ) )  / /   //   ) ) //___) ) // ) )  ) ) //   ) )\s
                 //    | |  //   / / //   / /  / /   //   / / //       // / /  / / //   / / \s
                //     | | //   / / ((___( (  / /   //   / / ((____   // / /  / / ((___( (  \s
                --------------------------------------------------------------------------\u001B[35m
                """);
        System.out.println("Initialized Project Anathema in version 1.0");
        System.out.printf("Loaded %s %s and %s %s!%n\n", cookies.size(), (cookies.size() == 1) ? "cookie" : "cookies",
                proxies.size(), (proxies.size() == 1) ? "proxy" : "proxies");
        prompt();
    }

    private static void prompt() throws IOException, ParserConfigurationException, SAXException, NoSuchMethodException, ConfigurationException, InterruptedException {
        System.out.print("\u001B[35m[Anathema] $\u001B[0m");
        String command = scanner.nextLine();

        runCommand(command);
    }

    private static void runCommand(@NotNull String command) throws IOException, ParserConfigurationException, SAXException, NoSuchMethodException, ConfigurationException, InterruptedException {
        String[] arguments = command.split(" ");
        String lowerCommand = arguments[0].toLowerCase();

        switch(lowerCommand) {
            case "start" -> cycles.start(resources);

            case "format" -> CookieFormatter.formatCookies();

            case "set" -> {
                try {
                    String key = arguments[1];
                    String value = arguments[2];
                    options.readProperty(key);

                    options.overwriteProperty(key, value);
                    System.out.printf("\u001B[32mSuccessfully set %s to %s!\u001B[0m%n", key, value);
                } catch (Exception ignored) {}
            }

            case "read" -> {
                try {
                    String key = arguments[1];
                    String value = options.readProperty(key);

                    System.out.printf("\u001B[32m%s is %s\u001B[0m%n", key, value);
                } catch (Exception ignored) {}
            }

            default -> System.out.printf("\u001B[33m[Anathema] - %s is not a valid command. Please run help " +
                    "for a list of commands.\u001B[0m\n", command);
        }

        prompt();
    }
}