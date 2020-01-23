package com.studyThread;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Study {
    @Test
    public void testCallable() throws ExecutionException, InterruptedException {
        MyCallable myCallable = new MyCallable();
        FutureTask<Integer> futureTask = new FutureTask<>(myCallable);
        new Thread(futureTask).start();

        Thread.sleep(1);
        System.out.println("main thread    ");
        futureTask.get();

    }

    @Test
    public void testInterrupt() throws InterruptedException {
//        Thread thread1 = new MyThread1();
//        thread1.start();
        //中断第一种方式
//        thread1.interrupt();
//        System.out.println("Main run");

        Thread thread2 = new MyThread2();
        thread2.start();
        Thread.sleep(100);
        //中断第二种方式
        thread2.interrupt();

        System.out.println("Main run");


    }

}


class MyCallable implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (i + 1);
            System.out.println("call thread    " + sum);
//            Thread.yield();

        }
        return sum;
    }
}

class MyThread1 extends Thread {
    @Override
    public void run() {
        try {
            Thread.sleep(2000);
            System.out.println("Thread run");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MyThread2 extends Thread {
    @Override
    public void run() {
        while (!interrupted()) {
            System.out.println("i am loop");
        }
        System.out.println("Thread end");
    }
}

