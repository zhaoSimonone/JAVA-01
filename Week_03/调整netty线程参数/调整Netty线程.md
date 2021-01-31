# 调整Netty的线程数量

## netty运行原理图

[![yEe6rn.png](https://s3.ax1x.com/2021/01/31/yEe6rn.png)](https://imgchr.com/i/yEe6rn)

## 调整线程数

###  使用sb进行压测

模拟40个并发连接，测试时间为60S。

```shell
sb -u http://localhost:8804 -c 40 -N 60
```

### 固定bossGroup，增加workGroup

#### bossGroup：2  ; workerGroup：20

```java
EventLoopGroup bossGroup = new NioEventLoopGroup(2);
EventLoopGroup workerGroup = new NioEventLoopGroup(20);
```

压测结果

```shell
PS C:\Windows\System32> sb -u http://localhost:8804 -c 40 -N 60
Starting at 2021/1/31 14:21:19
[Press C to stop the test]
198895  (RPS: 3125.3)
---------------Finished!----------------
Finished at 2021/1/31 14:22:22 (took 00:01:03.8242229)
Status 200:    198895

RPS: 3250.6 (requests/second)
Max: 187ms
Min: 4ms
Avg: 10.6ms

  50%   below 11ms
  60%   below 11ms
  70%   below 11ms
  80%   below 11ms
  90%   below 11ms
  95%   below 11ms
  98%   below 12ms
  99%   below 13ms
99.9%   below 20ms
PS C:\Windows\System32>
```

#### bossGroup：2  ; workerGroup：30

```java
 EventLoopGroup bossGroup = new NioEventLoopGroup(2);
 EventLoopGroup workerGroup = new NioEventLoopGroup(30);
```

压测结果

```shell
PS C:\Windows\System32> sb -u http://localhost:8804 -c 40 -N 60
Starting at 2021/1/31 14:27:24
[Press C to stop the test]
285689  (RPS: 4478.5)
---------------Finished!----------------
Finished at 2021/1/31 14:28:28 (took 00:01:03.9827817)
Status 200:    285689

RPS: 4668.1 (requests/second)
Max: 209ms
Min: 4ms
Avg: 7.1ms

  50%   below 5ms
  60%   below 6ms
  70%   below 10ms
  80%   below 11ms
  90%   below 11ms
  95%   below 11ms
  98%   below 11ms
  99%   below 12ms
99.9%   below 20ms
PS C:\Windows\System32>
```

#### bossGroup：2  ; workerGroup：40

```java
 EventLoopGroup bossGroup = new NioEventLoopGroup(2);
 EventLoopGroup workerGroup = new NioEventLoopGroup(40);
```

压测结果

```shell
PS C:\Windows\System32> sb -u http://localhost:8804 -c 40 -N 60
Starting at 2021/1/31 14:29:50
[Press C to stop the test]
351289  (RPS: 5520.7)
---------------Finished!----------------
Finished at 2021/1/31 14:30:54 (took 00:01:03.8296788)
Status 200:    351297

RPS: 5740 (requests/second)
Max: 187ms
Min: 4ms
Avg: 5.3ms

  50%   below 5ms
  60%   below 5ms
  70%   below 5ms
  80%   below 5ms
  90%   below 6ms
  95%   below 6ms
  98%   below 7ms
  99%   below 8ms
99.9%   below 10ms
PS C:\Windows\System32>
```

#### bossGroup：2  ; workerGroup：50

```shell
 EventLoopGroup bossGroup = new NioEventLoopGroup(2);
 EventLoopGroup workerGroup = new NioEventLoopGroup(50);
```

压测结果

```shell
PS C:\Windows\System32> sb -u http://localhost:8804 -c 40 -N 60
Starting at 2021/1/31 14:32:46
[Press C to stop the test]
352344  (RPS: 5544.1)
---------------Finished!----------------
Finished at 2021/1/31 14:33:50 (took 00:01:03.7458936)
Status 200:    352345

RPS: 5757.4 (requests/second)
Max: 187ms
Min: 4ms
Avg: 5.3ms

  50%   below 5ms
  60%   below 5ms
  70%   below 5ms
  80%   below 5ms
  90%   below 6ms
  95%   below 6ms
  98%   below 7ms
  99%   below 8ms
99.9%   below 10ms
PS C:\Windows\System32>
```

### 固定workGroup，增加bossGroup

#### bossGroup：2  ; workerGroup：40

```java
EventLoopGroup bossGroup = new NioEventLoopGroup(2);
EventLoopGroup workerGroup = new NioEventLoopGroup(40);
```

压测结果

```shell
PS C:\Windows\System32> sb -u http://localhost:8804 -c 40 -N 60
Starting at 2021/1/31 14:09:47
[Press C to stop the test]
346077  (RPS: 5438.3)
---------------Finished!----------------
Finished at 2021/1/31 14:10:51 (took 00:01:03.8325966)
Status 200:    346078

RPS: 5655 (requests/second)
Max: 210ms
Min: 4ms
Avg: 5.3ms

  50%   below 5ms
  60%   below 5ms
  70%   below 5ms
  80%   below 6ms
  90%   below 6ms
  95%   below 6ms
  98%   below 7ms
  99%   below 8ms
99.9%   below 14ms
PS C:\Windows\System32>
```

#### bossGroup：4  ; workerGroup：40

```java
EventLoopGroup bossGroup = new NioEventLoopGroup(4);
EventLoopGroup workerGroup = new NioEventLoopGroup(40);
```

压测结果

```shell
PS C:\Windows\System32> sb -u http://localhost:8804 -c 40 -N 60
Starting at 2021/1/31 14:05:23
[Press C to stop the test]
350370  (RPS: 5499)
---------------Finished!----------------
Finished at 2021/1/31 14:06:27 (took 00:01:03.9234367)
Status 200:    350376

RPS: 5724.1 (requests/second)
Max: 198ms
Min: 4ms
Avg: 5.3ms

  50%   below 5ms
  60%   below 5ms
  70%   below 5ms
  80%   below 5ms
  90%   below 6ms
  95%   below 6ms
  98%   below 7ms
  99%   below 8ms
99.9%   below 10ms
PS C:\Windows\System32>
```

#### bossGroup：6  ; workerGroup：40

```java
EventLoopGroup bossGroup = new NioEventLoopGroup(6);
EventLoopGroup workerGroup = new NioEventLoopGroup(40);
```

压测结果

```shell
PS C:\Windows\System32> sb -u http://localhost:8804 -c 40 -N 60
Starting at 2021/1/31 14:15:26
[Press C to stop the test]
352338  (RPS: 5543.1)
---------------Finished!----------------
Finished at 2021/1/31 14:16:30 (took 00:01:03.7598381)
Status 200:    352341

RPS: 5756.9 (requests/second)
Max: 132ms
Min: 4ms
Avg: 5.3ms

  50%   below 5ms
  60%   below 5ms
  70%   below 5ms
  80%   below 5ms
  90%   below 6ms
  95%   below 6ms
  98%   below 7ms
  99%   below 8ms
99.9%   below 10ms
PS C:\Windows\System32>
```

#### bossGroup：8  ; workerGroup：40

```java
EventLoopGroup bossGroup = new NioEventLoopGroup(8);
EventLoopGroup workerGroup = new NioEventLoopGroup(40);
```

压测结果

```shell
PS C:\Windows\System32> sb -u http://localhost:8804 -c 40 -N 60
Starting at 2021/1/31 14:18:08
[Press C to stop the test]
348872  (RPS: 5487.5)
---------------Finished!----------------
Finished at 2021/1/31 14:19:12 (took 00:01:03.7695992)
Status 200:    348872

RPS: 5700.3 (requests/second)
Max: 200ms
Min: 4ms
Avg: 5.3ms

  50%   below 5ms
  60%   below 5ms
  70%   below 5ms
  80%   below 6ms
  90%   below 6ms
  95%   below 6ms
  98%   below 7ms
  99%   below 8ms
99.9%   below 12ms
PS C:\Windows\System32>
```

### 对比使用线程池的方式

#### newFixedThreadPool(10)

```java
ExecutorService executorService = Executors.newFixedThreadPool(
    10);
final ServerSocket serverSocket = new ServerSocket(8803);
while (true) {
    try {
        final Socket socket = serverSocket.accept();
        executorService.execute(() -> service(socket));
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

压测结果

```shell
PS C:\Windows\System32> sb -u http://localhost:8803 -c 40 -N 60
Starting at 2021/1/31 14:44:58
[Press C to stop the test]
91643   (RPS: 1438.4)
---------------Finished!----------------
Finished at 2021/1/31 14:46:02 (took 00:01:03.8874309)
Status 200:    90245
Status 303:    1398

RPS: 1497.9 (requests/second)
Max: 142ms
Min: 6ms
Avg: 25.1ms

  50%   below 23ms
  60%   below 23ms
  70%   below 23ms
  80%   below 23ms
  90%   below 27ms
  95%   below 47ms
  98%   below 47ms
  99%   below 47ms
99.9%   below 59ms
PS C:\Windows\System32>
```

#### newFixedThreadPool(20)

```java
ExecutorService executorService = Executors.newFixedThreadPool(
    20);
final ServerSocket serverSocket = new ServerSocket(8803);
while (true) {
    try {
        final Socket socket = serverSocket.accept();
        executorService.execute(() -> service(socket));
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

压测结果

```shell
PS C:\Windows\System32> sb -u http://localhost:8803 -c 40 -N 60
Starting at 2021/1/31 14:42:58
[Press C to stop the test]
169716  (RPS: 2667.5)
---------------Finished!----------------
Finished at 2021/1/31 14:44:02 (took 00:01:03.6875195)
Status 200:    168455
Status 303:    1261

RPS: 2779.1 (requests/second)
Max: 108ms
Min: 1ms
Avg: 11.9ms

  50%   below 11ms
  60%   below 11ms
  70%   below 11ms
  80%   below 13ms
  90%   below 15ms
  95%   below 22ms
  98%   below 24ms
  99%   below 26ms
99.9%   below 42ms
PS C:\Windows\System32>
```

#### newFixedThreadPool(30)

```java
ExecutorService executorService = Executors.newFixedThreadPool(
    30);
final ServerSocket serverSocket = new ServerSocket(8803);
while (true) {
    try {
        final Socket socket = serverSocket.accept();
        executorService.execute(() -> service(socket));
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

压测结果

```shell
PS C:\Windows\System32> sb -u http://localhost:8803 -c 40 -N 60
Starting at 2021/1/31 14:46:44
[Press C to stop the test]
167486  (RPS: 2630.5)
---------------Finished!----------------
Finished at 2021/1/31 14:47:48 (took 00:01:03.8187898)
Status 200:    166327
Status 303:    1159

RPS: 2739.1 (requests/second)
Max: 80ms
Min: 1ms
Avg: 10.9ms

  50%   below 10ms
  60%   below 11ms
  70%   below 11ms
  80%   below 12ms
  90%   below 15ms
  95%   below 19ms
  98%   below 23ms
  99%   below 26ms
99.9%   below 43ms
PS C:\Windows\System32>
```

### 结论

- netty的性能要远远优于线程池的方式，性能约是使用线程池方式的两倍以上。

- 当`bossGroup`的线程数固定时， 增加`workerGroup`的线程数，RPS会显著增加，但是当 `workerGroup`增加到并发连接数时，继续增加`workerGroup`的线程数量时，RPS没有太大的变化。
- 增加`bossGroup`的线程数，RPS变化不是很显著。

