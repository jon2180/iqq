# fake-qq

## Changelog更改日志

> 较新的在上面

### 191213

- 建立客户端和服务端登录验证通信
  > 在[App.java](#%e7%9b%ae%e5%bd%95%e7%bb%93%e6%9e%84)中创建登录窗体，当点击确定时，在响应事件中实例化client中的[Login对象](#%e7%9b%ae%e5%bd%95%e7%bb%93%e6%9e%84)，用于与服务端建立通信\
  > 在[Login.java](#%e7%9b%ae%e5%bd%95%e7%bb%93%e6%9e%84)中通过[Client](#%e7%9b%ae%e5%bd%95%e7%bb%93%e6%9e%84)建立通信，并成功把用户输入信息包装成[Accounts对象](#%e7%9b%ae%e5%bd%95%e7%bb%93%e6%9e%84)传给服务端，url为[/user/login](#%e7%9b%ae%e5%bd%95%e7%bb%93%e6%9e%84)\
  > 服务端在[Router.java](#%e7%9b%ae%e5%bd%95%e7%bb%93%e6%9e%84)中对事件进行分发,增加了一个[loginCheck](#%e7%9b%ae%e5%bd%95%e7%bb%93%e6%9e%84)方法，在该方法中实例化一个sever的[Login对象](#%e7%9b%ae%e5%bd%95%e7%bb%93%e6%9e%84)，完成验证的逻辑\
  > @author eraevil

### 191212

- 增加登录和注册窗体类，运行App可成功创建
  > @author eraevil
- 更新一些文档
  > [文件结构](#%e7%9b%ae%e5%bd%95%e7%bb%93%e6%9e%84)\
  > [服务端类之间的调用关系](#%e6%9c%8d%e5%8a%a1%e7%ab%af%e7%b1%bb%e9%97%b4%e8%b0%83%e7%94%a8%e5%85%b3%e7%b3%bb)\
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
  > @jon2180


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

构建之后的运行

```powershell
# 例如：运行 com.nxt.im.Server包
# 编译构建后
# 在根目录打开终端运行
./run.bat com.nxt.im.Serve
# 运行不同的包，替换类
