# Changelog更改日志

## Dev版本

> 较新的在上面

### 191216

- 给数据库的 messages 和 friends 添加了 time 字段
    > @author jon2180
- 登录注册逻辑
    > @author jon2180
- 好友列表界面
    > @author deathwing111
- 配置命令字
    > @author jon2180
- 通信
    > @author jon2180

### 191215

- 重构了注册逻辑
  > 现以QQ号作为索引键\
  > @author jon2180
- 优化用户在网络中传输的 DataByteBuffer 类的结构
  > 添加了 mid, statusCode, time字段，具体请查看[DataByteBuffer](src/main/java/me/im/common/DataByteBuffer.java)\
  > @author jon2180
- 修复了 服务端与客户端在一方断开连接后，一直输出错误报告的bug
  > @author jon2180
- 新增消息转发的逻辑
  > @author jon2180
- 新增获取好友列表的逻辑
  > @author jon2180
- 新增通知在线好友的逻辑
  > @author jon2180
- 添加 Message 的单元测试
  > @author jon2180
- 添加了聊天界面
  > 简易聊天页面\
  > @author deathwing111
- 增加登录和注册界面的非空验证
  > @author deathwing111


### 191214

- 完成登录的逻辑
- 更新了一份新的sql，增加了accounts表里qnumber字段，id作为自增数字id作为一个用户的qq号不太合理

### 191213

- 建立客户端和服务端登录验证通信
  >- 在[App.java](#%e7%9b%ae%e5%bd%95%e7%bb%93%e6%9e%84)中创建登录窗体，当点击确定时，在响应事件中实例化client中的[Login对象](#%e7%9b%ae%e5%bd%95%e7%bb%93%e6%9e%84)，用于与服务端建立通信
  >- 在[Login.java](#%e7%9b%ae%e5%bd%95%e7%bb%93%e6%9e%84)中通过[Client](#%e7%9b%ae%e5%bd%95%e7%bb%93%e6%9e%84)建立通信，并成功把用户输入信息包装成[Accounts对象](#%e7%9b%ae%e5%bd%95%e7%bb%93%e6%9e%84)传给服务端，url为[/user/login](#%e7%9b%ae%e5%bd%95%e7%bb%93%e6%9e%84)
  >- 服务端在[Router.java](#%e7%9b%ae%e5%bd%95%e7%bb%93%e6%9e%84)中对事件进行分发,增加了一个[loginCheck](#%e7%9b%ae%e5%bd%95%e7%bb%93%e6%9e%84)方法，在该方法中实例化一个sever的[Login对象](#%e7%9b%ae%e5%bd%95%e7%bb%93%e6%9e%84)，完成验证的逻辑
  >- @author eraevil

### 191212

- 增加登录和注册窗体类，运行App可成功创建
  > @author eraevil
- 更新一些文档
  > [文件结构](#%e7%9b%ae%e5%bd%95%e7%bb%93%e6%9e%84)\
  > [服务端类之间的调用关系](#%e6%9c%8d%e5%8a%a1%e7%ab%af%e7%b1%bb%e9%97%b4%e8%b0%83%e7%94%a8%e5%85%b3%e7%b3%bb)\
  > @author jon2180

### 191211

- 设置路由的分发类(Router) 
  > 主要功能是分发用户的不同请求给相应的处理方法，结构有待优化\
  > @author jon2180
- 设置socket的包装器类 (SocketWrapper) 
  > 主要功能是用于作为 HashMap 的值，方便找到该用户所连接的SocketChannel对象，结构有待优化\
  > @author jon2180
- 连接上了数据库
  > [建表Sql](./sql/v191216.sql)\
  > [数据库连接配置](src/main/java/me/im/server/config/Database.java)\
  > @author jon2180

### 191210

- 设计并部分实现了前后端的通信方式，请看 [C/S通信方式](#cs-%e9%80%9a%e4%bf%a1%e6%96%b9%e5%bc%8f)
- 新增了几个bug😂，逻辑只是展现了思路，并没有完善
  > @jon2180

