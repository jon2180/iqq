package com.neutron.im.config;

public class AppConstants {
    /**
     * 静态文件保存路径
     */
    public static final String STATIC_FILE_STORAGE_PATH = "D:/Downloads/static";

    /**
     * 头像保存路径
     */
    public static final String AVATAR_STORAGE_PATH = STATIC_FILE_STORAGE_PATH + "/avatar";

    /**
     * http 访问头像的链接
     */
    public static final String AVATAR_BASE_URL = "//localhost:3002/avatar";

    public static String getAvatarUrl(String filename) {
        if (filename == null || "".equals(filename)) {
            throw new NullPointerException("Empty Filename");
        }
        return AVATAR_BASE_URL + "/" + filename;
    }
}
