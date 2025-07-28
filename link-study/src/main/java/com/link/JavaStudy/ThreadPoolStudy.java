package com.link.JavaStudy;
/*
1. **FixedThreadPool（固定线程数的线程池）**：
通过`Executors.newFixedThreadPool(int nThreads)`创建。
线程池中的线程数量是固定的，如果某个线程因为执行异常而结束，线程池会补充一个新的线程。
适用于负载比较重的服务器，需要限制当前线程的数量。
2. **SingleThreadExecutor（单线程的线程池）**：
通过`Executors.newSingleThreadExecutor()`创建。
线程池中只有一个线程，所有的任务都按照提交的顺序执行。
适用于需要保证顺序执行任务的场景。
3. **CachedThreadPool（可缓存线程的线程池）**：
通过`Executors.newCachedThreadPool()`创建。
线程池中线程的数量不固定，可以根据需求自动扩展。
当线程空闲超过60秒时，会被回收。
适用于执行大量短期异步任务的程序。
4. **ScheduledThreadPool（定时及周期执行的线程池）**：
通过`Executors.newScheduledThreadPool(int corePoolSize)`创建。
可以延迟或定时执行任务。
适用于执行定时任务和具体周期重复任务。
5. **WorkStealingPool（工作窃取线程池）**：
通过`Executors.newWorkStealingPool()`（Java 8引入）创建。
这是一个并行级别（ForkJoinPool）的线程池，它基于“工作窃取”算法，可以有效地处理不同线程之间的工作负载平衡。
适用于可以并行处理的任务。
6. **ForkJoinPool（分支/合并框架线程池）**：
可以通过`ForkJoinPool.commonPool()`或者直接创建`new ForkJoinPool()`使用。
专为可以递归分解成小块的任务而设计，类似于MapReduce模式。
适用于计算密集型任务。
*/
public class ThreadPoolStudy {
    public static void main(String[] args) {
        MyThread t1 = new MyThread();
        MyThread t2 = new MyThread();
        MyThread t3 = new MyThread();
        MyThread t4 = new MyThread();
        MyThread t0 = new MyThread();
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t0.start();


    }


    private static class MyThread {


        public void start() {

        }
    }
}
