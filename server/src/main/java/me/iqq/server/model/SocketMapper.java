package me.iqq.server.model;

import java.util.HashMap;

public class SocketMapper {

    private final HashMap<String, UserSocket> socketMap;

    public SocketMapper() {
        socketMap = new HashMap<>();
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

            if (userSocket.getChannel() == null || !userSocket.getChannel().isOpen())
                remove(el.getKey());
        }
    }
}
