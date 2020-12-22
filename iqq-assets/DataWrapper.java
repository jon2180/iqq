package me.iqq.common.protocol;

import me.iqq.common.protocol.type.CommandCode;
import me.iqq.common.protocol.type.DataType;

import java.io.Serializable;
import java.util.Vector;


/**
 * DataByteBuffer
 * 用于在服务端与客户端通道 Channel 中传递的消息格式规定
 *
 * @version 191215
 * @serial
 */
@Deprecated
public class DataWrapper<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用于接收方收到消息后的操作
     */
    private CommandCode url;

    /**
     * 状态码 类似于 http 的状态码
     */
    private int statusCode = 200;

    /**
     * 真实数据
     */
    private T data;

    /**
     * 消息类型 - 类似于 http 中的 Content-Type 字段
     */
    private DataType type;

    /**
     * 消息建立的时间戳
     */
    private long time;

    public DataWrapper(CommandCode url, int code, DataType type, T data) {
        this.url = url;
        this.statusCode = code;
        this.data = data;
        this.type = type;
        this.time = System.currentTimeMillis();
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return the statusCode
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(CommandCode url) {
        this.url = url;
    }

    public CommandCode getUrl() {
        return url;
    }

    /**
     * @return the time
     */
    public long getTime() {
        return time;
    }

    /**
     * @param dataType the type to set
     */
    public void setType(DataType dataType) {
        type = dataType;
    }

    public DataType getType() {
        return type;
    }

    public T getData() {
        return data;
    }

//    /**
//     * 通过一个byteBuffer来构造对象
//     *
//     * @param byteBuffer 对象二进制数据
//     * @throws IOException            二进制失败
//     * @throws ClassNotFoundException 没找到类名
//     */
//    @Deprecated
//    public DataWrapper(ByteBuffer byteBuffer) throws ClassNotFoundException, IOException {
//        DataWrapper<T> dataWrapper = (DataWrapper<T>) Serial.byteBufferToObject(byteBuffer);
//        this.url = dataWrapper.url;
//        this.data = dataWrapper.data;
//        this.type = dataWrapper.type;
//        this.statusCode = dataWrapper.statusCode;
//        this.time = System.currentTimeMillis();
//    }


    /**
     * @return data为数字
     */
    @Deprecated
    public String getString() {
        return type == DataType.PureString ? (String) data : null;
    }


    /**
     * @return data为消息
     */
    @Deprecated
    public Message getMessage() {
        return type == DataType.MessageObject ? (Message) data : null;
    }

    @Deprecated
    public Friend getFriend() {
        return type == DataType.FriendObject ? (Friend) data : null;
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    public Vector<Friend> getFriends() {
        return type == DataType.FriendsVector ? (Vector<Friend>) data : null;
    }

    /**
     * @param data the data to set
     */
    @Deprecated
    public void setData(T data) {
        this.data = data;
    }

    @Deprecated
    public User getUser() {
        return (User) data;
    }

}
