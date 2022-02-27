package com.roblox.botting;

import com.company.LocalFileReader;
import com.company.Options;
import com.google.common.collect.Iterables;
import com.roblox.classes.Cookie;
import com.roblox.classes.Model;
import org.apache.commons.configuration.ConfigurationException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("unused")
public class Botter {
    private Method botter;
    private final Options options = new Options();
    private final ArrayList<Thread> threadsList = new ArrayList<>();

    public Botter(Method botter, String assetId, int objectsPerChunk) throws IOException, ConfigurationException, InterruptedException {
        this.botter = botter;

        Botters botters = new Botters();

        List<Cookie> cookies = new ArrayList<>();
        LocalFileReader cookiesFile = new LocalFileReader("\\cookies.txt");
        cookiesFile.readLines().forEach((cookieString) -> cookies.add(new Cookie(cookieString)));
        LocalFileReader proxiesFile = new LocalFileReader("\\proxies.txt");
        ArrayList<String> proxies = new ArrayList<>(proxiesFile.readLines());

        Iterable<List<Cookie>> cookiePartitions = Iterables.partition(cookies, objectsPerChunk);
        Iterable<List<String>> proxyPartitions = Iterables.partition(proxies, objectsPerChunk);

        AtomicLong purchases = new AtomicLong();
        Model model = new Model(assetId);

        for(int i = 0; i < Iterables.size(cookiePartitions); i++) {
            List<Cookie> cookiePartition = Iterables.get(cookiePartitions, Math.min(i,
                    Iterables.size(cookiePartitions)-1));
            List<String> proxyPartition = Iterables.get(proxyPartitions, Math.min(i,
                    Iterables.size(proxyPartitions)-1));

            Thread newThread = new Thread(() -> {
                try {
                    botter.invoke(botters, model, cookiePartition, proxyPartition,
                            Long.parseLong(options.readProperty("SalesThreshold")), purchases);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });

            threadsList.add(newThread);
            newThread.start();
        }

        while(!this.threadsList.get(0).isInterrupted())
            TimeUnit.SECONDS.sleep(3);
    }

    public void stop() {
        threadsList.forEach(Thread::interrupt);
    }

    public void setBotter(Method botter) {
        this.botter = botter;
        threadsList.forEach(Thread::interrupt);
    }
}
