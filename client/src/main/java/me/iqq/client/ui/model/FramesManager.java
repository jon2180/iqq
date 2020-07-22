package me.iqq.client.ui.model;

import me.iqq.client.ui.ChatFrame;
import me.iqq.client.ui.LoginFrame;
import me.iqq.client.ui.MainFrame;
import me.iqq.client.ui.RegisterFrame;

import java.util.HashMap;
import java.util.Map;

public class FramesManager {
    /**
     * 当前用户状态 枚举类型
     */
    public enum State {
        Login,
        Reg,
        Main,
    }

    private State currentState;

    private final HashMap<String, ChatFrame> chatFrames;
    private LoginFrame loginFrame;
    private RegisterFrame regFrame;
    private MainFrame frame;

    public FramesManager() {
        chatFrames = new HashMap<>();
        currentState = State.Login;
    }

    public void switchToLogin() {
        disposeRegisterFrame();
        disposeAllChatFrame();

        if (loginFrame == null)
            loginFrame = new LoginFrame(this);
        else
            loginFrame.setVisible(true);
        currentState = State.Login;
    }

    public void switchToReg() {
        disposeLoginFrame();
        disposeAllChatFrame();

        if (regFrame == null)
            regFrame = new RegisterFrame(this);
        else
            regFrame.setVisible(true);
        currentState = State.Reg;
    }

    public void switchToMain() {
        disposeLoginFrame();
        disposeRegisterFrame();
        disposeAllChatFrame();

        frame = new MainFrame(this);
        currentState = State.Main;
    }


    private void disposeLoginFrame() {
        if (loginFrame != null)
            loginFrame.dispose();
    }

    private void disposeRegisterFrame() {
        if (regFrame != null)
            regFrame.dispose();
    }

    private void disposeAllChatFrame() {
        if (!chatFrames.isEmpty()) {
            for (Map.Entry<String, ChatFrame> item : chatFrames.entrySet()) {
                ChatFrame cf = item.getValue();
                cf.dispose();
            }
            chatFrames.clear();
        }
    }

    public ChatFrame createChatFrame(String qq) {
        ChatFrame chatFrame;
        if (!chatFrames.containsKey(qq)) {
            chatFrame = new ChatFrame("1234567", qq);
            chatFrames.put(qq, chatFrame);
        } else {
            chatFrame = chatFrames.get(qq);
            chatFrame.setVisible(true);
        }
        return chatFrame;
    }

    public void disposeChatFrame(String qq) {
        if (chatFrames.containsKey(qq)) {
            chatFrames.get(qq).dispose();
            chatFrames.remove(qq);
        }
    }
}
// is auto open chat frame
//    private boolean isAutoOpen = true;

//    private String id;
//    public void setId(String newId) {
//        id = newId;
//    }

//    public MainFrame getFrame() {
//        return frame;
//    }
//
//    public RegisterFrame getRegFrame() {
//        return regFrame;
//    }
//
//    public LoginFrame getLoginFrame() {
//        return loginFrame;
//    }
