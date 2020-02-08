# fake-qq

<img alt="GitHub Workflow Status" src="https://img.shields.io/github/workflow/status/jon2180/fake-qq/Java CI"> <img alt="GitHub" src="https://img.shields.io/github/license/jon2180/fake-qq">

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
# 运行不同的包，替换类名即可
```

### 实现技术

- Java NIO通信
- MySQL关系型数据库
- Java Swing 图形界面编程
- JUnit 单元测试框架


### 预览

![预览](doc/qq-v1.gif)

