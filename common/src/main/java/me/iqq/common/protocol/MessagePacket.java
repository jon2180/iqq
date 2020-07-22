package me.iqq.common.protocol;

import lombok.Builder;
import lombok.Data;
import me.iqq.common.api.MessageHeaderProto;

@Data
@Builder
public class MessagePacket {
    private byte[] startTag;
    private byte[] headerLengthTag;
    private int headerLength;
    private byte[] headerBytes;
    private MessageHeaderProto.MessageHeader header;
    private byte[] body;
}
