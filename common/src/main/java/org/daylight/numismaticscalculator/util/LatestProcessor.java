package org.daylight.numismaticscalculator.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class LatestProcessor<T> {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final AtomicReference<T> latest = new AtomicReference<>();
    private volatile boolean running = false;
    private volatile boolean scheduled = false;
    private final Consumer<T> functionality;

    public LatestProcessor(Consumer<T> functionality) {
        this.functionality = functionality;
    }

    public void submit(T value) {
        latest.set(value);
        synchronized (this) {
            if (running) {
                scheduled = true; // пометим, что есть новые данные
                return;
            }
            running = true;
        }
        executor.submit(this::runLoop);
    }

    private void runLoop() {
        try {
            while (true) {
                T value = latest.getAndSet(null);
                if (value != null) {
                    process(value);
                }
                synchronized (this) {
                    if (scheduled) {
                        scheduled = false;
                        continue; // сразу ещё одна итерация
                    }
                    running = false;
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void process(T value) {
        functionality.accept(value);
    }
}
