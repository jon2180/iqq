package com.nxt.im.client;

import java.nio.channels.Selector;
import java.io.IOException;
// import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
// import java.nio.channels.Selector;
// import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

import com.nxt.im.common.DataByteBuffer;

/**
 * 基于NIO实现的发消息的线程类
 * 
 * @version v191204
 */
public class ResponseHandler implements Runnable {
  private Selector selector;

  public ResponseHandler(Selector selector) {
    this.selector = selector;
  }

  @Override
  public void run() {

    /**
     * 循环等待新消息
     */
    while (true) {
      try {
        // TODO: 获取可用channel数量
        int readyChannels = selector.select();

        // TODO: 为什么要这样？
        if (readyChannels == 0)
          continue;

        // 获取可用channel的集合
        Set<SelectionKey> selectionKeys = selector.selectedKeys();

        // 迭代器遍历 selectionKey 的 set
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
          // selectionKey实例
          SelectionKey selectionKey = iterator.next();

          // [!] 移除set中的当前的selectionKey
          iterator.remove();

          /**
           * 根据就绪状态来判断相应的逻辑
           */

          // 如果是可读事件
          if (selectionKey.isReadable()) {
            readHandler(selectionKey, selector);
          }
        }
      } catch (Exception e) {
        break;
      }
    }
  }

  /**
   * 接受服务器响应
   */
  public void readHandler(SelectionKey selectionKey, Selector selector) {
    // 要从selectionKey中获取到已经就绪的channel
    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    // 循环读取客户端请求信息
    String response = "";
    try {
      // socketChannel.read(byteBuffer);
      // DataByteBuffer data = new DataByteBuffer(byteBuffer);
      // data.getData();
      while (socketChannel.read(byteBuffer) > 0) {
        /**
         * 切换buffer为读模式
         */
        byteBuffer.flip();

        /**
         * 读取buffer中的内容
         */

        response += Charset.forName("UTF-8").decode(byteBuffer);

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
     * 显示出信息内容
     */
    if (response.length() > 0) {
      System.out.println(response);
    }

  }
}