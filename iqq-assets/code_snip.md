```java
//        Connection connection = null;
//        PreparedStatement statement = null;
//        ResultSet rs = null;
//
//        Vector<Friend> friends = new Vector<>();
//
//        try {
//            connection = DatabaseConnection.getConnection();
//            String sql = "select id, target_account, group_name, type from friends where origin_account=?";
//            statement = connection.prepareStatement(sql);
//            statement.setObject(1, qq);
//            rs = statement.executeQuery();
//            while (rs.next()) {
//                int friendId = rs.getInt("id");
//                String targetAccount = rs.getString("target_account");
//                String groupName = rs.getString("group_name");
//                int type = rs.getInt("type");
//
//                friends.add(new Friend(friendId, qq, targetAccount, groupName, type));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//
//            if (rs != null) {
//                try {
//                    rs.close();
//                } catch (SQLException sqlException) {
//                    sqlException.printStackTrace();
//                }
//            }
//            if (statement != null) {
//                try {
//                    statement.close();
//                } catch (SQLException sqlException) {
//                    sqlException.printStackTrace();
//                }
//            }
//            if (connection != null) {
//                try {
//                    connection.close();
//                } catch (SQLException sqlException) {
//                    sqlException.printStackTrace();
//                }
//            }
//        }
//
//        return friends;
```

### Send Message

```java
//    /**
//     * 发送消息给指定客户端
//     *
//     * @param dataBuf 消息对象
//     */
//    public void sendMessage(DataWrapper dataBuf, UserSocket response) {
//
//        Message message = dataBuf.getMessage();
//
//        String friendQQ = message.getTargetAccount();
//
//        if (socketManager.containsKey(friendQQ)) {
//            UserSocket userSocket = socketManager.get(friendQQ);
//            try {
//                if (!userSocket.isOpen()) {
//                    socketManager.remove(friendQQ);
//                } else {
//                    userSocket.send(dataBuf);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
```

### Controller.login()

```java
        User account = request.getUser();
//        ResultSet resultSet;
//        String qq = account.getQQ();
//        String password = account.getPassword();
//        try {
//            resultSet = FriendDAO.find(qq);
//
//            // x 账户不存在
//            if (!resultSet.next()) {
//                System.out.println("No this accounts");
//
//                DataWrapper data = new DataWrapper(CommandCode.LOG_IN, DataType.PureString, "无此用户");
//                data.setStatusCode(401);
//
//                data.setType(DataType.PureString);
//                response.send(data);
//                return;
//            }
//
//            String realPassword = resultSet.getString("password");
//
//            // x 登录账户与密码不匹配
//            if (!realPassword.equals(password)) {
//                System.out.println(qq + " login failed");
//                DataWrapper data = new DataWrapper(CommandCode.LOG_IN, DataType.PureString, "登录密码错误");
//                data.setStatusCode(401);
//                response.send(data);
//                return;
//            }
//
//            // v 正常登录
//            System.out.println(qq + " login successfully");
//
//            // 把当前用户的 qq号 作为键，对应的 SocketWrapper 对象作为值，放进 socketMap 中
//            response.setUserID(qq);
//            socketManager.put(qq, response);
//
//            Vector<Friend> friends = FriendDAO.getAllFriends(qq);
//
//            // 获取各好友的在线状态，并通知在线好友：“我上线了”
//            if (friends != null && !friends.isEmpty()) {
//                response.notifyFriends(friends, socketManager);
//            }
//
//            // 发送消息给客户端：好友列表
//            DataWrapper data = new DataWrapper(CommandCode.LOG_IN, DataType.FriendsVector, friends);
//            response.send(data);
//        } catch (IOException | SQLException ioE) {
//            ioE.printStackTrace();
//        }
```

### Controller User

