# main线程中获取其它线程的返回值

## 方法一：使用join

使用一个对象来传递值，主线程中等待工作线程执行完毕，然后读取该对象的值。

```java
package homework;

import java.util.concurrent.atomic.AtomicInteger;

public class MainThreadGetTheReturnValueOfOtherThread {

  private static AtomicInteger VALUE = new AtomicInteger(0);

  public static void main(String[] args) throws InterruptedException {
    System.out.println("主线程打印SHARE_VALUE的值: " + VALUE.get());
    System.out.println("主线程启动executor");
    Thread executor = new Thread(new Task(VALUE));
    executor.start();
    executor.join();
    System.out.println("主线程等待executor执行完毕");
    System.out.println("主线程打印SHARE_VALUE的值: " + VALUE.get());
  }

  private static class Task implements Runnable {

    private AtomicInteger value;

    public Task(AtomicInteger value) {
      this.value = value;
    }

    @Override
    public void run() {
      try {
        Thread.sleep(5);
        System.out.println("executor:" + Thread.currentThread().getName() + "开始执行!");
        for (int i = 0; i < 100; ++i) {
          Thread.sleep(1);
          value.incrementAndGet();
        }
        System.out.println("executor:" + Thread.currentThread().getName() + "执行完毕!");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
```

执行结果

```shell
主线程打印SHARE_VALUE的值: 1
主线程启动executor
executor:Thread-0开始执行!
executor:Thread-0执行完毕!
主线程等待executor执行完毕
主线程打印SHARE_VALUE的值: 100
```

## 方法二：通过Callable获取返回值

```java
package homework;

import java.util.concurrent.*;

public class MainThreadGetTheReturnValueOfOtherThread {

  public static void main(String[] args) throws InterruptedException, ExecutionException {
    System.out.println("主线程启动executor");
    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    int value = executor.submit(new Task()).get();
    System.out.println("主线程等待executor执行完毕");
    System.out.println("主线程获取executor返回的值: " + value);
  }

  private static class Task implements Callable<Integer> {

    private int value;


    @Override
    public Integer call() throws Exception {
      System.out.println("executor:" + Thread.currentThread().getName() + "开始执行!");
      for (int i = 0; i < 100; ++i) {
        Thread.sleep(1);
        ++value;
      }
      System.out.println("executor:" + Thread.currentThread().getName() + "执行完毕!");
      return value;
    }
  }
}
```

执行结果

```shell
主线程启动executor
executor:pool-1-thread-1开始执行!
executor:pool-1-thread-1执行完毕!
主线程等待executor执行完毕
主线程获取executor返回的值: 100
```

## 方法三：使用线程池，通过Future获取值

```java
package homework;

import java.util.concurrent.*;

public class MainThreadGetTheReturnValueOfOtherThread {

  public static void main(String[] args) throws InterruptedException, ExecutionException {
    System.out.println("主线程启动executor");
    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    Future<Integer> value = executor.submit(new Task());
    while (!value.isDone()) {
      Thread.sleep(10);
      System.out.println("executor未执行完毕,主线程做自己的事");
    }
    System.out.println("executor执行完毕,主线程开始获取返回的值");
    System.out.println("主线程获取executor返回的值: " + value.get());
  }

  private static class Task implements Callable<Integer> {

    private int value;


    @Override
    public Integer call() throws Exception {
      System.out.println("executor:" + Thread.currentThread().getName() + "开始执行!");
      for (int i = 0; i < 100; ++i) {
        Thread.sleep(1);
        ++value;
      }
      System.out.println("executor:" + Thread.currentThread().getName() + "执行完毕!");
      return value;
    }
  }
}
```

执行结果

```shell
主线程启动executor
executor未执行完毕,主线程做自己的事
executor:pool-1-thread-1开始执行!
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor:pool-1-thread-1执行完毕!
executor未执行完毕,主线程做自己的事
executor执行完毕,主线程开始获取返回的值
主线程获取executor返回的值: 100
```

## 方法四：使用一个阻塞队列来存储线程中的值

```java
package homework;

import java.util.concurrent.*;

public class MainThreadGetTheReturnValueOfOtherThread {

  public static BlockingQueue<Integer> resultQueue = new LinkedBlockingQueue<>(2);

  public static void main(String[] args) throws InterruptedException, ExecutionException {
    System.out.println("主线程启动executor");
    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    executor.execute(new Task());
    while (resultQueue.size() == 0) {
      Thread.sleep(10);
      System.out.println("executor未执行完毕,主线程做自己的事");
    }
    System.out.println("executor执行完毕,主线程开始获取返回的值");
    System.out.println("主线程获取executor返回的值: " + resultQueue.take());
    executor.shutdown();
  }

  private static class Task implements Runnable {

    private int value;

    @Override
    public void run() {
      System.out.println("executor:" + Thread.currentThread().getName() + "开始执行!");
      for (int i = 0; i < 100; ++i) {
        try {
          Thread.sleep(1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        ++value;
      }
      System.out.println("executor:" + Thread.currentThread().getName() + "执行完毕!");
      resultQueue.add(value);
    }
  }
}
```

执行结果

```shell
主线程启动executor
executor未执行完毕,主线程做自己的事
executor:pool-1-thread-1开始执行!
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor未执行完毕,主线程做自己的事
executor:pool-1-thread-1执行完毕!
executor未执行完毕,主线程做自己的事
executor执行完毕,主线程开始获取返回的值
主线程获取executor返回的值: 100
```

