package com.nxt.im;

import com.nxt.im.ui.LoginFrame;

/**
 * 应用程序客户端入口
 *
 * @version v191204
 */
public final class App {
    /**
     * Says hello to the world.
     * 以登录为入口
     *
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        LoginFrame.getInstance();
        System.out.println("Create LoginFrame!");
    }
}
