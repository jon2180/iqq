package me.iqq.client.socket;

import me.iqq.common.api.LoginApiProto.LoginReq;
import me.iqq.common.protocol.MessageProtocol;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;

class ClientSocketTest {

//    private NioClient nioClient = new NioClient("127.0.0.1", 8000, null);
//
//    ClientSocketTest() throws IOException {
//    }
//
//    @Test
////    @Disabled
//    void getSocketChannel() {
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        LoginReq req = LoginReq.newBuilder()
//            .setUsername("haha")
//            .setPassword("hehe")
//            .setIsNeedKey(true)
//            .build();
//        byte[] body = req.toByteArray();
//        System.out.println(body.length);
//        byte[] bytes = MessageProtocol.pack(body, "iqq://localhost:8080/", "string", "1234567", "2346578");
//
//        try {
//            nioClient.getSocketChannel().write(ByteBuffer.wrap(bytes));
//            System.out.println("successful");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
