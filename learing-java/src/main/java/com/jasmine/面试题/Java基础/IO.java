package Java基础;

/**
 * @author : jasmineXz
 */
public class IO {
    /**
     1. BIO、NIO和AIO
     BIO（Blocking I/O）同步阻塞I/O
     这是最基本与简单的I/O操作方式，其根本特性是做完一件事再去做另一件事，一件事一定要等前一件事做完，这很符合程序员传统的顺序来开发思想，因此BIO
     模型程序开发起来较为简单，易于把握。

     NIO (New I/O) 同步非阻塞I/O
     关于NIO，国内有很多技术博客将英文翻译成No-Blocking I/O，非阻塞I/O模型 ，当然这样就与BIO形成了鲜明的特性对比。NIO本身是基于事件驱动的思想来
     实现的，其目的就是解决BIO的大并发问题，在BIO模型中，如果需要并发处理多个I/O请求，那就需要多线程来支持，NIO使用了多路复用器机制

     AIO (Asynchronous I/O) 异步非阻塞I/O
     Java AIO就是Java作为对异步IO提供支持的NIO.2

     AIO相对于NIO的区别在于，NIO需要使用者线程不停的轮询IO对象，来确定是否有数据准备好可以读了，而AIO则是在数据准备好之后，才会通知数据使用者，
     这样使用者就不需要不停地轮询了。
     */
}
