package com.nxt.im.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 基于NIO实现客户端连接的主类
 *
 * @version v191204
 */
public class ClientSocket {

    private InetSocketAddress inetSocketAddress;
    private Selector selector;
    private SocketChannel socketChannel;

    @Deprecated
    public ClientSocket() throws IOException {
        this("127.0.0.1", 8000);
    }

    public ClientSocket(String ip, int port) throws IOException {
        init(ip, port);
    }

    public void init(String ip, int port) throws IOException {
        inetSocketAddress = new InetSocketAddress(ip, port);

        socketChannel = SocketChannel.open(inetSocketAddress);
        socketChannel.configureBlocking(false);

        selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ);

        /*
         * 接受服务器响应
         */
        Thread thread = new Thread(new ResponseHandler(selector));
        thread.start();

        // isConnected = true;
    }

    public void send(String str) throws IOException {
        if (str != null && str.length() > 0) {
            socketChannel.write(StandardCharsets.UTF_8.encode(str));
        }
    }

    /**
     * 向服务端发送 byteBuffer消息
     *
     * @param byteBuffer ByteBuffer对象
     * @throws IOException socketChannel发送出错便抛出
     */
    public void send(ByteBuffer byteBuffer) throws IOException {
        if (byteBuffer != null)
            socketChannel.write(byteBuffer);
    }
}
