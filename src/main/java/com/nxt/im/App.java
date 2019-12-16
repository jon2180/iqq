package com.nxt.im;

// import java.io.IOException;
// import com.nxt.im.client.Client;
// import com.nxt.im.ui.Chat;

import com.nxt.im.client.ClientRouter;
import com.nxt.im.ui.LoginFrame;

/**
 * 应用程序客户端入口
 *
 * @version v191204
 */
public final class App {
    /**
     * Says hello to the world.
     *
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        new LoginFrame();


        System.out.println("Create LoginFrame!");
    }

    /**
     * 测试登录
     *
     * @param args
     */
    public static void reg(String[] args) {
        if (ClientRouter.userReg("hah", "ainiya")) {
            System.out.println("注册成功");
        } else {
            System.out.println("注册失败");
        }
    }
}
