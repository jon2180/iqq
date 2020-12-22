# SocketChannel---各种注意点

![](https://csdnimg.cn/release/phoenix/template/new_img/reprint.png)

[billluffy](https://me.csdn.net/billluffy) 2017-09-20 09:31:18 ![](https://csdnimg.cn/release/phoenix/template/new_img/articleRead.png) 14262 ![](https://csdnimg.cn/release/phoenix/template/new_img/collect.png) ![](https://csdnimg.cn/release/phoenix/template/new_img/tobarCollectionActive.png) 收藏  9 

最后发布:2017-09-20 09:31:18首发:2017-09-20 09:31:18

找问题的时后发现了这篇文章，惊为天人，几乎涵盖了我所有碰到的坑，非常不错！

不得不说，NIO的API设计的够难用的，坑还巨多....这也是为什么大家都不直接使用nio的原因吧，一般会用mina或者netty啥的（这是个记录的博客，所以会不断更新）

关于通道本身的一些注意点，请参考我之前的：[nio通道(2)---几个注意点](http://www.360doc.com/content/12/0822/00/495229_231622420.shtml)

其他一些参考[nio summary](http://www.dazhuangzhuang.com/?p=144)

# 1 坑爹的事件

SocetChannel和ServerSocketChannel各自支持的事件，在前面已经提到：

[http://www.360doc.com/content/12/0902/17/495229\_233773276.shtml](http://www.360doc.com/content/12/0902/17/495229_233773276.shtml)

## 1.1 什么时候可以register

**客户端：**
linux: SocketChannel: 注册了op\_read,op\_write,op\_connect的SocketChannel**在connect之前，open之后，都可以select到，只不过不能够read和write**  
windows:SocketChannel:只有注册了op\_connect的SocketChannel**在connect之后，才被select到。  是一个正确的符合逻辑的理解**。

**服务器端：**

(2)如果只注册了读的操作，则select时，会发生阻塞，因为是没有为读准备好的socket  

(3)如果没有可写的socket，则select时，不会发生阻塞，直接返回0。如果阻塞写，只能是发送区满。

另外：iterator到selectedKey之后，需要将该key移除出selectedKey。如果不移出，例如OP\_ACCEPT，则再下次accept之后，会产生空的SocketChannel。

## 1.2 connect事件（连接\--成功or失败？）

在之前的Socket通道中，已经看到，非阻塞模式下，connect操作会返回false，后面会发出CONNECT事件来表示连接，但是这里其实没有区分成功还是失败。。

connect事件：表示连接通道连接就绪或者发生了错误，会被加到ready 集合中（下面面是API说明）

If the selector detects that the corresponding socket channel is ready to complete its connection sequence, or has an error pending, then it will add OP\_CONNECT to the key's ready set and add the key to its selected-key set.

所以这个事件发生的时候不能简单呢的认为连接成功，要使用finishConnect判断下，如果连接失败，会抛出异常

[NIO就绪处理之OP\_CONNECT](http://blog.csdn.net/zhouhl_cn/article/details/6568893)  
  
```java
    if (key.isValid() && key.isConnectable()) {
        SocketChannel ch = (SocketChannel) key.channel();

        if (ch.finishConnect()) {

        // Connect successfully

        // key.interestOps(SelectionKey.OP\_READ);

        } else {

        // Connect failed

        }
    }
```

## 1.3 read&关闭

一直很奇怪，为啥没有close事件，终于在一次实验的时候发现：

1.启动一个客户端和服务端

2.关闭客户端，服务端会发生一个read事件，并且在read的时候抛出异常，来表示关闭

![](http://userimage3.360doc.com/12/0902/22/495229_201209022226160388.jpg)

另外，这个事件会不断发生，就算从已准备好的集合移除也没有，必须将该channel关闭或者调用哪个该key的cancel方法，因为**SelectionKey代表的是Selector和Channel之间的联系，所以在Channel关闭了之后，对于Selector来说，这个Channel永远都会发出关闭这个事件，表明自己关闭了，直到从该Selector移除去**

3.服务端关闭，client端在write的时候会抛出异常

java.io.IOException: 远程主机强迫关闭了一个现有的连接。

 at sun.nio.ch.SocketDispatcher.write0(Native Method)

 at sun.nio.ch.SocketDispatcher.write(Unknown Source)

## 1.4 还是关闭(TIME\_WAIT)

[NIO的SelectableChannel关闭的一个问题](http://dennis-zane.iteye.com/blog/204969)

如果在取消SelectionKey（这时候只是加入取消的键集合，下一次select才会执行）后没有调用到selector的select方法（因为Client一般在取消key后，  我们都会终止调用select的循环，当然，server关闭一个注册的channel我们是不会终止select循环的），那么本地socket将进入CLOSE-WAIT  状态（等待本地Socket关闭）。简单的解决办法是在 SelectableChannel.close方法之后调用Selector.selectNow方法

**Netty在超过256连接关闭的时候主动调用一次selectNow**

## 1.5 write

[NIO就绪处理之OP\_WRITE](http://blog.csdn.net/zhouhl_cn/article/details/6582435)

一开始很多人以为write事件，表示在调用channel的write方法之后，就会发生这个事件，然后channel再会把数据真正写出，但是实际上，**写操作的就绪条件为底层缓冲区有空闲空间，而写缓冲区绝大部分时间都是有空闲空间的，所以当你注册写事件后，写操作一直是就绪的，选择处理线程全占用整个CPU资源。所以，只有当你确实有数据要写时再注册写操作，并在写完以后马上取消注册，**一般的，Client端需要注册OP\_CONNECT,OP\_READ;Server端需要注册OP\_ACCEPT并且连接之后注册OP\_READ

当有数据在写时，将数据写到缓冲区中，并注册写事件。

\[java\]  [view plain](http://blog.csdn.net/zhouhl_cn/article/details/6582435#)[copy](http://blog.csdn.net/zhouhl_cn/article/details/6582435#)

1 public  void write(byte\[\] data) throws IOException {

2 writeBuffer.put(data);

3 key.interestOps(SelectionKey.OP\_WRITE);

4 }

  
注册写事件后，写操作就绪，这时将之前写入缓冲区的数据写入通道，并取消注册。\[java\]  [view plain](http://blog.csdn.net/zhouhl_cn/article/details/6582435#)[copy](http://blog.csdn.net/zhouhl_cn/article/details/6582435#)

5 channel.write(writeBuffer);

6 key.interestOps(key.interestOps() & ~SelectionKey.OP\_WRITE);

  
大部分情况下，其实直接用write方法写就好了，没必要用写事件。

另外，关于write还可以参考下面的4.2的一些注意点

# 2 Channel的bind方法

**Socket/ServerSocket**

两者都有bind方法，表示绑定到某个端口，在绑定之后，**前者调用connect方法，表示连接到某个服务端；后者要在后面调用accept方法，监听到来的连接请求（一个Socket句柄包含了两个地址对，本地ip:port----远程ip:port）**

# 3 Selector的select和wakeup机制

[Java NIO类库Selector机制解析（上）](http://blog.csdn.net/haoel/article/details/2224055)  
[Java NIO类库Selector机制解析（下）](http://blog.csdn.net/haoel/article/details/2224069)  
[Java NIO 类库Selector机制解析（续）](http://blog.csdn.net/haoel/article/details/2379586)

在windows平台下，调用Selector.open()方法，会自己和自己建立两条TCP连接，消耗了两个TCP连接和端口，也消耗了文件描述符

在linux平台下，会自己和自己建立两条管道，消耗了两个系统的文件描述符

![](http://userimage3.360doc.com/12/0902/22/495229_201209022226160919.jpg)

目的

一个阻塞seelct上的线程想要被唤醒，有3种方式：

1.有数据可读/.可写，或者出现异常

2.阻塞时间到，time out

**3.收到一个non-block信号，由kill或者pthread\_kill发出**

  

所以Select.wakeup方法也只能通过这三种方法，可以排除2，也可以排除3，因为windows上没有这种机制。因此只有第一种方法；再回想到为什么每个Selector.open()，在Windows会建立一对自己和自己的loopback的TCP连接；在Linux上会开一对pipe（pipe在Linux下一般都是成对打开），估计我们能够猜得出来——那就是如果想要唤醒select，只需要朝着自己的这个loopback连接发点数据过去，于是，就可以唤醒阻塞在select上的线程了。

这两个方法完全是来模仿Linux中的的kill和pthread\_kill给阻塞在select上的线程发信号的。但因为发信号这个东西并不是一个跨平台的标准（pthread\_kill这个系统调用也不是所有Unix/Linux都支持的），而pipe是所有的Unix/Linux所支持的，但Windows又不支持，所以，Windows用了TCP连接来实现这个事。(**在Linux下使用pipe管道**)

# 4 一些特殊情况

## 4.1 读不满

因为我们的数据都是偏业务性的，比如使用开头一个字节来表示后面数据的长度，接着就会等待读取到那么多数据，但是TCP是流式的协议，100字节的数据可能是一段段发送过来的，所以在没有读到完整的数据前需要等待。

这时候可以将buffer attach到key上，下次read发生的时候再继续读取，但是也有另外一种说法，在网络条件比较好的情况下，直接使用一个临时selector会减少上下文切换。。这个不太明白

## 4.2 写不出去

在发送缓冲区空间不够的情况下，write方法可能会返回能够写出去的字节数，比如只剩50字节，你写入100字节，这时候write会返回50，即往缓冲区写入了50字节

在网络较好的情况下，这应该是不太可能发生的，一般都是网络有问题，重传率很高

详细的情况可以参考：[java nio对OP\_WRITE的处理解决网速慢的连接](http://blog.sina.com.cn/s/blog_783ede0301013g5n.html)

while (bb.hasRemaining()) {

 int len = socketChannel.write(bb);

 if (len < 0) {

 throw new EOFException();

 }

}

由于缓冲区一直蛮，下面的代码会一直执行，占用CPU100%，因此推荐的方式如下

while (bb.hasRemaining()) {

 int len = socketChannel.write(bb);

 if (len < 0){

 throw new EOFException();

 }

 **if (len == 0) {**

 selectionKey.interestOps(

 selectionKey.interestOps() | SelectionKey.OP\_WRITE);

 mainSelector.wakeup();

 break;

 }

}

如果返回0，表示缓冲区满，那么注册WRITE事件，缓冲区不满的情况下，就会触发WRITE事件，在那时候再写入，可以避免不要的消耗。（另外Grizzly还是用了另一种方式，也可以从上面的参考链接得到）