package com.nxt.im;

import java.io.IOException;
// import com.nxt.im.client.Client;
// import com.nxt.im.ui.Chat;
import com.nxt.im.ui.LoginFrame;

/**
 * 应用程序客户端入口
 * 
 * @version v191204
 */
public final class App {
    // private static Client socketChannel;

    // static {
    //     try {
    //         socketChannel = new Client();
    //         socketChannel.init();
    //     } catch (IOException ioE) {
    //         ioE.printStackTrace();
    //     }
    // }

    // private App() {
    // }

    // /**
    //  * @return the socketChannel
    //  */
    // public static Client getSocketChannel() {
    //     return socketChannel;
    // }
    /**
     * Says hello to the world.
     * 
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        new LoginFrame();
        System.out.println("Create LoginFrame!");
    }
}
