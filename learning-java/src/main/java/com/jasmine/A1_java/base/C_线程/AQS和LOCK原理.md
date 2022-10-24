# <center>AQS/LOCK/线程池</center>
# 一. AbstractQueuedSynchronizer(AQS)

即队列同步器

它是构建锁或者其他同步组件的基础框架（如ReentrantLock、ReentrantReadWriteLock、Semaphore等），JUC并发包的作者（Doug Lea）期望它能够成为实现大部分同步需求的基础。它是JUC并发包中的核心基础组件。

AQS定义两种资源共享方式：

1. Exclusive（独占，只有一个线程能执行，如ReentrantLock）
2. Share（共享，多个线程可同时执行，如Semaphore/CountDownLatch）。

## 1. 主要对象

### 1.1 state

表示同步状态, 在可重入锁中也表示重入次数

- 以ReentrantLock为例，state初始化为0，表示未锁定状态。A线程lock()时，会调用tryAcquire()独占该锁并将state+1。此后，其他线程再tryAcquire()时就会失败，直到A线程unlock()到state=0（即释放锁）为止，其它线程才有机会获取该锁。当然，释放锁之前，A线程自己是可以重复获取此锁的（state会累加），这就是可重入的概念。但要注意，获取多少次就要释放多么次，这样才能保证state是能回到零态的。

- 再以CountDownLatch以例，任务分为N个子线程去执行，state也初始化为N（注意N要与线程个数一致）。这N个子线程是并行执行的，每个子线程执行完后countDown()一次，state会CAS减1。等到所有子线程都执行完后(即state=0)，会unpark()主调用线程，然后主调用线程就会从await()函数返回，继续后余动作。

  1. getState()：返回同步状态的当前值；
  2. setState(int newState)：设置当前同步状态；
    3. compareAndSetState()

### 1.2 Node 队列节点

```java
static final class Node {
  // 共享
  static final Node SHARED = new Node();
  // 独占
  static final Node EXCLUSIVE = null;
  static final int CANCELLED =  1;
  static final int SIGNAL    = -1;
  static final int CONDITION = -2;
  static final int PROPAGATE = -3;
  volatile     int waitStatus; // 等待状态,会将值赋值为上面几个状态 [1,-1,-2,-3]

  volatile Node prev;      // 前驱节点
  volatile Node next;      // 后继节点
  volatile Thread thread;  // 获取同步状态的线程
  Node nextWaiter;    
  final boolean isShared() {        
    return nextWaiter == SHARED;    
  }    
  final Node predecessor() throws NullPointerException {        
    Node p = prev;        
    if (p == null)            
      throw new NullPointerException();        
    else            
    return p;    
  }    
  Node() {    }    
  Node(Thread thread, Node mode) {        
    this.nextWaiter = mode;        
    this.thread = thread;    
  }    
  Node(Thread thread, int waitStatus) {        
    this.waitStatus = waitStatus;        
    this.thread = thread;    
  }
}
```

##### 1.2.1 属性

- waitStatus : 节点状态

  | 节点状态      |  值  | 说明                                                         |
  | :------------ | :--: | :----------------------------------------------------------- |
  | 0             |  0   | 新结点入队时的默认状态。                                     |
  | **CANCELLED** |  1   | 表示当前结点已取消调度。当timeout或被中断（响应中断的情况下），会触发变更为此状态，进入该状态后的结点将不会再变化。 |
  | **SIGNAL**    |  -1  | 表示后继结点在等待当前结点唤醒。后继结点入队时，会将前继结点的状态更新为SIGNAL。 |
  | **CONDITION** |  -2  | 表示结点等待在Condition上，当其他线程调用了Condition的signal()方法后，CONDITION状态的结点将**从等待队列转移到同步队列中**，等待获取同步锁。 |
  | **PROPAGATE** |  -3  | 共享模式下，前继结点不仅会唤醒其后继结点，同时也可能会唤醒后继的后继结点。 |

  > 负值表示结点处于有效等待状态，而正值表示结点已被取消。所以源码中很多地方用 >0、<0 来判断结点的状态是否正常

- nextWaiter
- volatile Node prev : 元素的上一个节点
- volatile Node next : 元素的下一个节点
- volatile Thread thread : 等待的线程
- Node nextWaiter : 状态

##### 1.2.2 方法

- final Node predecessor() : 获取前一节点

  ```java
  Node p = prev;
  return p;
  ```

  

