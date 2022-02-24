package com.roblox.botting;

import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class Botter {
    private Botters.BotterInterface botter;
    private final String assetId;
    private final ArrayList<Thread> threadsList = new ArrayList<>();
    private long threads;
    private boolean running = true;

    public Botter(Botters.BotterInterface botter, String assetId, long threads) {
        this.botter = botter;
        this.assetId = assetId;
        this.threads = threads;
    }

    public Botter(Botters.BotterInterface botter, String assetId, long threads, boolean running) {
        this.botter = botter;
        this.assetId = assetId;
        this.threads = threads;
        this.running = running;
    }

    public void setThreads(long threads) {
        if(threads >= this.threads) { // if the new amount of threads is greater, we must create new threads
            for(long i = this.threads; i <= threads; i++) {
                this.threads++;

                Thread newThread = new Thread(() -> {
                    try {
                        botter.bot(this.assetId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                threadsList.add(newThread);
            }
        } else {
            for(long i = this.threads; i >= threads; i--) {
                this.threads--;

                Thread currentThread = threadsList.get((int) i);
                currentThread.stop();

                threadsList.remove((int) i);
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setBotter(Botters.BotterInterface botter) {
        this.botter = botter;
    }
}
