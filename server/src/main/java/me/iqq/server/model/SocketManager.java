package me.iqq.server.model;

import me.iqq.common.protocol.User;

import java.util.HashMap;
import java.util.Iterator;

public class SocketManager {


    private HashMap<String, UserSocket> socketMap;

    public SocketManager() {
        socketMap = new HashMap<>();
    }

    /**
     * @return the socketMap
     */
    public HashMap<String, UserSocket> getSocketMap() {
        return socketMap;
    }

    public boolean containsKey(String key) {
        return socketMap.containsKey(key);
    }

    public UserSocket get(String key) {
        return socketMap.get(key);
    }

    public UserSocket find(String key) {
        return containsKey(key) ? get(key) : null;
    }

    public void put(String key, UserSocket value) {
        socketMap.put(key, value);
    }

    public void remove(String key) {
        socketMap.remove(key);
    }

    public void refresh() {
        for (HashMap.Entry<String, UserSocket> el : socketMap.entrySet()) {
            UserSocket userSocket = el.getValue();

            if (userSocket.isOpen())
                remove(el.getKey());
        }
    }
}
