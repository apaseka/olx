package com.example.olx.service;

import com.example.olx.model.ParsingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.MINUTES;

@Service
public final class SchedulingService {

    @Autowired
    private ScheduledExecutorService scheduler;

    public void parsingRetry(Runnable command, ParsingRequest request) throws InterruptedException {
        final ScheduledFuture<?> scheduledFuture = scheduler.scheduleAtFixedRate(command, 0, request.getDelayMinutes(), MINUTES);

        while (true) {
            Thread.sleep(1000);
            if (request.getNumberOfLoops() <= request.getLoop()) {
                System.out.println("Count is " + request.getLoop() + ", cancel the scheduledFuture!");
                scheduledFuture.cancel(true);
                scheduler.shutdown();
                break;
            }
        }
    }
}
