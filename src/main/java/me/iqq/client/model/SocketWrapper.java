package me.iqq.client.model;

import me.iqq.client.socket.ClientSocket;
import me.iqq.client.socket.Router;
import me.iqq.common.protocol.DataWrapper;
import me.iqq.common.utils.Serial;

import java.io.IOException;
import java.nio.ByteBuffer;

public class SocketWrapper {

    ClientSocket clientSocket;

    public static ClientSocket getConnection(String ip, int port, Router router) throws IOException {
        return new ClientSocket(ip, port, router);
    }

    public SocketWrapper(ClientSocket initialClientSocket) {
        clientSocket = initialClientSocket;
    }

    /**
     * 向服务端发送 byteBuffer消息
     *
     * @param byteBuffer ByteBuffer对象
     * @throws IOException socketChannel发送出错便抛出
     */
    public void send(ByteBuffer byteBuffer) throws IOException {
        if (byteBuffer != null)
            clientSocket.getSocketChannel().write(byteBuffer);
    }

    /**
     * 向服务端发送 byteBuffer消息
     *
     * @param dataWrapper dataWrapper
     * @throws IOException socketChannel发送出错便抛出
     */
    public void send(DataWrapper dataWrapper) throws IOException {
        send(Serial.objectToByteBuffer(dataWrapper));
    }


    public void startReceiverThread() {
        clientSocket.getReceiverThread().start();
    }

    public void stopReceiverThread() throws InterruptedException {
        clientSocket.getReceiverThread().join();
    }

}
