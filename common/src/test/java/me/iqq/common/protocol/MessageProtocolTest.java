package me.iqq.common.protocol;

import me.iqq.common.api.LoginApiProto;
import me.iqq.common.api.MessageHeaderProto;
import me.iqq.common.utils.BytesUtil;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import java.io.IOException;


class MessageProtocolTest {

    @Ignore("not ready yet")
    @Test
    void pack() {
//        MessageProtocol.pack();
    }

    @Test
    void setup() {
        // 构建 内容实体
        LoginApiProto.LoginReq req = LoginApiProto.LoginReq.newBuilder()
            .setUsername("haha")
            .setPassword("hehe")
            .setIsNeedKey(true)
            .build();
        byte[] body = req.toByteArray();

        byte[] bytes = MessageProtocol.pack(body, "iqq://localhost:8080/", MessageHeaderProto.MessageHeader.ContentType.SIGN_IN, "1234567", "2346578");

        System.out.println(bytes.length);
        System.out.println(BytesUtil.bytesToHexString(BytesUtil.read(bytes, 0, 4)));
        System.out.println(BytesUtil.bytesToHexStringWithSpace(bytes));
//        System.out.println(cLen);
//        System.out.println(contentType);
//        System.out.println("开始标记：" + BytesUtil.byteArrayToInt32(startTag));
//        System.out.println("长度标记：" + BytesUtil.byteArrayToInt32(headerLengthTag));


//        for (int i = 0; i < 4; ++i)
//            headerLength[i] = bytes[idx++];
//        System.out.println("标记头长度：" + BytesUtil.byteArrayToInt32(headerLength));

//        System.out.println(BytesUtil.bytesToHexStringWithSpace(startTag));
//        System.out.println(BytesUtil.bytesToHexStringWithSpace(headerLength));
//        byte[] header = new byte[];
//        for (int i = 0; i < 4; ++i)
//            header[i] = bytes[idx++];

        MessagePacket m = null;
        try {
            m = MessageProtocol.unpack(bytes);
            System.out.println(m.getHeaderLength());
            System.out.println((m.getHeader().getContentLength()));
            System.out.println(m.getBody().length);
            System.out.println(BytesUtil.bytesToHexStringWithSpace(m.getBody()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void readToken() {
    }

    @Test
    void buildHeader() {
    }

    @Test
    void unpack() {

    }
}