```java

//    private final UserService userService = new UserService();
//
//    /**
//     * 注册用户
//     *
//     * @param request  需要的数据
//     * @param response 用来给客户端回复消息
//     */
//    private void registerUser() throws IOException {
//        DataWrapper request;
//        UserSocket response;
//        User account = request.getUser();
//        String nickname = account.getNickname();
//        String password = account.getPassword();
//        String email = account.getEmail();
//
//        // register
//        String qq = userService.registerUser(nickname, password, email);
//
//        // 把账户返回给客户端
//        account.setQQ(qq);
//        System.out.println("Successful: " + qq + ":" + nickname);
//
//        DataWrapper data = new DataWrapper(CommandCode.REG, DataType.UserObject, account);
//        data.setStatusCode(200);
//        response.send(data);
//    }
//
//    /**
//     * 触发登录验证
//     */
//    private void loginCheck(DataWrapper request, UserSocket response) {
//        User account = request.getUser();
//        ResultSet resultSet;
//        String qq = account.getQQ();
//        String password = account.getPassword();
//        try {
//            resultSet = FriendDAO.find(qq);
//
//            // x 账户不存在
//            if (!resultSet.next()) {
//                System.out.println("No this accounts");
//
//                DataWrapper data = new DataWrapper(CommandCode.LOG_IN, DataType.PureString, "无此用户");
//                data.setStatusCode(401);
//
//                data.setType(DataType.PureString);
//                response.send(data);
//                return;
//            }
//
//            String realPassword = resultSet.getString("password");
//
//            // x 登录账户与密码不匹配
//            if (!realPassword.equals(password)) {
//                System.out.println(qq + " login failed");
//                DataWrapper data = new DataWrapper(CommandCode.LOG_IN, DataType.PureString, "登录密码错误");
//                data.setStatusCode(401);
//                response.send(data);
//                return;
//            }
//
//            // v 正常登录
//            System.out.println(qq + " login successfully");
//
//            // 把当前用户的 qq号 作为键，对应的 SocketWrapper 对象作为值，放进 socketMap 中
//            response.setUserID(qq);
//            socketManager.put(qq, response);
//
//            Vector<Friend> friends = FriendDAO.getAllFriends(qq);
//
//            // 获取各好友的在线状态，并通知在线好友：“我上线了”
//            if (friends != null && !friends.isEmpty()) {
//                response.notifyFriends(friends, socketManager);
//            }
//
//            // 发送消息给客户端：好友列表
//            DataWrapper data = new DataWrapper(CommandCode.LOG_IN, DataType.FriendsVector, friends);
//            response.send(data);
//        } catch (IOException | SQLException ioE) {
//            ioE.printStackTrace();
//        }
//    }
    }



    //    public User find(String id) {
//        return null;
//    }

//    /**
//     * 注册用户
//     *
//     * @param nickname
//     * @param password
//     * @param email
//     */
//    public String registerUser(String nickname, String password, String email) {
//        String sql = "select qnumber, nickname from accounts where qnumber=?";
//        try {
//
//            // 随机出来一个QQ号
//            String qq;
//
//            boolean isRepeat;
//
//            do {
//                qq = StringUtil.getRandom();
//
//
//                // 判断QQ号是否已经被占用
//                isRepeat = dbConnection.query(sql).next();
//            } while (isRepeat);
//
//            account.setQQ(qq);
//            FriendDAO.createUser(account);
//
//            // 把账户返回给客户端
//            account.setQQ(qq);
//            DataWrapper data = new DataWrapper(CommandCode.REG, DataType.UserObject, account);
//            data.setStatusCode(200);
//
//            System.out.println("Successful: " + qq + ":" + nickname);
//
//            response.send(data);
//
//        } catch (IOException | SQLException ioE) {
//            ioE.printStackTrace();
//        }
//    }
//
//
//    /**
//     * 触发登录验证
//     */
//    private void loginCheck(DataWrapper request, UserSocket response) {
//        User account = request.getUser();
//        ResultSet resultSet;
//        String qq = account.getQQ();
//        String password = account.getPassword();
//        try {
//            resultSet = FriendDAO.find(qq);
//
//            // x 账户不存在
//            if (!resultSet.next()) {
//                System.out.println("No this accounts");
//
//                DataWrapper data = new DataWrapper(CommandCode.LOG_IN, DataType.PureString, "无此用户");
//                data.setStatusCode(401);
//
//                data.setType(DataType.PureString);
//                response.send(data);
//                return;
//            }
//
//            String realPassword = resultSet.getString("password");
//
//            // x 登录账户与密码不匹配
//            if (!realPassword.equals(password)) {
//                System.out.println(qq + " login failed");
//                DataWrapper data = new DataWrapper(CommandCode.LOG_IN, DataType.PureString, "登录密码错误");
//                data.setStatusCode(401);
//                response.send(data);
//                return;
//            }
//
//            // v 正常登录
//            System.out.println(qq + " login successfully");
//
//            // 把当前用户的 qq号 作为键，对应的 SocketWrapper 对象作为值，放进 socketMap 中
//            response.setUserID(qq);
//            socketManager.put(qq, response);
//
//            Vector<Friend> friends = FriendDAO.getAllFriends(qq);
//
//            // 获取各好友的在线状态，并通知在线好友：“我上线了”
//            if (friends != null && !friends.isEmpty()) {
//                response.notifyFriends(friends, socketManager);
//            }
//
//            // 发送消息给客户端：好友列表
//            DataWrapper data = new DataWrapper(CommandCode.LOG_IN, DataType.FriendsVector, friends);
//            response.send(data);
//        } catch (IOException | SQLException ioE) {
//            ioE.printStackTrace();
//        }
//    }
```

### User find

