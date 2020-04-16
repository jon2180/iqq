package me.iqq.client.socket;

import me.iqq.client.controller.Routes;
import me.iqq.client.model.FramesManager;
import me.iqq.common.protocol.DataWrapper;
import me.iqq.common.utils.FormatTools;

import java.nio.channels.SocketChannel;

public class Router {

    Routes routes;

    private FramesManager framesManager;

    public Router(FramesManager fm) {
        framesManager = fm;
        routes = new Routes(framesManager);
    }

    public void dispatch(DataWrapper request, SocketChannel response) {
        middleware(request, response);

        switch (request.getUrl()) {
            // register
            case REG: {
                routes.register(request, response);
                break;
            }
            // sign in
            case LOG_IN: {
                routes.login(request, response);
                break;
            }
            // send message
            case SEND_MESSAGE: {
                routes.sendMessage(request, response);
                break;
            }
            // notify user about
            case NOTIFY_ONLINE: {
                routes.notifyFriends(request, response);
                break;
            }
            default:
                routes.notFound(request, response);
        }
    }

    public void middleware(DataWrapper request, SocketChannel response) {
        // for overriding
        System.out.println("Response: " + FormatTools.formatTimestamp(request.getTime()) + " " + request.getUrl() + " " + request.getStatusCode());
        System.out.println();
    }
}
