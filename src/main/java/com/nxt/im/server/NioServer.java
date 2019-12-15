package com.nxt.im.server;

import java.io.IOException;

import java.net.InetSocketAddress;

import java.nio.ByteBuffer;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Set;

/**
 * 初始化服务 关键类：Channels ServerSocketCHannel,Selector,SelectorKey
 * 
 * @version 191211
 */
public class NioServer {
  private InetSocketAddress inetSocketAddress;
  /**
   * selector
   */
  private Selector selector;

  private static HashMap<String, SocketWrapper> socketMap;
  static {
    socketMap = new HashMap<String, SocketWrapper>();
  }

  /**
   * socket
   */
  private ServerSocketChannel serverSocketChannel;

  public NioServer(int port) throws IOException {
    inetSocketAddress = new InetSocketAddress(port);

    // 创建一个selector
    selector = Selector.open();
    // 通过serversocketchannel 创建channel通道
    serverSocketChannel = ServerSocketChannel.open();
    // 为channel 通道绑定监听端口
    serverSocketChannel.bind(inetSocketAddress);
    // 设置channel为非阻塞模式
    serverSocketChannel.configureBlocking(false);
    // 将channel 注册到selector上，监听连接事件
    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

    System.out.println("Server running at " + inetSocketAddress.getAddress() + ":" + inetSocketAddress.getPort());
  }

  public NioServer() throws IOException {
    this(8000);
  }

  /**
   * @return the socketMap
   */
  public static HashMap<String, SocketWrapper> getSocketMap() {
    return socketMap;
  }

  public void listen() {

    // 服务器启动成功
    while (true) {
      // TODO: 获取可用channel数量
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

      // 获取所有可用channel的集合
      Set<SelectionKey> selectionKeys = selector.selectedKeys();

      Iterator<SelectionKey> iterator = selectionKeys.iterator();

      while (iterator.hasNext()) {
        // selectionKey实例
        SelectionKey selectionKey = iterator.next();

        // [!] 移除set中的当前的selectionKey
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

  public void acceptHandler(ServerSocketChannel serverSocketChannel, Selector selector) throws IOException {
    // 如果是接入事件，创建socketchannel
    SocketChannel socketChannel = serverSocketChannel.accept();

    // 将socketChannel设置为非阻塞工作模式
    socketChannel.configureBlocking(false);

    // 将channel注册到selector上，监听可读事件
    socketChannel.register(selector, SelectionKey.OP_READ);

    // 回复客户端提示信息
    socketChannel.write(Message.encode("请注意隐私安全"));
  }

  /**
   * 处理可读事件
   * 
   * @param selectionKey
   * @param selector
   */
  public void readHandler(SelectionKey selectionKey, Selector selector) {
    // 要从selectionKey中获取到已经就绪的channel
    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

    // 创建buffer
    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    try {
      socketChannel.read(byteBuffer);
      Router.dispatch(socketChannel, byteBuffer);
    } catch (IOException ioE) {
      // ioE.printStackTrace();
      // return;
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
  }
}