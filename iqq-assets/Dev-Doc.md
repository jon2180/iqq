
> 以下内容已不适用与最新版，之后抽空更新

## 开发备注文档

### C/S 通信方式

> @update 2019-12-10 17:05\
> @description 描述前后端通信的方式

当存在消息时，消息需要被包装成一个 `DataByteBuffer` 对象，

DataByteBuffer 存在两个构造函数，注意的是调用需要捕捉抛出的错误

```java
class DataByteBuffer {
    /**
     * 发送消息时使用，把数据转换为 ByteBuffer 发送
     * url 的功能类似 http 的 url, obj 即为数据
     */
    DataByteBuffer(String url, Serializable obj) {
    }
    
    /**
     * 接收消息时使用
     * 接收到 ByteBuffer 消息后使用此方法构造出对象
     */
    DataByteBuffer(ByteBuffer byteBuffer) {
    }
}
```

发送示例

然后调用 `DateByteBuffer` 的 `toByteBuffer()` 方法，获取到将序列化 `DateByteBuffer` 之后的 `ByteBuffer` 对象，并将此对象通过 `Channel` 的 `write(ByteBuffer)` 方法发送(此方法被封装在 `com.nxt.im.client.Client.send()` 方法中)

```java
Accounts account = new Accounts();
account.setEmail("763653451@qq.com");
account.setNickname("slience");
account.setSignature("this is a signature");

DataByteBuffer dataWrapper = new DataByteBuffer("/user/name", account);

/**
 * 方法位于 com.nxt.im.client.Client 类中
 * 发送到后端
 */
client.send(dataWrapper.toByteBuffer());

```

接收消息示例（部分代码）

```java
/**
 * 创建buffer
 */
ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

try {
  socketChannel.read(byteBuffer);

  DataByteBuffer dataWrapper = new DataByteBuffer(byteBuffer);
  System.out.println(dataWrapper.getUrl());
  Accounts account = (Accounts) dataWrapper.getData();
  System.out.println(account.getSignature());
} catch (IOException ioE) {
  // ioE.printStackTrace();
  // return;
} catch (ClassNotFoundException cnfE) {
  // cnfE.printStackTrace();
  // return;

}
```




### 目录结构

```bash
sql/    # 数据库文件
res/    # 资源文件
src/    # 代码与测试代码源文件
  main/java/com/nxt/im/  # 主包
    client/              # 客户端的核心，客户端独有，客户端与后端交互的代码应该放在这里
    common/              # 客户端服务器共用，例如交互用到的序列化的类
    config/              # 配置，这个我也还没搞清楚需要放哪些
    db/                  # 数据库相关，服务端独有
    server/              # 服务端独有，服务器核心逻辑
    ui/                  # 界面相关，客户端独有
  test/java/com/nxt/im/  # 单元测试
```

### 服务端类间调用关系

```
NioServer 主入口
  - 开启服务器就监听来自客户端的事件
  - 碰到可读事件，读取到byteBuffer类型的消息
    - 【调用了Router.dispatch()方法】把事件及数据发送给 Router
      - Router 判断事件类型，在数据转发给位于Router类内的相应的处理方法
        - 碰到登录成功的事件，就把当前用户的id作为键，特定的SocketWrapper对象作为值放到 HashMap（目前位于NioServer） 中,具体处理方式有待商榷
        - 碰到断开连接事件或退出登录事件，就在HashMap中移除掉此id对应的项目
      - 在 Router 类的各个方法中，可能会调用到数据库的相关类
```

### 可能存在的交互

所以消息都被封装在 DataByteBuffer对象中

- 注册 Accounts对象，核心参数：nickname 与 password   核心返回值：id
- 登录 Accounts对象， 核心参数：id 与 password 核心返回值：状态
- 加好友 Friends对象 核心参数：好友的 id 核心返回值： Fiends对象
- 删好友 Friends对象 核心参数：好友的 id 核心返回值：状态
- 拉黑好友 Friends对象 核心参数：好友的 id 核心返回值：状态
- 发消息 Messages 对象 核心参数：对方的id，消息类型与内容  核心返回值：状态
  - 文本 直接作为字符串内容 
  - 图片 Image对象作为content
  - 音频【目前不需实现】
  - 文件【目前不需实现】
  - ...
- 下线 无需参数


### 备注

真正一构思下来发现要做好这么一个IM软件需要做的事情是相当地多

