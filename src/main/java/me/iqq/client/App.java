package me.iqq.client;

import me.iqq.client.config.ServerInfo;
import me.iqq.client.controller.RequestUtils;
import me.iqq.client.model.FramesManager;
import me.iqq.client.model.SocketWrapper;
import me.iqq.client.socket.Router;

import java.io.IOException;

/**
 * 应用程序客户端入口
 *
 * @version v191204
 */
public final class App {
    private FramesManager framesManager;
    private SocketWrapper socketWrapper;

    private boolean hasLogin = false;

    public App() throws IOException {
        framesManager = new FramesManager();
        Router router = new Router(framesManager);
        framesManager.switchToLogin();

        // connect to server
        socketWrapper = new SocketWrapper(SocketWrapper.getConnection(ServerInfo.IP, ServerInfo.PORT, router));
        RequestUtils.setSocketWrapper(socketWrapper);
        RequestUtils.setFramesManager(framesManager);

        socketWrapper.startReceiverThread();
    }

    /**
     * Says hello to the world.
     * 以登录为入口
     *
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        try {
            new App();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
