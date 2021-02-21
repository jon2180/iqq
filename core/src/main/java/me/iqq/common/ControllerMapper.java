package me.iqq.common;

import me.iqq.common.api.MessageHeaderProto.MessageHeader.ContentType;

import java.util.concurrent.ConcurrentHashMap;

public class ControllerMapper<T extends Controller> extends ConcurrentHashMap<ContentType, T> {
}
