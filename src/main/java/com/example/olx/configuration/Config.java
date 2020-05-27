package com.example.olx.configuration;

import com.example.olx.internal.CommandAdapter;
import com.example.olx.service.ParserService;
import com.example.olx.service.RateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

@EnableScheduling
@Configuration
public class Config {

    @Autowired
    ParserService parserService;

    public static Set<String> urlToParse = new TreeSet<>();

    @Scheduled(fixedDelay = 1000 * 60 * 60 * 6)
    public void doSomething() {
        RateService.downloadRates();
        urlToParse.forEach(u -> CompletableFuture.runAsync(() -> parserService.parse(u)));
        parserService.init();
    }

    @Bean
    public ScheduledExecutorService getExecutorService() {
        final Logger logger = LoggerFactory.getLogger(ScheduledExecutorService.class);
        final Thread.UncaughtExceptionHandler handler = (t, e) -> logger.error(String.format("Uncaught exception in thread %s", t.getName()), e);

        ThreadFactory factory = new ThreadFactory() {

            private final ThreadFactory factory = Executors.defaultThreadFactory();
            private final AtomicLong counter = new AtomicLong();

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = factory.newThread(r);
                thread.setUncaughtExceptionHandler(handler);
                thread.setName("SCF_" + counter.getAndIncrement());
                return thread;
            }
        };
        return Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() * 10, factory);
    }

    @Bean
    public CommandAdapter getCommandAdapter() {
        return new CommandAdapter();
    }
}
