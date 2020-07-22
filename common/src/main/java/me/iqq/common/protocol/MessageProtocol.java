package me.iqq.common.protocol;

import lombok.extern.slf4j.Slf4j;
import me.iqq.common.api.MessageHeaderProto;
import me.iqq.common.api.MessageHeaderProto.MessageHeader;
import me.iqq.common.api.MessageHeaderProto.MessageHeader.ContentType;
import me.iqq.common.utils.BytesUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class MessageProtocol {

    private static final byte[] startFlag = {0x12, 0x34, 0x45, 0x47};


    public static byte[] pack(byte[] body,
                              String url,
                              ContentType contentType,
                              String from,
                              String to) {
        int contentLength = body.length;
        MessageHeader header = buildHeader(contentLength, url, contentType, from, to);
        byte[] bytesHeader = header.toByteArray();
        int headerLength = bytesHeader.length;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            // 写入 开始标志
            baos.write(startFlag);
            // 写入 header 长度
            baos.write(BytesUtil.int32ToByteArray(headerLength));
            // 写入 header 实体
            baos.write(bytesHeader);
            // 写入 body 实体
            baos.write(body);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }


    public static byte[] pack(byte[] body,
                              String url,
                              ContentType contentType,
                              String from,
                              String to,
                              String token) {
        int contentLength = body.length;
        MessageHeader header = buildHeader(contentLength, url, contentType, from, to, token);
        byte[] bytesHeader = header.toByteArray();
        int headerLength = bytesHeader.length;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            // 写入 开始标志
            baos.write(startFlag);
            // 写入 header 长度
            baos.write(BytesUtil.int32ToByteArray(headerLength));
            // 写入 header 实体
            baos.write(bytesHeader);
            // 写入 body 实体
            baos.write(body);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }

    public static String readToken() {
        return "token: read from priv-key";
    }

    public static MessageHeader buildHeader(int length, String url, ContentType contentType, String from, String to) {
        // 构建 header 实体
        MessageHeader header = MessageHeader.newBuilder()
            .setContentLength(length)
            .setContentType(contentType)
            .setTime(System.currentTimeMillis())
            .setToken(readToken())
            .setUrl(url)
            .setFrom(from)
            .setTo(to)
            .build();
        return header;
    }

    public static MessageHeader buildHeader(int length, String url, ContentType contentType, String from, String to, String token) {
        // 构建 header 实体
        MessageHeader header = MessageHeader.newBuilder()
            .setContentLength(length)
            .setContentType(contentType)
            .setTime(System.currentTimeMillis())
            .setToken(token)
            .setUrl(url)
            .setFrom(from)
            .setTo(to)
            .build();
        return header;
    }


    public static MessagePacket unpack(byte[] bytes) throws IOException {
        if (bytes.length <= 8)
            throw new IOException("");

        MessagePacket.MessagePacketBuilder builder = MessagePacket.builder();

        // part 1 - start tag
        byte[] startTag = new byte[4];
        System.arraycopy(bytes, 0, startTag, 0, 4);

        if (!checkStartTag(startFlag)) {
            System.out.println();
            throw new IOException("Incomplete packet");
        }

        builder.startTag(startTag);

        // part 2 - length tag
        byte[] headerLengthTag = new byte[4];
        System.arraycopy(bytes, 4, headerLengthTag, 0, 4);
        builder.headerLengthTag(headerLengthTag);

        // calculate the length of header
        int headerLen = BytesUtil.byteArrayToInt32(headerLengthTag);
        builder.headerLength(headerLen);

        // part 3 - header tag
        byte[] headerBytes = new byte[headerLen];
        System.arraycopy(bytes, 8, headerBytes, 0, headerLen);
        builder.headerBytes(headerBytes);

        // deserialize the header
        MessageHeaderProto.MessageHeader header = MessageHeaderProto.MessageHeader.parseFrom(headerBytes);
        builder.header(header);

        // get the length of body
        int bodyLen = header.getContentLength();

        // part 4 - body tag
        byte[] body = new byte[bodyLen];
        System.arraycopy(bytes, 8 + headerLen, body, 0, bodyLen);
        builder.body(body);

        return builder.build();
    }

    private static boolean checkStartTag(byte[] receivedStartTag) {
        if (receivedStartTag == null || receivedStartTag.length != startFlag.length)
            return false;
        for (int i = 0; i < 4; ++i) {
            if (receivedStartTag[i] != startFlag[i]) {
                return false;
            }
        }
        return true;
    }

}
