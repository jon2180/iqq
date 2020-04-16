package me.iqq.client.model;

import me.iqq.client.GlobalData;
import me.iqq.client.ui.ChatFrame;
import me.iqq.client.ui.LoginFrame;
import me.iqq.client.ui.MainFrame;
import me.iqq.client.ui.RegisterFrame;

import java.util.HashMap;
import java.util.Map;

public class FramesManager {
    public enum State {
        Login,
        Reg,
        Main
    }

    private State currentState;

    private HashMap<String, ChatFrame> chatFrames;

    private String id;

    private LoginFrame loginFrame;
    private RegisterFrame regFrame;
    private MainFrame frame;

    // is auto open chat frame
    private boolean isAutoOpen = true;

    public FramesManager() {
        chatFrames = new HashMap<>();
        currentState = State.Login;
    }

    public void setId(String newId) {
        id = newId;
    }

    public void switchToLogin() {
        if (regFrame != null && regFrame.isVisible())
            regFrame.dispose();

        if (!chatFrames.isEmpty()) {
            for (Map.Entry<String, ChatFrame> item : chatFrames.entrySet()) {
                ChatFrame cf = (ChatFrame) item.getValue();
                cf.dispose();
            }
            chatFrames.clear();
        }

        if (loginFrame == null)
            loginFrame = new LoginFrame(this);
        else
            loginFrame.setVisible(true);
        currentState = State.Login;
    }

    public void switchToReg() {
        if (loginFrame != null)
            loginFrame.dispose();

        if (!chatFrames.isEmpty()) {
            for (Map.Entry<String, ChatFrame> item : chatFrames.entrySet()) {
                ChatFrame cf = item.getValue();
                cf.dispose();
            }
            chatFrames.clear();
        }

        if (regFrame == null)
            regFrame = new RegisterFrame(this);
        else
            regFrame.setVisible(true);
        currentState = State.Reg;
    }

    public void switchToMain() {
        if (loginFrame != null)
            loginFrame.dispose();

        if (regFrame != null)
            regFrame.dispose();

        if (!chatFrames.isEmpty()) {
            for (Map.Entry<String, ChatFrame> item : chatFrames.entrySet()) {
                ChatFrame cf = (ChatFrame) item.getValue();
                cf.dispose();
            }
            chatFrames.clear();
        }

        frame = new MainFrame(this);
        currentState = State.Main;
    }

    public MainFrame getFrame() {
        return frame;
    }

    public RegisterFrame getRegFrame() {
        return regFrame;
    }

    public LoginFrame getLoginFrame() {
        return loginFrame;
    }

    public ChatFrame createChatFrame(String qq) {
        ChatFrame chatFrame;
        if (!chatFrames.containsKey(qq)) {
            chatFrame = new ChatFrame(GlobalData.getMyAccount(), qq);
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
