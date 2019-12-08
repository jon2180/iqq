package com.nxt.im.server;

import java.io.IOException;

import java.net.InetSocketAddress;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import java.nio.channels.ClosedChannelException;

import java.nio.channels.SelectionKey;
import java.nio.channels.Channel;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import java.util.Iterator;
import java.util.Set;

/**
 * 关键类： Channels ServerSocketCHannel, Selector, SelectorKey
 */
public class NioServer {
  private InetSocketAddress inetSocketAddress;
  /**
   * selector
   */
  private Selector selector;

  /**
   * socket
   */
  private ServerSocketChannel serverSocketChannel;

  public NioServer(int port) {
    inetSocketAddress = new InetSocketAddress(port);
  }

  public NioServer() {
    this(8000);
  }

  public ByteBuffer encode(String str) {
    return Charset.forName("UTF-8").encode(str);
  }

  /**
   * 初始化服务
   */
  public void open() throws IOException {
    /**
     * 创建一个selector
     */
    selector = Selector.open();
    /**
     * 通过serversocketchannel 创建channel通道
     */
    serverSocketChannel = ServerSocketChannel.open();

    /**
     * 为channel 通道绑定监听端口
     */
    serverSocketChannel.bind(inetSocketAddress);

    /**
     * 设置channel为非阻塞模式
     */
    serverSocketChannel.configureBlocking(false);

    /**
     * 将channel 注册到selector上，监听连接事件
     */
    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
  }

  public void listen() {

    /**
     * 服务器启动成功
     */
    while (true) {
      /**
       * TODO: 获取可用channel数量
       */
      int readyChannels = 0;
      try {
        readyChannels = selector.select();
      } catch (IOException ioE) {
        ioE.printStackTrace();
        break;
      }

      if (readyChannels == 0) {
        continue;
      }

      /**
       * 获取所有可用channel的集合
       */
      Set<SelectionKey> selectionKeys = selector.selectedKeys();

      Iterator<SelectionKey> iterator = selectionKeys.iterator();

      while (iterator.hasNext()) {
        /**
         * selectionKey实例
         */
        SelectionKey selectionKey = iterator.next();

        /**
         * [!] 移除set中的当前的selectionKey
         */
        iterator.remove();

        /**
         * 根据就绪状态来判断相应的逻辑
         */
        if (selectionKey.isAcceptable()) {
          try {
            acceptHandler(serverSocketChannel, selector);
          } catch (IOException ioE) {
            ioE.printStackTrace();
            break;
          }
        } else if (selectionKey.isReadable()) {
          readHandler(selectionKey, selector);
        }
      }
    }
  }

  /**
   * 
   * @throws IOException
   */
  @Deprecated
  public void start() throws IOException {
    /**
     * 创建一个selector
     */
    Selector selector = Selector.open();
    /**
     * 通过serversocketchannel 创建channel通道
     */
    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

    /**
     * 为channel 通道绑定监听端口
     */
    serverSocketChannel.bind(inetSocketAddress);

    /**
     * 设置channel为非阻塞模式
     */
    serverSocketChannel.configureBlocking(false);

    /**
     * 将channel 注册到selector上，监听连接事件
     */
    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

    /**
     * 服务器启动成功
     */

    while (true) {
      /**
       * TODO: 获取可用channel数量
       */
      int readyChannels = selector.select();

      if (readyChannels == 0) {
        continue;
      }
      /**
       * 获取所有可用channel的集合
       */
      Set<SelectionKey> selectionKeys = selector.selectedKeys();

      Iterator<SelectionKey> iterator = selectionKeys.iterator();

      while (iterator.hasNext()) {
        /**
         * selectionKey实例
         */
        SelectionKey selectionKey = iterator.next();

        /**
         * [!] 移除set中的当前的selectionKey
         */
        iterator.remove();

        /**
         * 根据就绪状态来判断相应的逻辑
         */
        if (selectionKey.isAcceptable()) {
          acceptHandler(serverSocketChannel, selector);
        } else if (selectionKey.isReadable()) {
          readHandler(selectionKey, selector);
        }

      }
    }
  }

  public void acceptHandler(ServerSocketChannel serverSocketChannel, Selector selector) throws IOException {
    /**
     * 如果是接入事件，创建socketchannel
     */
    SocketChannel socketChannel = serverSocketChannel.accept();

    /**
     * 将socketChannel设置为非阻塞工作模式
     */

    socketChannel.configureBlocking(false);
    /**
     * 将channel注册到selector上，监听可读事件
     */
    socketChannel.register(selector, SelectionKey.OP_READ);

    /**
     * 回复客户端提示信息
     */
    socketChannel.write(encode("请注意隐私安全"));
  }

  public void readHandler(SelectionKey selectionKey, Selector selector) {
    /**
     * 要从selectionKey中获取到已经就绪的channel
     */
    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

    /**
     * 创建buffer
     */
    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    /**
     * 循环读取客户端请求信息
     */
    String request = "";
    try {

      while (socketChannel.read(byteBuffer) > 0) {
        /**
         * 切换buffer为读模式
         */
        byteBuffer.flip();

        /**
         * 读取buffer中的内容
         */

        request += Charset.forName("UTF-8").decode(byteBuffer);

      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
      return;
    }

    /**
     * 将channel再次注册到selector上，监听它的可读事件
     */
    try {

      socketChannel.register(selector, SelectionKey.OP_READ);
    } catch (ClosedChannelException cce) {
      cce.printStackTrace();
      return;
    }

    /**
     * 将客户端发送的请求信息，广播给其他客户端
     */
    if (request.length() > 0) {
      // 广播给其他客户端
      System.out.println("::" + request);
      this.broadcast(selector, socketChannel, request);
    }

  }

  private void broadcast(Selector selector, SocketChannel socketChannel, String request) {
    /**
     * 获取所有已介入的客户端channel
     */
    Set<SelectionKey> selectionKeys = selector.keys();

    selectionKeys.forEach(selectionKey -> {
      Channel targetChannel = selectionKey.channel();

      // 剔除发消息的客户端
      if (targetChannel instanceof SocketChannel && targetChannel != socketChannel) {
        try {
          ((SocketChannel) targetChannel).write(Charset.forName("UTF-8").encode(request));

        } catch (IOException ioe) {
          ioe.printStackTrace();
        }
      }
    });
  }

}