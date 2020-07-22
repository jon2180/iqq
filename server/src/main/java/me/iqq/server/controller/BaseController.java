package me.iqq.server.controller;

import me.iqq.common.Controller;
import me.iqq.common.protocol.MessagePacket;

import java.nio.channels.SocketChannel;

public abstract class BaseController extends Controller {

    //    protected
    public abstract void handle(MessagePacket packet, SocketChannel channel);

}
