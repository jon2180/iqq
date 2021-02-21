package me.iqq.client.request;


import me.iqq.client.App;

/**
 * Request 对象装配中心
 *
 * @date 20200701
 */
public class RequestFactory {
    private static UserRequest userRequest = null;

    public static UserRequest getUserRequest() {
        // lazy init
        if (userRequest == null)
            userRequest = new UserRequest(App.getFramesManager(), App.getSocketChannel());
        return userRequest;
    }
}
