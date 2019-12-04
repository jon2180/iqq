package com.nxt.im;

import com.nxt.im.ui.Chat;

/**
 * 应用程序客户端入口
 * @version v191204
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * 
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        new Chat().start();
        System.out.println("Hello World!");

    }
}
