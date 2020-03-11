package com.studyThread;

import ch.qos.logback.core.util.TimeUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class Study {

}

class FakeWake {


    /**
     * 虚假唤醒
     */
    private List<String> list = new ArrayList<String>();

    public synchronized void push(String value) {
        synchronized (this) {
            list.add(value);
            notify();
//            notifyAll();
            System.out.println("notify finish");

        }
    }

    public synchronized String pop() throws InterruptedException {
        synchronized (this) {
            //if变while可以解决虚假唤醒问题
            //关键是synchronized是非公平锁，当第一次wait之后，被pop抢到锁则正常，被push抢到锁则可能出现数组指针越界。
            if (list.size() <= 0) {
                wait();
                System.out.println("wait finish");
            }
            return list.remove(list.size() - 1);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        FakeWake FakeWake = new FakeWake();


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FakeWake.pop();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                FakeWake.push("a");
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FakeWake.pop();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        Thread.sleep(1000);
        System.out.println(FakeWake.list);


    }

}

/**
 * 循环打印ABC
 */

class ABC_Synch_1 {
    public static class ThreadPrinter implements Runnable {
        private String name;
        private Object prev;
        private Object self;

        private ThreadPrinter(String name, Object prev, Object self) {
            this.name = name;
            this.prev = prev;
            this.self = self;
        }

