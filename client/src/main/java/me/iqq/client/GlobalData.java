package me.iqq.client;

import me.iqq.client.model.FramesManager;
import me.iqq.client.model.SocketWrapper;
import me.iqq.common.protocol.Friend;

import java.util.Vector;

public class GlobalData {

    private static String myAccount;
    private static Vector<Friend> friends;
    private static FramesManager framesManager;
    private static SocketWrapper socketWrapper;

    public static Vector<Friend> getFriends() {
        return friends;
    }

    public static void setFriends(Vector<Friend> newFriends) {
        GlobalData.friends = newFriends;
    }

    public static String getMyAccount() {
        return myAccount;
    }

    public static void setMyAccount(String newId) {
        GlobalData.myAccount = newId;
    }

    public static FramesManager getFramesManager() {
        return framesManager;
    }

    public static void setFramesManager(FramesManager framesManager) {
        GlobalData.framesManager = framesManager;
    }

    public static SocketWrapper getSocketWrapper() {
        return socketWrapper;
    }

    public static void setSocketWrapper(SocketWrapper socketWrapper) {
        GlobalData.socketWrapper = socketWrapper;
    }
}