### 1.3 CLH 同步队列

CLH同步队列是一个FIFO双向队列，AQS依赖它来完成同步状态的管理，当前线程如果获取同步状态失败时，AQS则会将当前线程已经等待状态等信息构造成一个节点（Node）并将其加入到CLH同步队列，同时会阻塞当前线程，当同步状态释放时，会把首节点唤醒（公平锁），使其再次尝试获取同步状态。

![avatar](https://xiaozei-bucket.oss-cn-hangzhou.aliyuncs.com/xiaozei/blog/other/7d6b06d1092f43119a967cae6b2ac06c.jpg)

##### 1.3.1 重要属性

```java
/**
 * 等待队列的头，惰性初始化。除了初始化之外，它只通过setHead方法进行修改。注意:如果head存在，它的等待状态保证不会被取消。
 */
private transient volatile Node head;

/**
 * 等待队列的尾部，延迟初始化。仅通过方法enq修改以添加新的等待节点。
 */
private transient volatile Node tail;
```

##### 1.3.2 入列
未获取到锁的线程进入等待队列尾端

学了数据结构的我们，CLH队列入列是再简单不过了，无非就是tail指向新节点、新节点的prev指向当前最后的节点，当前最后一个节点的next指向当前节点。代码我们可以看看addWaiter(Node node)方法：

```java
/**
 * 为当前线程和给定模式创建并对节点进行排队。
 *
 * @param mode : static final Node EXCLUSIVE = null;
 * @return the new node
 */
private Node addWaiter(Node mode) {        
  /*
  新建Node
  thread : 线程为当前线程
  mode   : nextWaiter = null
  */
  Node node = new Node(Thread.currentThread(), mode);        
  // 当前队列尾节点变为上一节点
  Node pred = tail;
  // 如果上一节点(尾节点)不为空
  if (pred != null) {
    // 当前节点的上一节点变为曾经的尾节点
    node.prev = pred;            
    // CAS设置尾节点            
    if (compareAndSetTail(pred, node)) {
      // 上一节点的下一节点为当前节点,即新增的节点
      // 新增的节点即为尾节点
      pred.next = node;                
      return node;            
    }
  }
  //多次尝试        
  enq(node);
  return node;
}
```
addWaiter(Node node)先通过快速尝试设置尾节点，如果失败，则调用enq(Node node)方法设置尾节点
```java
private Node enq(final Node node) {
  // 直到成功为止
  for (;;) {
    Node t = tail;
    // 尾节点不存在，则 [尾 = 首]
    if (t == null) {
      if (compareAndSetHead(new Node()))
        tail = head;
    // 尾节点存在,则节点的上一节点变为尾,尾的下一节点变为本节点.
    } else {
      // 设置为尾节点
      node.prev = t;
      if (compareAndSetTail(t, node)) {
        t.next = node;
        return t;
      }
    }
  }
}
```
如下图,节点添加到尾节点中

![avatar](https://xiaozei-bucket.oss-cn-hangzhou.aliyuncs.com/xiaozei/blog/other/1fae36ad19b247bf936c5a503f9f090d.jpg)

##### 1.3.3 出列
CLH同步队列遵循FIFO，首节点的线程释放同步状态后，将会唤醒它的后继节点（next），而后继节点将会在获取同步状态成功时将自己设置为首节点，这个过程非常简单，head执行该节点并断开原首节点的next和当前节点的prev即可，注意在这个过程是不需要使用CAS来保证的，因为只有一个线程能够成功获取到同步状态。



---



## 2 主要方法

| 方法名                | 说明                                                         |
| --------------------- | ------------------------------------------------------------ |
| isHeldExclusively()   | 该线程是否正在独占资源。只有用到condition才需要去实现它。    |
| tryAcquire(int arg)   | 独占方式,尝试获取资源，成功则返回true，失败则返回false。     |
| tryRelease(int arg)   | 独占方式,尝试释放资源，成功则返回true，失败则返回false。     |
| tryAcquireShared(int) | 共享方式。尝试获取资源。负数表示失败；0表示成功，但没有剩余可用资源；正数表示成功，且有剩余资源。 |
| tryReleaseShared(int) | 共享方式。尝试释放资源，如果释放后允许唤醒后续等待结点返回true，否则返回false。 |

**一般来说，自定义同步器要么是独占方法，要么是共享方式，他们也只需实现tryAcquire-tryRelease、tryAcquireShared-tryReleaseShared中的一种即可。但AQS也支持自定义同步器同时实现独占和共享两种方式，如ReentrantReadWriteLock。**

### 2.1 获取锁 acquire(int arg)

**由子类调用**

- @param arg 把获得参数设为参数。这个值被传递给 {@link #tryAcquire}，但是没有被解释，可以表示任何你喜欢的东西。


```java
/**
 * 以排除模式获取，忽略中断。通过至少调用一次{@link #tryAcquire}来实现，成功后返回。
 * 否则，线程将排队，可能会重复阻塞和取消阻塞，调用{@link #tryAcquire}直到成功。
 * 此方法可用于实现方法{@link Lock# Lock}。
 */
public final void acquire(int arg) {
    if (!tryAcquire(arg) && acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
        selfInterrupt();
}
```

- **tryAcquire** (见 2.2) : 去尝试获取锁，获取成功则设置锁状态并返回true，否则返回false。子类实现，该方法必须要保证线程安全的获取同步状态。

- **addWaiter** (见 1.3.2): 如果 **[ tryAcquire ]** 返回 **[ false ]**，则调用该方法将当前线程加入到CLH同步队列尾部，并标记为独占模式；

- **acquireQueued** (见 二.4)：使线程阻塞在等待队列中获取资源，一直获取到资源后才返回。如果在整个等待过程中被中断过，则返回true，否则返回false。

- selfInterrupt：产生一个中断。

### 2.2 acquireQueued

方法为一个自旋的过程，也就是说当前线程（Node）进入同步队列后，就会进入一个自旋的过程，每个节点都会自省地观察，当条件满足，获取到同步状态后，就可以从这个自旋过程中退出，否则会一直执行下去。

如果当前线程获取锁失败,会把当前线程放入队列中,然后进入此方法
```java
final boolean acquireQueued(final Node node, int arg) {
    // 是否失败,默认失败
    boolean failed = true;
    try {
        // 是否被打断
        boolean interrupted = false;
        /*
        * 自旋过程，其实就是一个死循环而已
        */
        for (;;) {
            // 获得该节点的前节点
            final Node p = node.predecessor();
            // 如果前一个节点为队列头部,并且当前线程再次获取锁成功
            if (p == head && tryAcquire(arg)) {
                // 则头部为自己
                setHead(node);
                p.next = null; // help GC , 原来的头结点的下一个节点为null,即释放前一个锁
                failed = false;    // 成功
                return interrupted;// return false
            }
            if (shouldParkAfterFailedAcquire(p, node) && parkAndCheckInterrupt())
                interrupted = true;
        }
    } finally {
        if (failed)
            cancelAcquire(node);
    }
}
```



```java
private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
    int ws = pred.waitStatus;
    if (ws == Node.SIGNAL)
        /*
         * This node has already set status asking a release
         * to signal it, so it can safely park.
         */
        return true;
    if (ws > 0) {
        /*
         * Predecessor was cancelled. Skip over predecessors and
         * indicate retry.
         */
        do {
            node.prev = pred = pred.prev;
        } while (pred.waitStatus > 0);
        pred.next = node;
    } else {
        /*
         * waitStatus must be 0 or PROPAGATE.  Indicate that we
         * need a signal, but don't park yet.  Caller will need to
         * retry to make sure it cannot acquire before parking.
         */
        compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
    }
    return false;
}
```



---

---

# 二. Lock实现 

## ReentrantLock 可重入锁 -  公平锁 

FairSync

### 1 、加锁 Lock

```java
/** 加锁 */
final void lock() {
    acquire(1);
}
```

###  2  、AQS#acquire

```java
public final void acquire(int arg) { // arg = 1
    // 尝试获取锁失败 && 当前线程被中断
    if (!tryAcquire(arg) && acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
        selfInterrupt();// 中断线程
}
```

### 3 、子类实现 tryAcquire

子类实现尝试加锁方法 FairSync#tryAcquire(int acquires)

```java
/** 公平版的尝试获得。不要授予访问权限，除非递归调用或没有等待者或第一个等待者 */
protected final boolean tryAcquire(int acquires) { // arg = 1
    final Thread current = Thread.currentThread();
    // 获取当前状态AQS状态
    int c = getState();
    // 如果没有等待的线程,则加锁
    if (c == 0) {
        // 当前线程前没有等待的线程 && 当前线程设置state成功,则获取到锁
        if (!hasQueuedPredecessors() && compareAndSetState(0, acquires)) {
            setExclusiveOwnerThread(current); // 设置独占线程
            return true;
        }
    // 如果当前获得锁的线程是自己,则重入
    } else if (current == getExclusiveOwnerThread()) {
        int nextc = c + acquires;
        if (nextc < 0)
            throw new Error("Maximum lock count exceeded");
        setState(nextc); // state 保存重入次数
        return true;
    }
    return false;
}


/**
 * 判断当前线程前没有等待的线程
 * true  : 如果在当前线程之前有一个排队的线程
 * false : 如果当前线程位于队列的头部或队列为空
 */
public final boolean hasQueuedPredecessors() {
    Node t = tail; // 以相反的初始化顺序读取字段
    Node h = head;
    Node s;
    // 头部 != 尾部 && (头部的下一节点为null || 当前线程不等于头部的后继节点线程)
    return h != t && ((s = h.next) == null || s.thread != Thread.currentThread());
}
```

### 4 、AQS#acquireQueued

进入AQS实现的acquireQueued（自旋获取锁）

如果当前线程获取锁失败,会把当前线程放入队列中,然后进入此方法,线程进入此方法后,会自旋获取前一节点

```java
final Node p = node.predecessor();
```

如果前一节点是头结点,自己则尝试加锁

方法为一个自旋的过程，也就是说当前线程（Node）进入同步队列后，就会进入一个自旋的过程，每个节点都会自省地观察，当条件满足，获取到同步状态后，就可以从这个自旋过程中退出，否则会一直执行下去。

```java
/**
 * 以独占锁模式获取队列中已经存在的线程,使用条件等待方法以及获取。
 *
 * @param node : 当前线程添加到队列尾的节点
 * @param arg  : 1
 * @return {@code true} if interrupted while waiting
 */
final boolean acquireQueued(final Node node, int arg) {
    // 是否失败,默认失败
    boolean failed = true;
    try {
        // 是否被打断
        boolean interrupted = false;
        /* =================================================
        进入此节点的线程开始自旋尝试获取加锁状态
        ====================================================*/
        for (;;) {
            // 获得该节点的前节点, 见 [1.2.2]
            final Node p = node.predecessor();
            // 如果当前节点的前节点为队列头部,并且当前线程再次获取锁成功,则自己为获取锁状态,结束自旋
            if (p == head && tryAcquire(arg)) {
                setHead(node);     // 则头部为自己
                p.next = null; 	   // 原来的头结点的下一个节点为null,即释放前一个锁
                failed = false;    // 成功获取资源
                return interrupted;// 返回等待过程中是否被中断过
            }
            // 如果自己可以休息了，就通过park()进入waiting状态，直到被unpark()。
            // 如果不可中断的情况下被中断了，那么会从park()中醒过来，发现拿不到资源，从而继续进入park()等待。
            // 自己可以等待 && 自己等待成功了
            if (shouldParkAfterFailedAcquire(p, node) && parkAndCheckInterrupt())
                interrupted = true;
        }
    } finally {
        // 如果出现异常,且没有获取到,则取消正在进行的获取尝试。
        if (failed)
            cancelAcquire(node);
    }
}
```



```java
/**
 * 进入此方法则证明有前驱节点,且前驱节点不为头节点
 * 整个流程中，如果前驱结点的状态不是SIGNAL，那么自己就不能安心去休息，需要去将自己放到 [SIGNAL] 的节点后，同时可以再尝试下看有没有机会轮到自己拿号。
 * 
 * @param pred : 当前节点的前驱节点
 * @param node : 当前节点
 * @return {@code true} 当前线程需要阻塞
 */
private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
    int ws = pred.waitStatus;//拿到前驱的状态
    // 1. 如果前驱节点释放后会自己 (= -1), 那自己就可以安心等待了
    if (ws == Node.SIGNAL)
        return true;
    
    // 2. 如果前驱节点放弃了,那就找一个不放弃的前驱节点.
    if (ws > 0) {
        /*
		* 如果前驱放弃了，那就一直往前找，直到找到最近一个正常等待的状态，并排在它的后边。
		* 注意：那些放弃的结点，由于被自己插到它们前边，它们相当于形成一个无引用链，稍后就会被GC回收
		*/
        do {
            node.prev = pred = pred.prev;// 自己的前驱 = 前驱的前驱
        } while (pred.waitStatus > 0);
        pred.next = node; // 前驱的前驱的后继节点等于自己,则前驱不在链表中,如下图
        
    // 3. 如果前驱节点是其他状态,就设置为 Node.SIGNAL(-1)
    } else {
        // 如果前驱正常，那就把前驱的状态设置成SIGNAL，告诉它释放后通知自己一下。有可能失败，人家说不定刚刚释放完呢！
        compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
    }
    return false;
}
```

![AQS#shouldParkAfterFailedAcquire逻辑.png](https://xiaozei-bucket.oss-cn-hangzhou.aliyuncs.com/xiaozei/blog/java/AQS%23shouldParkAfterFailedAcquire%E9%80%BB%E8%BE%91%20.png)

```java
/**
 * 将自己设置为等待状态
 */
private final boolean parkAndCheckInterrupt() {
     LockSupport.park(this);//调用park()使线程进入waiting状态
     return Thread.interrupted();//如果被唤醒，查看自己是不是被中断的。
}
```

park()会让当前线程进入waiting状态。在此状态下，有两种途径可以唤醒该线程：1）被unpark()；2）被interrupt()。需要注意的是，Thread.interrupted()会清除当前线程的中断标记位。 

### 5 、AQS#cancelAcquire

```java
/**
 * 取消正在进行的获取尝试。
 *
 * @param node : 当前节点
 */
private void cancelAcquire(Node node) {
    // Ignore if node doesn't exist
    if (node == null)
        return;

    node.thread = null;

    // 跳过退出的前驱节点
    Node pred = node.prev;
    while (pred.waitStatus > 0)
        node.prev = pred = pred.prev;

    // predNext is the apparent node to unsplice. CASes below will
    // fail if not, in which case, we lost race vs another cancel
    // or signal, so no further action is necessary.
    Node predNext = pred.next;

    // Can use unconditional write instead of CAS here.
    // After this atomic step, other Nodes can skip past us.
    // Before, we are free of interference from other threads.
    node.waitStatus = Node.CANCELLED;

    // If we are the tail, remove ourselves.
    if (node == tail && compareAndSetTail(node, pred)) {
        compareAndSetNext(pred, predNext, null);
    } else {
        // If successor needs signal, try to set pred's next-link
        // so it will get one. Otherwise wake it up to propagate.
        int ws;
        if (pred != head &&
            ((ws = pred.waitStatus) == Node.SIGNAL ||
             (ws <= 0 && compareAndSetWaitStatus(pred, ws, Node.SIGNAL))) &&
            pred.thread != null) {
            Node next = node.next;
            if (next != null && next.waitStatus <= 0)
                compareAndSetNext(pred, predNext, next);
        } else {
            unparkSuccessor(node);
        }

        node.next = node; // help GC
    }
```

### 6、总结

1. 调用自定义同步器的tryAcquire()尝试直接去获取资源，如果成功则直接返回；
2. 没成功，则addWaiter()将该线程加入等待队列的尾部，并标记为独占模式；
3. acquireQueued()使线程在等待队列中休息，有机会时（轮到自己，会被unpark()）会去尝试获取资源。获取到资源后才返回。如果在整个等待过程中被中断过，则返回true，否则返回false。
4. 如果线程在等待过程中被中断过，它是不响应的。只是获取资源后才再进行自我中断selfInterrupt()，将中断补上。

![AQS独占锁加锁过程](https://xiaozei-bucket.oss-cn-hangzhou.aliyuncs.com/xiaozei/blog/java/AQS%E7%8B%AC%E5%8D%A0%E9%94%81%E5%8A%A0%E9%94%81%E6%B5%81%E7%A8%8B.png)

---

### ──────────────────────

### 1、释放锁 unLock

```java
/** 释放锁 */
public void unlock() {
    sync.release(1);
}
```

### 2、AQS#release

```java
public final boolean release(int arg) {
    if (tryRelease(arg)) {
        Node h = head; // 找到头节点
        if (h != null && h.waitStatus != 0)
            unparkSuccessor(h);// 唤醒等待队列里的下一个线程
        return true;
    }
    return false;
}
```

### 3、子类实现 tryRelease

需要是注意,尝试释放锁是实现类内部抽象类 **[Sync]** 中实现, 而不是 **[Sync]** 的具体实现类 **[NonfairSync]** 或 **[FairSync]** 实现

```java
/** 
 * 尝试释放锁
 * @see Sync#tryRelease
 */
protected final boolean tryRelease(int releases) {
    // 获取加锁状态 - 1
    int c = getState() - releases;
    // 线程不是当前独占线程要报错
    if (Thread.currentThread() != getExclusiveOwnerThread())
        throw new IllegalMonitorStateException();
    // 初始化释放状态为 false
    boolean free = false;
    // 如果加锁状态 - 1 = 0 ,则说明当前线程没有获取加锁状态,则设置当前独占线程为null,并确认释放
    if (c == 0) {
        free = true;
        setExclusiveOwnerThread(null);
    }
    setState(c); // 设置加锁状态为 0 
    return free; // 返回是否释放
}
```

### 4、通知其他线程获取锁 (unparkSuccessor )

```java
/** 唤醒节点的后继节点(如果存在的话)。 */
private void unparkSuccessor(Node node) {
    /*
     * 头节点的状态, 
     * Node h = head;
     * unparkSuccessor(h);
     */
    int ws = node.waitStatus;
    if (ws < 0)
        // 置零当前线程所在的结点状态，允许失败。
        compareAndSetWaitStatus(node, ws, 0);

    /*
     * Thread to unpark 保存在后续节点中，它通常只是下一个节点。
     * 但是，后继节点的获取状态可为取消, ( 即: waitStatus = CANCELLED = 1 )
     * 如果取消或为空，则从tail向前遍历，以找到实际的非取消后继节点.
     */
    // 找到下一个需要唤醒的结点 s
    Node s = node.next;
    // 如果下一节点为空或等待状态为取消
    if (s == null || s.waitStatus > 0) {
        s = null;
        // 从尾部向前遍历直到找到一个等待状态 <= 0 的节点
        for (Node t = tail; t != null && t != node; t = t.prev)
            if (t.waitStatus <= 0)
                s = t;
    }
    // 如果存在后继节点,则将后继唤醒
    if (s != null)
        LockSupport.unpark(s.thread);
}
```



### 5、LockSupport.unpark(s.thread)

```java
public static void unpark(Thread thread) {
    if (thread != null)
        UNSAFE.unpark(thread);
}
```





## ReentrantLock 可重入锁 -  非公平锁 

NonfairSync

### 1 、加锁 Lock

```java
// 加锁
final void lock() {
    // 非公平锁进入就尝试加锁
    if (compareAndSetState(0, 1))
        // 获取到锁则设置独占线程为当前线程
        setExclusiveOwnerThread(Thread.currentThread());
    // 没获取到锁,则进入尝试加锁阶段 (见:2.1 获取锁)
    else
        acquire(1);
}
```



### 2  、AQS#acquire

和公平锁一样

```java
public final void acquire(int arg) { // arg = 1
    // 尝试获取锁失败 && 当前线程被中断
    if (!tryAcquire(arg) && acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
        selfInterrupt();// 中断线程
}
```



### 3 、子类实现 tryAcquire

实际调用是 **nonfairTryAcquire**

```java
protected final boolean tryAcquire(int acquires) {
    return nonfairTryAcquire(acquires);
}

/**
 * 执行块tryLock。tryAcquire是在子类中实现的，但两者都需要对trylock方法进行不公平的尝试。
 */
final boolean nonfairTryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
    // 获取当前加锁状态
    int c = getState();
    if (c == 0) {
        /**==========================================================================================
         *                                             不同点
         * ==========================================================================================
         * 直接就尝试加锁,非公平锁会判断是否有前驱节点在等待加锁
         * 非公平锁 : if (!hasQueuedPredecessors() && compareAndSetState(0, acquires)) {
         ==========================================================================================*/ 
        if (compareAndSetState(0, acquires)) {
            // 如果加锁成功,则设置独占线程为当前线程,然后返回进入到selfInterrupt
            setExclusiveOwnerThread(current);
            return true;
        }
    }
    // 如果当前获取锁的线程为自己,则可重入,重入次数加1
    else if (current == getExclusiveOwnerThread()) {
        int nextc = c + acquires;
        if (nextc < 0)
            throw new Error("Maximum lock count exceeded");
        setState(nextc);
        return true;
    }
    return false;
}
```