```java

        QueryEvent queryEvent = new QueryEvent(DatabaseConnection.getConnection());


        String sql = "SELECT id,qnumber,nickname,password FROM accounts where qnumber = ?";
        String[] params = new String[]{qq};

        User accounts = null;
        try {
//            ResultSet rs = queryEvent.prepare(sql).setParams(params).executeQuery();
            ResultSet rs = queryEvent.prepare(sql).setParams(qq).executeQuery();

            if (rs.next()) {
                accounts = new User();
                accounts.setId(rs.getInt("id"));
                accounts.setQQ(rs.getString("qnumber"));
                accounts.setNickname(rs.getString("nickname"));
                accounts.setSignature(rs.getString("signature"));
                accounts.setTel(rs.getString("tel"));
                accounts.setIp(rs.getString("ip"));
                accounts.setPort(rs.getInt("port"));
                accounts.setAvatar(rs.getString("avatar"));
                accounts.setEmail(rs.getString("email"));
                accounts.setPassword(rs.getString("password"));
                accounts.setSalt(rs.getString("salt"));
                // TODO 类型问题
                // accounts.setBirthday(rs.getDate("birthday"));
                accounts.setStatus(rs.getInt("status"));
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            queryEvent.close();
        }


        return accounts;
```


### code snippets

```java
    /**
     * 通知所有好友，xxx上线了
     */
    public void notifyFriend(UserSocket userSocket) throws IOException {
        if (userID != null && userSocket.isOpen()) {
            DataWrapper data = new DataWrapper(CommandCode.NOTIFY_ONLINE, 200, DataType.PureString, userID + "上线了");
//            data.setType(DataType.PureString);
            data.setStatusCode(200);
            userSocket.send(data);
        }
    }

    public void notifyFriends(Vector<Friend> friends, SocketManager socketManager) {
        for (Friend friend : friends) {
            UserSocket userSocket = socketManager.find(friend.getTargetUserId());
            if (userSocket != null) {
                try {
                    userSocket.notifyFriend(userSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
```

### dispatcher

```java

//    public void dispatch(DataWrapper<?> request, SocketChannel socketChannel) {
//
//        UserSocket response = new UserSocket(socketChannel);
//
////        middleware(request, response);
//
//        switch (request.getUrl()) {
//            case REG:
//                userController.register();
//                break;
//            case LOG_IN:
//                userController.login();
//                break;
////            case SEND_MESSAGE:
////                sendMessage(request, response);
//            default:
//                notFound(request, response);
//        }
//    }

//    private void notFound(DataWrapper dataWrapper, UserSocket response) {
//    }
//
//    public void middleware(DataWrapper dataWrapper, UserSocket socketChannel) {
//        System.out.println(FormatTools.formatTimestamp(dataWrapper.getTime()) + " " + dataWrapper.getType() + " " + dataWrapper.getUrl());
//    }

```


```java
//        String sql = "insert into accounts (qnumber, nickname, password) values(?,?,?)";
//        String[] params = new String[]{user.getQQ(), user.getNickname(), user.getPassword()};
//        QueryEvent queryEvent = new QueryEvent(DatabaseConnection.getConnection());
//        int rs = -1;
//        try {
//            queryEvent.setAutoCommit(false);
////            rs = queryEvent.prepare(sql).setParams(params).executeUpdate();
//            rs = queryEvent.prepare(sql).setParams(user.getQQ(), user.getNickname(), user.getPassword()).executeUpdate();
//            queryEvent.commit();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            try {
//                queryEvent.rollback();
//            } catch (SQLException sqlException) {
//                sqlException.printStackTrace();
//            }
//        }
//        return rs;
```


```java

    public static byte[] pack() {


        // 构建 内容实体
        LoginReq req = LoginReq.newBuilder()
            .setUsername("haha")
            .setPassword("hehe")
            .setIsNeedKey(true)
            .build();

        // 计算 内容长度
        ByteArrayOutputStream oos = new ByteArrayOutputStream();
        int length = 0;

        try {
            req.writeTo(oos);
            length = oos.size();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(length);

        // 构建 header 实体
        MessageWrapper header = MessageWrapper.newBuilder()
            .setContentLength(length)
            .setContentType("content-type")
            .setTime(System.currentTimeMillis())
            .setToken("token: read from file")
            .setUrl("url: iqq://localhost:8080/add-card")
            .setFrom("current user")
            .setTo("send to user / group / system")
            .build();

        // 计算 header 长度
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            header.writeTo(os);
            System.out.println(os.size());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 计算
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // 写入包头标识
        try {
            baos.write(BytesUtil.int32ToByteArray(0xAB47));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(baos.size());

        baos.write(123456);

        System.out.println(baos.size());

        MessageWrapper messageWrapper = MessageWrapper.newBuilder().setContentLength(1123L).build();

        try {
            messageWrapper.writeTo(baos);
            System.out.println(baos.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(baos.toByteArray().length);
        System.out.println(BytesUtil.bytesToHexString(baos.toByteArray()));
        System.out.println(Arrays.toString(baos.toByteArray()));

        return null;
    }
```


