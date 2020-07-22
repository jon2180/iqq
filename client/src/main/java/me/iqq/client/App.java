package me.iqq.client;

import lombok.extern.slf4j.Slf4j;
import me.iqq.client.controller.BaseController;
import me.iqq.client.controller.MessageReceiveController;
import me.iqq.client.controller.SignInRespController;
import me.iqq.client.controller.SignOnRespController;
import me.iqq.client.ui.model.FramesManager;
import me.iqq.client.socket.NioClient;
import me.iqq.common.ControllerMapper;
import me.iqq.common.api.MessageHeaderProto;
import me.iqq.common.socket.EventDispatcher;
import me.iqq.common.socket.SelectionKeyHandler;
import me.iqq.common.socket.impl.EventDispatcherImpl;
import me.iqq.common.socket.impl.SelectionKeyHandlerImpl;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * 应用程序客户端入口
 *
 * @version v191204
 */
@Slf4j
public class App {

    /**
     * 窗口管理器
     */
    private static FramesManager framesManager = new FramesManager();

    /**
     * 连接器
     */
    private static NioClient nc = null;

    public static void startApplication(String... args) {
        try {
            ControllerMapper<BaseController> mapper = new ControllerMapper<>();
            mapper.register(MessageHeaderProto.MessageHeader.ContentType.SIGN_IN_RESP, new SignInRespController(framesManager, null));
            mapper.register(MessageHeaderProto.MessageHeader.ContentType.SIGN_ON_RESP, new SignOnRespController(framesManager, null));
            mapper.register(MessageHeaderProto.MessageHeader.ContentType.MESSAGE, new MessageReceiveController(framesManager, null));

            EventDispatcher dispatcher = new EventDispatcherImpl<>(mapper);
            SelectionKeyHandler keyHandler = new SelectionKeyHandlerImpl(dispatcher);

            nc = new NioClient(
                new InetSocketAddress(ConfigurationLoader.getString("ip"), ConfigurationLoader.getInt("port")),
                keyHandler
            );
            nc.getReceiverThread().start();
            App.getFramesManager().switchToLogin();

            log.info("Application Version: " + "v0.1.0");
            log.info("Protocol Version: " + ConfigurationLoader.getString("protocol_version"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void start() {
        nc.getReceiverThread().start();
    }

    public static void shutdown() {
        nc.shutdown();
    }

    public static void setFramesManager(FramesManager fm) {
        framesManager = fm;
    }

    public static SocketChannel getSocketChannel() {
        return nc.getSocketChannel();
    }

    public static FramesManager getFramesManager() {
        return framesManager;
    }
}

