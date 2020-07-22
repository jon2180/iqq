package me.iqq.common;

import me.iqq.common.api.MessageHeaderProto.MessageHeader.ContentType;

import java.util.HashMap;

public class ControllerMapper<T extends Controller> {

    private final HashMap<ContentType, T> map;

    public ControllerMapper() {
        map = new HashMap<>();
    }

    public void register(ContentType name, T handler) {
        map.put(name, handler);
    }

    public void clear() {
        map.clear();
    }

    public HashMap<ContentType, T> toMap() {
        return map;
    }

    public boolean containsKey(ContentType contentType) {
        return map.containsKey(contentType);
    }

    public T get(ContentType contentType) {
        // TODO create a default value of MessageHandler
        return map.getOrDefault(contentType, null);
    }
}
