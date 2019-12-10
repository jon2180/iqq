# fake-qq

## Changelog更改日志

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