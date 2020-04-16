package me.iqq.client.socket;

import me.iqq.common.protocol.DataWrapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 基于NIO实现客户端连接的主类
 *
 * @version v191204
 */
public class ClientSocket {

    private InetSocketAddress inetSocketAddress;
    private Selector selector;
    private SocketChannel socketChannel;
    private Thread receiverThread;

    public ClientSocket(String ip, int port, Router router) throws IOException {

        inetSocketAddress = new InetSocketAddress(ip, port);

        socketChannel = SocketChannel.open(inetSocketAddress);
        socketChannel.configureBlocking(false);

        selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ);

        // 接受服务器响应
        receiverThread = new ReceiveThread(selector, router);
    }

    public InetSocketAddress getInetSocketAddress() {
        return inetSocketAddress;
    }

    public Selector getSelector() {
        return selector;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public Thread getReceiverThread() {
        return receiverThread;
    }

    public class ReceiveThread extends Thread {
        private Router router;
        private Selector selector;

        public ReceiveThread(Selector initialSelector, Router iRouter) {
            router = iRouter;
            selector = initialSelector;
        }

        @Override
        public void run() {
            System.out.println("Receiver thread has started");
            // 循环等待新消息
            while (true) {
                try {
                    // 获取可用channel数量，避免进行下列无效操作
                    if (selector.select() == 0)
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

                        // 根据就绪状态来判断相应的逻辑
                        // 如果是可读事件
                        if (selectionKey.isReadable()) {
                            readHandler(selectionKey);
                        }
                    }
                } catch (Exception e) {
                    break;
                }
            }
        }

        /**
         * Run in the message receiver thread
         */
        public void readHandler(SelectionKey selectionKey) {
            // 要从selectionKey中获取到已经就绪的channel
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            try {
                ByteBuffer byteBuffer = ByteBuffer.allocate(3096);
                // 读取客户端请求信息
                socketChannel.read(byteBuffer);
                router.dispatch(new DataWrapper(byteBuffer), socketChannel);
            } catch (IOException | ClassNotFoundException ioe) {
                ioe.printStackTrace();
            }

            try {
                // 将channel再次注册到selector上，监听它的可读事件
                socketChannel.register(selector, SelectionKey.OP_READ);
            } catch (ClosedChannelException cce) {
                cce.printStackTrace();
            }
        }
    }

}
