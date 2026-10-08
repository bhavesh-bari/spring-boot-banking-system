//package com.example.demo;
//
//import java.math.BigDecimal;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import com.example.demo.service.AccountService;
//
//@SpringBootTest
//public class ConcurrencyTest {
//
//    @Autowired
//    private AccountService accountService;
//
//    @Test
//    void testConcurrentTransfer() throws InterruptedException {
//
//        ExecutorService executor = Executors.newFixedThreadPool(10);
//
//        CountDownLatch startLatch = new CountDownLatch(1);
//
//        for (int i = 0; i < 100; i++) {
//
//            final int taskNumber = i;
//
//            executor.submit(() -> {
//
//                try {
//
//                    startLatch.await();
//
//                    if (taskNumber % 2 == 0) {
//
//                        accountService.transfer(
//                            "ACC107",
//                            new BigDecimal("20000"),
//                            "ACC108"
//                        );
//
//                        System.out.println(
//                            Thread.currentThread().getName()
//                            + " Transfer "
//                            + taskNumber
//                            + " : ACC107 -> ACC1003 SUCCESS"
//                        );
//
//                    } else {
//
//                        accountService.transfer(
//                            "ACC108",
//                            new BigDecimal("1000"),
//                            "ACC107"
//                        );
//
//                        System.out.println(
//                            Thread.currentThread().getName()
//                            + " Transfer "
//                            + taskNumber
//                            + " : ACC1003 -> ACC107 SUCCESS"
//                        );
//                    }
//
//                } catch (Exception e) {
//
//                    System.out.println(
//                        Thread.currentThread().getName()
//                        + " Transfer "
//                        + taskNumber
//                        + " FAILED: "
//                        + e.getClass().getSimpleName()
//                        + " - "
//                        + e.getMessage()
//                    );
//                }
//            });
//        }
//
//        startLatch.countDown();
//
//        executor.shutdown();
//
//        executor.awaitTermination(30, TimeUnit.SECONDS);
//    }
//}