
package me.iqq.server;

import me.iqq.common.ControllerMapper;
import me.iqq.common.api.MessageHeaderProto;
import me.iqq.common.socket.EventDispatcher;
import me.iqq.common.socket.SelectionKeyHandler;
import me.iqq.common.socket.impl.EventDispatcherImpl;
import me.iqq.common.socket.impl.SelectionKeyHandlerImpl;
import me.iqq.server.controller.BaseController;
import me.iqq.server.controller.MessageController;
import me.iqq.server.controller.SignInController;
import me.iqq.server.controller.SignOnController;
import me.iqq.server.socket.NioServer;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * 用户程序服务端入口
 *
 * @version v200702
 */
public class Server {

    public Server(String... args) {
        NioServer server = null;

        // 处理对象映射
        ControllerMapper<BaseController> mapper = new ControllerMapper<>();
        mapper.register(MessageHeaderProto.MessageHeader.ContentType.SIGN_IN, new SignInController());
        mapper.register(MessageHeaderProto.MessageHeader.ContentType.SIGN_ON, new SignOnController());
        mapper.register(MessageHeaderProto.MessageHeader.ContentType.MESSAGE, new MessageController());

        // 事件分发
        EventDispatcher dispatcher = new EventDispatcherImpl<>(mapper);
        // selection key 处理
        SelectionKeyHandler keyHandler = new SelectionKeyHandlerImpl(dispatcher);

        try {
            server = new NioServer(keyHandler, Integer.parseInt(serverConf.getString("port")));
            server.start();
        } catch (IOException ioE) {
            server.stop();
            System.exit(-1);
            ioE.printStackTrace();
        }
    }

    private static final ResourceBundle serverConf;

    static {
        serverConf = ResourceBundle.getBundle("application");
    }
}


