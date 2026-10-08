package com.example.demo.concurrency;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Component;

@Component
public class TransferLockManager {

    private final ConcurrentHashMap<String, ReentrantLock> locks =
            new ConcurrentHashMap<>();

    public ReentrantLock getLock(String accountNumber) {

        return locks.computeIfAbsent(
                accountNumber,
                key -> new ReentrantLock()
        );
    }
}