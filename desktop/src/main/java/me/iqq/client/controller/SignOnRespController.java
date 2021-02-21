package me.iqq.client.controller;

import me.iqq.client.ui.model.FramesManager;
import me.iqq.common.protocol.DataPacket;

import java.nio.channels.SocketChannel;

public class SignOnRespController extends BaseController {
    public SignOnRespController(FramesManager fm, SocketChannel channel) {
        super(fm, channel);
    }

    @Override
    public void handle(DataPacket packet, SocketChannel channel) {

    }
}
