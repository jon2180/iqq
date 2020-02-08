package me.im.common.protocol;

import java.io.*;
import java.nio.ByteBuffer;


/**
 * DataByteBuffer
 *
 * @version 191215
 * @descriptions 用于在服务端与客户端通道 Channel 中传递的消息格式规定
 * @serial
 */
public class DataByteBuffer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Message id
     * 指定唯一的消息id
     */
    private int mid;

    /**
     * 用于接收方收到消息后的操作
     */
    private String url;


    /**
     * 状态码 类似于 http 的状态码
     */
    private int statusCode = 404;

    /**
     * 真实数据
     */
    private Serializable data;

    /**
     * 消息类型 - 类似于 http 中的 Content-Type 字段
     * "Accounts", "Messages", "String", "Vector<Friends>", "Friends"
     */
    private String type = "String";

    /**
     * 消息建立的时间戳
     */
    private long time;

    public DataByteBuffer(ByteBuffer byteBuffer) throws IOException, ClassNotFoundException {
        DataByteBuffer dataByteBuffer = (DataByteBuffer) DataByteBuffer.byteBufferToObject(byteBuffer);
        this.url = dataByteBuffer.url;
        this.data = dataByteBuffer.data;
        this.type = dataByteBuffer.type;
        this.statusCode = dataByteBuffer.statusCode;
        this.mid = dataByteBuffer.mid;
        this.time = System.currentTimeMillis();
    }

    public DataByteBuffer(String url, Serializable data) {
        this.url = url;
        this.data = data;
        this.type = "serial-data";
        this.time = System.currentTimeMillis();
    }

    public DataByteBuffer(String url, String type, Serializable data) {
        this.url = url;
        this.data = data;
        this.type = type;
        this.time = System.currentTimeMillis();
    }

    public DataByteBuffer() {

    }

    /**
     * @return the mid
     */
    public int getMid() {
        return mid;
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
    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    /**
     * @return the time
     */
    public long getTime() {
        return time;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Serializable getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Serializable data) {
        this.data = data;
    }

    public ByteBuffer toByteBuffer() throws IOException {
        return DataByteBuffer.objectToByteBuffer(this);
    }

    /**
     * 对象序列化，转为 ByteBuffer
     *
     * @param obj 可序列化对象
     * @return 对象序列化后的ByteBuffer对象
     * @throws IOException 写入对象错误时抛出
     */
    public static ByteBuffer objectToByteBuffer(Serializable obj) throws IOException {
        // try {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bout);
        out.writeObject(obj);
        out.flush();
        byte[] bytes = bout.toByteArray();
        bout.close();
        out.close();
        // ————————————————
        // 版权声明：本文为CSDN博主「eclipser1987」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
        // 原文链接：https://blog.csdn.net/eclipser1987/article/details/5350007
        return ByteBuffer.wrap(bytes);
    }

    /**
     * ByteBuffer 还原为 Object
     *
     * @param buffer 代反序列化的ByteBuffer对象
     * @return 对象
     * @throws IOException            ...
     * @throws ClassNotFoundException 读Object错误时抛出
     */
    public static Object byteBufferToObject(ByteBuffer buffer) throws IOException, ClassNotFoundException {
        InputStream input = new ByteArrayInputStream(buffer.array());
        ObjectInputStream oi = new ObjectInputStream(input);
        Object obj = oi.readObject();
        input.close();
        oi.close();
        buffer.clear();
        return obj;
        // ————————————————
        // 版权声明：本文为CSDN博主「eclipser1987」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
        // 原文链接：https://blog.csdn.net/eclipser1987/article/details/5350007
    }
}
