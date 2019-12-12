# fake-qq

## Changelog更改日志

> 较新的在上面

### 191212

- 增加登录和注册窗体类，运行App可成功创建
  > @...
- 更新一些文档
  > [文件结构](#%e7%9b%ae%e5%bd%95%e7%bb%93%e6%9e%84)
  > [服务端类之间的调用关系](#%e6%9c%8d%e5%8a%a1%e7%ab%af%e7%b1%bb%e9%97%b4%e8%b0%83%e7%94%a8%e5%85%b3%e7%b3%bb)
  > @author jon2180

### 191211

- 设置路由的分发类(com.nxt.im.server.Router) 
  > 主要功能是分发用户的不同请求给相应的处理方法，结构有待优化\
  > @author jon2180
- 设置socket的包装器类 (com.nxt.im.server.SocketWrapper) 
  > 主要功能是用于作为 HashMap 的值，方便找到该用户所连接的SocketChannel对象，结构有待优化\
  > @author jon2180
- 连接上了数据库
  > [建表Sql](./sql/v191208.1.sql)\
  > [数据库连接配置](./src/main/java/com/nxt/im/config/Database.java)\
  > @author jon2180

### 191210

- 设计并部分实现了前后端的通信方式，请看 [C/S通信方式](#cs-%e9%80%9a%e4%bf%a1%e6%96%b9%e5%bc%8f)
- 新增了几个bug😂，逻辑只是展现了思路，并没有完善


## 一、概述

### 构建系统

基于 maven

mvn 常用命令

```bash
# 单元测试
mvn test

# 干净构建
mvn clean package

# 到 target/classes 下开始输入命令，运行测试
java <package-name>
```

### 实现技术

- Java NIO通信
- MySQL关系型数据库
- Java Swing 图形界面编程
- JUnit 单元测试框架

## 二、需求

1. 基本的注册登录
2. 好友系统，包括好友与用户群，应当可以记录在线人数与好友在线状态
3. 单人聊天与群聊功能

## 三、文档

### C/S 通信方式

> @update 2019-12-10 17:05\
> @description 描述前后端通信的方式

当存在消息时，消息需要被包装成一个 `com.nxt.im.common.DataByteBuffer` 对象，

DataByteBuffer 存在两个构造函数，注意的是调用需要捕捉抛出的错误

```java

/**
 * 发送消息时使用，把数据转换为 ByteBuffer 发送
 * url 的功能类似 http 的 url, obj 即为数据
 */
DataByteBuffer(String url, Serializable obj);

/**
 * 接收消息时使用
 * 接收到 ByteBuffer 消息后使用此方法构造出对象
 */
DataByteBuffer(ByteBuffer byteBuffer);

```

发送示例

然后调用 `DateByteBuffer` 的 `toByteBuffer()` 方法，获取到将序列化 `DateByteBuffer` 之后的 `ByteBuffer` 对象，并将此对象通过 `Channel` 的 `write(ByteBuffer)` 方法发送(此方法被封装在 `com.nxt.im.client.Client.send()` 方法中)

```java
Accounts account = new Accounts();
account.setEmail("763653451@qq.com");
account.setNickname("slience");
account.setSignature("this is a signature");

DataByteBuffer dataByteBuffer = new DataByteBuffer("/user/name", account);

/**
 * 方法位于 com.nxt.im.client.Client 类中
 * 发送到后端
 */
client.send(dataByteBuffer.toByteBuffer());

```

接收消息示例（部分代码）

```java
/**
 * 创建buffer
 */
ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

try {
  socketChannel.read(byteBuffer);

  DataByteBuffer dataByteBuffer = new DataByteBuffer(byteBuffer);
  System.out.println(dataByteBuffer.getUrl());
  Accounts account = (Accounts) dataByteBuffer.getData();
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
