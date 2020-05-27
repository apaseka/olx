package com.example.olx.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
public final class CompletableFutures {

    @Autowired
    private ScheduledExecutorService service;

    private CompletableFutures() {
    }

    public void delayedRunAsync(Runnable action, long delay, TimeUnit unit) {
        service.schedule(() -> CompletableFuture.runAsync(action), delay, unit);
    }

    public <T> CompletableFuture<T> delayedSupplyAsync(Supplier<T> supplier, long delay, TimeUnit unit) {
        CompletableFuture<T> future = new CompletableFuture<>();
        service.schedule(() -> future.complete(supplier.get()), delay, unit);
        return future;
    }

    public <T> CompletableFuture<T> retry(int times, int delay, TimeUnit unit, Supplier<CompletableFuture<T>> action) {
        CompletableFuture<T> future = new CompletableFuture<>();
        action.get().thenAccept((t) -> exceptionally(times, delay, unit, action, future)
        );
        return future;
    }

    private <T> void retry(int times, int delay, TimeUnit unit, Supplier<CompletableFuture<T>> action, CompletableFuture<T> future) {
        action.get().thenAccept((t) -> exceptionally(times, delay, unit, action, future)
        );
    }

    private <T> void exceptionally(int times,
                                          int delay,
                                          TimeUnit unit,
                                          Supplier<CompletableFuture<T>> action,
                                          CompletableFuture<T> future) {

        if (times > 1) {
            service.schedule(() -> retry(times - 1, delay, unit, action, future), delay, unit);
        }
    }
}