```java
        /**
         * Run in the message receiver thread
         */
        public void readHandler(SelectionKey selectionKey) {
            // 要从selectionKey中获取到已经就绪的channel
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
//            try {
//                ByteBuffer byteBuffer = ByteBuffer.allocate(3096);
//                // 读取客户端请求信息
//                socketChannel.read(byteBuffer);
//                router.dispatch((DataWrapper<?>) Serial.byteBufferToObject(byteBuffer), socketChannel);
//            } catch (IOException | ClassNotFoundException ioe) {
//                ioe.printStackTrace();
//            }

            try {
                // 将channel再次注册到selector上，监听它的可读事件
                socketChannel.register(selector, SelectionKey.OP_READ);
            } catch (ClosedChannelException cce) {
                cce.printStackTrace();
            }
        }
```

```java

//            EventQueue.invokeLater(() -> {
//                JFrame frame = new JFrame("QQ Server Side");
//                frame.getContentPane().add(new JLabel("服务已启动"));
//                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                frame.setPreferredSize(new Dimension(300, 185));
//                frame.pack();
//                frame.setResizable(false);
//                frame.setVisible(true);
//            });
```

```java

    /**
     * 发送 登录 请求
     *
     * @param qq       昵称
     * @param password 密码
     */
    public void sendLoginReq(String qq, String password) {
        // 构建 内容实体
//        LoginApiProto.LoginReq req = LoginApiProto.LoginReq.newBuilder()
//            .setUsername("haha")
//            .setPassword("hehe")
//            .setIsNeedKey(true)
//            .build();
//        byte[] body = req.toByteArray();
        byte[] body = new byte[100];
        byte[] bytes = MessageProtocol.setup(body, "iqq://localhost:8080/", "string", "1234567", "2346578");

//        System.out.println(bytes.length);
//        System.out.println(BytesUtil.bytesToHexString(BytesUtil.read(bytes, 0, 4)));
//        System.out.println(BytesUtil.bytesToHexStringWithSpace(bytes));
//        System.out.println(cLen);
//        System.out.println(contentType);
//        System.out.println("开始标记：" + BytesUtil.byteArrayToInt32(startTag));
//        System.out.println("长度标记：" + BytesUtil.byteArrayToInt32(headerLengthTag));


//        for (int i = 0; i < 4; ++i)
//            headerLength[i] = bytes[idx++];
//        System.out.println("标记头长度：" + BytesUtil.byteArrayToInt32(headerLength));

//        System.out.println(BytesUtil.bytesToHexStringWithSpace(startTag));
//        System.out.println(BytesUtil.bytesToHexStringWithSpace(headerLength));
//        byte[] header = new byte[];
//        for (int i = 0; i < 4; ++i)
//            header[i] = bytes[idx++];

//        MessagePacket m = MessageProtocol.unpack(bytes);
//        System.out.println(m.getHeaderLength());
//        System.out.println((m.getHeader().getContentLength()));
//        System.out.println(m.getBody().length);
//        System.out.println(BytesUtil.bytesToHexStringWithSpace(m.getBody()));


        ByteBuffer buffer = ByteBuffer.allocate(1024);

        buffer.put(bytes);
        try {
            socket.send(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        User account = new User(qq, password);
//        DataWrapper<User> dataWrapper = new DataWrapper<User>(CommandCode.LOG_IN, 200, DataType.UserObject, account);
//        try {
//            socket.send(Serial.objectToByteBuffer(dataWrapper));
//            framesManager.setId(qq);
//            GlobalData.setMyAccount(qq);
//            System.out.println("Sent login request successfully");
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//            System.out.println("Sent login request failed");
//        }
    }

```


```java

//    public void dispatch(DataWrapper request, SocketChannel response) {
//        middleware(request, response);

//        switch (request.getUrl()) {
//            // register
//            case REG: {
//                routes.register(request, response);
//                break;
//            }
//            // sign in
//            case LOG_IN: {
//                routes.login(request, response);
//                break;
//            }
//            // send message
//            case SEND_MESSAGE: {
//                routes.sendMessage(request, response);
//                break;
//            }
//            // notify user about
//            case NOTIFY_ONLINE: {
//                routes.notifyFriends(request, response);
//                break;
//            }
//            default:
//                routes.notFound(request, response);
//        }
//    }

//    public void middleware(DataWrapper request, SocketChannel response) {
//        // for overriding
//        System.out.println("Response: " + FormatTools.formatTimestamp(request.getTime()) + " " + request.getUrl() + " " + request.getStatusCode());
//        System.out.println();
//    }
```