        @Override
        public void run() {
            int count = 10;
            while (count > 0) {// 多线程并发，不能用if，必须使用whil循环
                synchronized (prev) { // 先获取 prev 锁
                    synchronized (self) {// 再获取 self 锁
                        System.out.print(name);//打印
                        count--;
                        self.notifyAll();// 唤醒其他线程竞争self锁，注意此时self锁并未立即释放。
                    }
                    //此时执行完self的同步块，这时self锁才释放。
                    try {
                        prev.wait(); // 立即释放 prev锁，当前线程休眠，等待唤醒
                        /**
                         * JVM会在wait()对象锁的线程中随机选取一线程，赋予其对象锁，唤醒线程，继续执行。
                         */
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Object a = new Object();
        Object b = new Object();
        Object c = new Object();
        ThreadPrinter pa = new ThreadPrinter("A", c, a);
        ThreadPrinter pb = new ThreadPrinter("B", a, b);
        ThreadPrinter pc = new ThreadPrinter("C", b, c);

        new Thread(pa).start();
        Thread.sleep(10);//保证初始ABC的启动顺序
        new Thread(pb).start();
        Thread.sleep(10);
        new Thread(pc).start();
        Thread.sleep(10);
    }
}




/**
 * 计算接口
 */

interface Calculator {
    long sumUp(int[] numbers) throws Exception;
}

/**
 * 单线程完成
 */
class SingleThread implements Calculator {
    static long timeBefore;
    static long timeAfter;


    @Override
    public long sumUp(int[] calcData) throws Exception {
        timeBefore = System.currentTimeMillis();

        // 此句代码只是为了延长程序运行时间，和程序逻辑无关
        List<SingleThread> tasks = new ArrayList<SingleThread>();


        int calcDataLength = calcData.length;
        long sum = 0l;
        for (int i = 0; i < calcDataLength; i++) {
            sum += calcData[i];

            // 此句代码只是为了延长程序运行时间，和程序逻辑无关
            tasks.add(new SingleThread());
        }
        return sum;
    }

    public static void main(String[] args) throws Exception {
        //默认求和数组,arraylist中添加上亿个对象会堆溢出
        int[] calcData = new int[1000_0000];
        for (int i = 0; i < calcData.length; i++) {
            calcData[i] = i + 1;
        }

        System.out.println(new SingleThread().sumUp(calcData));
        timeAfter = System.currentTimeMillis();
        System.out.println("时间为" + (timeAfter - timeBefore));
    }

}

/***
 * 多线程实现
 */
class MutilThreadOfThreadPoolExecutor implements Calculator {
    static long timeBefore;
    static long timeAfter;

    ExecutorService executorService = new ThreadPoolExecutor(5, 10, // 线程数
            60l, TimeUnit.SECONDS,  // 超时时间
            new ArrayBlockingQueue<Runnable>(100, true),  // 线程处理数据的方式
            Executors.defaultThreadFactory(),  // 创建线程的工厂
            new ThreadPoolExecutor.CallerRunsPolicy());  // 超出处理范围的处理方式


    @Override
    public long sumUp(int[] calcData) throws Exception {
        long sum = 0;
        //从池中分配5个线程进行处理
        timeBefore = System.currentTimeMillis();

        for (int i = 0; i < 5; i++) {
            int arrStart = calcData.length / 5 * i;
            int arrEnd = calcData.length / 5 * (i + 1);

            SubTask subTask = new SubTask(calcData, arrStart, arrEnd);
            Future<Long> future = executorService.submit(subTask);
            sum += future.get().longValue();
            System.out.println("难道是算完一个结果才开始下一个" + sum);

        }
        LongTask longTask = new LongTask();
        ShortTask shortTask = new ShortTask();

        Future future = executorService.submit(shortTask);
        System.out.println("---先执行那句？----");
        executorService.submit(longTask);

        return sum;
    }

    private class LongTask implements Callable {

        @Override
        public Object call() throws Exception {
            Thread.sleep(10000);
            System.out.println("long task finish");
            return null;
        }
    }

    private class ShortTask implements Callable {


        @Override
        public Object call() throws Exception {
            Thread.sleep(1000);
            System.out.println("short task finish");

            return null;
        }
    }


    private class SubTask implements Callable {
        int[] arr;
        int start, end;

        private SubTask() {
        }

        private SubTask(int[] arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
        }

        @Override
        public Object call() throws Exception {

            // 此句代码只是为了延长程序运行时间，和程序逻辑无关
            List<SingleThread> tasks = new ArrayList<SingleThread>();


            long sum = 0l;
            //每个任务起止指针不同
            for (int i = start; i < end; i++) {
                sum += arr[i];

                // 此句代码只是为了延长程序运行时间，和程序逻辑无关
                tasks.add(new SingleThread());
            }
            return sum;
        }
    }


    public static void main(String[] args) throws Exception {
        //效率时间大致为13到58之间不等，比1700高了一百多倍,那是侧错了地方，其实效率一样，future.get是阻塞
        //默认求和数组,arraylist中添加上亿个对象会堆溢出
        int[] calcData = new int[1000_0000];
        for (int i = 0; i < calcData.length; i++) {
            calcData[i] = i + 1;
        }

        System.out.println("       " + new MutilThreadOfThreadPoolExecutor().sumUp(calcData));
        timeAfter = System.currentTimeMillis();
        System.out.println("时间为" + (timeAfter - timeBefore));


    }

}

class MutilThreadOfForkJoinPool implements Calculator {
    static long timeBefore;
    static long timeAfter;
    static ForkJoinPool forkJoinPool = new ForkJoinPool();

    private class SubTask extends RecursiveTask {
        int[] arr;
        int start, end;

        private SubTask() {
        }

        private SubTask(int[] arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
        }

        @Override
        public Object compute() {

            // 此句代码只是为了延长程序运行时间，和程序逻辑无关
            List<SingleThread> tasks = new ArrayList<SingleThread>();


            long sum = 0l;
            //每个任务起止指针不同
            for (int i = start; i < end; i++) {
                sum += arr[i];

                // 此句代码只是为了延长程序运行时间，和程序逻辑无关
                tasks.add(new SingleThread());
            }
            return sum;
        }
    }


    @Override
    public long sumUp(int[] calcData) throws Exception {
        long sum = 0;
        timeBefore = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            int arrStart = calcData.length / 5 * i;
            int arrEnd = calcData.length / 5 * (i + 1);

            SubTask subTask = new SubTask(calcData, arrStart, arrEnd);
            sum += (Long) forkJoinPool.invoke(subTask);

            System.out.println("难道是算完一个结果才开始下一个" + sum);


        }

        return sum;
    }

    public static void main(String[] args) throws Exception {
//fork效率会更好一点，如果用二分法递归处理效果可能更好。

        //默认求和数组,arraylist中添加上亿个对象会堆溢出
        int[] calcData = new int[1000_0000];
        for (int i = 0; i < calcData.length; i++) {
            calcData[i] = i + 1;
        }

        System.out.println("       " + new MutilThreadOfForkJoinPool().sumUp(calcData));
        timeAfter = System.currentTimeMillis();
        System.out.println("时间为" + (timeAfter - timeBefore));


    }

}


