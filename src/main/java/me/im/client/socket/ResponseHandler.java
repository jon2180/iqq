package me.im.client.socket;

import me.im.client.ui.FriendsFrame;
import me.im.client.ui.LoginFrame;
import me.im.client.ui.RegisterFrame;
import me.im.common.protocol.Accounts;
import me.im.common.protocol.DataByteBuffer;
import me.im.common.protocol.Messages;
import me.im.common.utils.CommandCode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

// import java.net.InetSocketAddress;
// import java.nio.channels.Selector;
// import java.nio.channels.ServerSocketChannel;
// import java.nio.charset.Charset;

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

        // 循环等待新消息

        while (true) {
            try {
                // TODO 获取可用channel数量
                int readyChannels = selector.select();

                // TODO 为什么要这样？
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

                    // 根据就绪状态来判断相应的逻辑

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

        ByteBuffer byteBuffer = ByteBuffer.allocate(3096);

        // 循环读取客户端请求信息

        try {
            socketChannel.read(byteBuffer);
            DataByteBuffer data = new DataByteBuffer(byteBuffer);

            int statusCode = data.getStatusCode();
            Object object = data.getData();
            long time = data.getTime();
            String type = data.getType();

            switch (data.getUrl()) {
                case CommandCode.REG: {
                    Accounts account = (Accounts) object;
                    System.out.println(time);
                    System.out.println(statusCode);
                    System.out.println(account.getQnumber() + " : " + account.getNickname());
                    RegisterFrame.getInstance().register(statusCode, account);
                    break;
                }
                case CommandCode.LOG_IN: {
                    LoginFrame.getInstance().login(statusCode, type, data.getData());
                    break;
                }
                case CommandCode.SEND_MESSAGE: {
                    Messages message = (Messages) object;
                    System.out.println(message.getOrigin_account() + "  " + time);
                    System.out.println(message.getContent());
                    // 调用方式是把 （String myQQ, Messages message, long time)
                    FriendsFrame.displayMessage(message.getTarget_account(), message, time);
                    break;
                }
                case CommandCode.NOTIFY_ONLINE: {
                    System.out.println((String) object);
                    break;
                }
                default:
                    System.out.println("暂无此url选项");
            }
        } catch (IOException | ClassNotFoundException ioe) {
            ioe.printStackTrace();
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        /*
         * 将channel再次注册到selector上，监听它的可读事件
         */
        try {
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (ClosedChannelException cce) {
            cce.printStackTrace();
        }
    }
}
