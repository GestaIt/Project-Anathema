package com.roblox.botting;

import com.roblox.classes.Cookie;
import com.roblox.classes.Model;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("unused")
public class Botters {
    private static final int OK = 200;
    private static final int TOOMANYREQUESTS = 429;

    public void modelBot(Model otherModel, List<Cookie> cookies, List<String> proxies, long threshold,
                         AtomicLong purchases) {
        final int[] proxyPosition = {0};
        boolean purchasing = true;
        Model model = new Model(otherModel);

        while (!Thread.interrupted() && purchases.get() < threshold) {
            if (purchasing) {
                for (Cookie cookie : cookies) {
                    if(Thread.interrupted() || purchases.get() > threshold)
                        break;

                    try {
                        int code = model.buy(cookie.toString());

                        if (code == OK) {
                            purchases.getAndIncrement();
                            System.out.println("\u001B[32mSuccessfully purchased! " +
                                    String.format("Currently at %s purchases!\u001B[0m", purchases.get()));
                        } else if (code == TOOMANYREQUESTS) {
                            System.out.println("\u001B[33mRatelimited. Changing proxy\u001B[0m");
                            model.setProxy(proxies.get(proxyPosition[0]));

                            if (proxyPosition[0] + 1 >= proxies.size())
                                proxyPosition[0] = 0;
                            else
                                proxyPosition[0]++;
                        } else
                            System.out.println("\u001B[31mFailed to purchase\u001B[0m");
                    } catch (IOException ignored) {}
                }
            } else {
                for (Cookie cookie : cookies) {
                    if(Thread.interrupted() || purchases.get() > threshold)
                        break;

                    try {
                        int code = model.delete(cookie.toString());

                        if (code == OK) {
                            System.out.println("\u001B[32mSuccessfully deleted!\u001B[0m");
                        } else if (code == TOOMANYREQUESTS) {
                            System.out.println("\u001B[33mRatelimited. Changing proxy\u001B[0m");
                            model.setProxy(proxies.get(proxyPosition[0]));

                            if (proxyPosition[0] + 1 >= proxies.size())
                                proxyPosition[0] = 0;
                            else
                                proxyPosition[0]++;
                        } else
                            System.out.println("\u001B[31mFailed to delete\u001B[0m");
                    } catch (IOException ignored) {
                    }
                }
            }

            purchasing = !purchasing;
        }

        Thread.currentThread().interrupt();
    }

    public void favoriteBot(Model otherModel, @NotNull List<Cookie> cookies, List<String> proxies, long threshold,
                     AtomicLong purchases) throws InterruptedException {
        final int[] proxyPosition = {0};
        AtomicLong favorites = new AtomicLong();
        Model model = new Model(otherModel);

        cookies.forEach((cookie) -> {
            try {
                int code = model.toggleFavorite(String.valueOf(cookie));

                if(code == OK) {
                    favorites.getAndIncrement();
                    System.out.println("\u001B[32mSuccessfully favorited! " +
                            String.format("Currently at %s purchases!\u001B[0m", favorites.get()));
                } else if(code == TOOMANYREQUESTS) {
                    System.out.println("\u001B[33mRatelimited. Changing proxy\u001B[0m");
                    model.setProxy(proxies.get(proxyPosition[0]));

                    if(proxyPosition[0] + 1 >= proxies.size())
                        proxyPosition[0] = 0;
                    else
                        proxyPosition[0]++;
                } else
                    System.out.println("\u001B[31mFailed to favorite\u001B[0m");
            } catch (IOException ignored) {}
        });

        Thread.currentThread().join();
    }
}