## 方法五：使用CompletableFuture

```java
package homework;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MainThreadGetTheReturnValueOfOtherThread {

  public static void main(String[] args) {
    System.out.println("主线程启动executor");
    AtomicInteger result = CompletableFuture.supplyAsync(() -> new AtomicInteger(0)).thenApplyAsync(MainThreadGetTheReturnValueOfOtherThread::task).join();
    System.out.println("主线程等待executor执行完毕,主线程开始获取返回的值");
    System.out.println("主线程获取executor返回的值: " + result.get());
  }

  private static AtomicInteger task(AtomicInteger atomicInteger) {
    System.out.println("executor:" + Thread.currentThread().getName() + "开始执行!");
    for (int i = 0; i < 100; ++i) {
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      atomicInteger.incrementAndGet();
    }
    System.out.println("executor:" + Thread.currentThread().getName() + "执行完毕!");
    return atomicInteger;
  }
}
```

执行结果

```shell
主线程启动executor
executor:ForkJoinPool.commonPool-worker-3开始执行!
executor:ForkJoinPool.commonPool-worker-3执行完毕!
主线程等待executor执行完毕,主线程开始获取返回的值
主线程获取executor返回的值: 100
```

## 方法六：使用Stream

```java
package homework;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MainThreadGetTheReturnValueOfOtherThread {

  public static void main(String[] args) throws InterruptedException {
    List<Callable> taskQueue = new ArrayList<>();
    for (int i = 0; i < 4; ++i) {
      taskQueue.add(new Task(new AtomicInteger(i)));
    }
    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    System.out.println("主线程开始取出任务并提交至线程池执行");
    List<AtomicInteger> resultList = taskQueue.stream().parallel().
            map(task -> {
              System.out.println("取出任务的线程: " + Thread.currentThread().getName());
              try {
                return (AtomicInteger) executor.submit(task).get();
              } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new AtomicInteger(0);
              }
            }).collect(Collectors.toList());
    System.out.println("主线程等待executor执行完毕,主线程开始获取返回的值");
    for (AtomicInteger atomicInteger : resultList) {
      System.out.println("主线程获取executor返回的值: " + atomicInteger);
    }
  }

  private static class Task implements Callable<AtomicInteger> {

    AtomicInteger atomicInteger;

    public Task(AtomicInteger atomicInteger) {
      this.atomicInteger = atomicInteger;
    }

    @Override
    public AtomicInteger call() {
      System.out.println("executor:" + Thread.currentThread().getName() + "开始执行!");
      for (int i = 0; i < 100; ++i) {
        try {
          Thread.sleep(1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        atomicInteger.incrementAndGet();
      }
      System.out.println("executor:" + Thread.currentThread().getName() + "执行完毕!");
      return atomicInteger;
    }
  }
}
```

执行结果

```shell
主线程开始取出任务并提交至线程池执行
取出任务的线程: main
取出任务的线程: ForkJoinPool.commonPool-worker-3
取出任务的线程: ForkJoinPool.commonPool-worker-5
取出任务的线程: ForkJoinPool.commonPool-worker-7
executor:pool-1-thread-2开始执行!
executor:pool-1-thread-1开始执行!
executor:pool-1-thread-3开始执行!
executor:pool-1-thread-4开始执行!
executor:pool-1-thread-1执行完毕!
executor:pool-1-thread-3执行完毕!
executor:pool-1-thread-4执行完毕!
executor:pool-1-thread-2执行完毕!
主线程等待executor执行完毕,主线程开始获取返回的值
主线程获取executor返回的值: 100
主线程获取executor返回的值: 101
主线程获取executor返回的值: 102
主线程获取executor返回的值: 103
```

## 方法七：使用CountDownLatch

```java
package homework;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class MainThreadGetTheReturnValueOfOtherThread {

  private static AtomicInteger VALUE = new AtomicInteger(0);

  public static void main(String[] args) throws InterruptedException {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    System.out.println("主线程打印SHARE_VALUE的值: " + VALUE.get());
    System.out.println("主线程启动executor");
    Thread executor = new Thread(new Task(VALUE, countDownLatch));
    executor.start();
    countDownLatch.await();
    System.out.println("主线程等待executor执行完毕");
    System.out.println("主线程获取到的SHARE_VALUE的值: " + VALUE.get());
  }

  private static class Task implements Runnable {

    private AtomicInteger value;
    private CountDownLatch countDownLatch;

    public Task(AtomicInteger value, CountDownLatch countDownLatch) {
      this.value = value;
      this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
      try {
        Thread.sleep(5);
        System.out.println("executor:" + Thread.currentThread().getName() + "开始执行!");
        for (int i = 0; i < 100; ++i) {
          Thread.sleep(1);
          value.incrementAndGet();
        }
        System.out.println("executor:" + Thread.currentThread().getName() + "执行完毕!");
        countDownLatch.countDown();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
```

执行结果

```shell
主线程打印SHARE_VALUE的值: 0
主线程启动executor
executor:Thread-0开始执行!
executor:Thread-0执行完毕!
主线程等待executor执行完毕
主线程获取到的SHARE_VALUE的值: 100
```



