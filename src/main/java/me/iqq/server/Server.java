
package me.iqq.server;

import me.iqq.server.routes.Router;
import me.iqq.server.socket.NioServer;

import java.io.IOException;

/**
 * 用户程序服务端入口
 *
 * @version v191204
 */
public final class Server {

    private NioServer server;
    private Router router;

    public Server() {
        router = new Router();
        try {
            server = new NioServer(router);
            server.listen();
            server.close();
        } catch (IOException ioE) {
            System.exit(-1);
            ioE.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}

//            EventQueue.invokeLater(() -> {
//                JFrame frame = new JFrame("QQ Server Side");
//                frame.getContentPane().add(new JLabel("服务已启动"));
//                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                frame.setPreferredSize(new Dimension(300, 185));
//                frame.pack();
//                frame.setResizable(false);
//                frame.setVisible(true);
//            });

