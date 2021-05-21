package com.neutron.im.config;

public class AppConstants {
    public static final String STATIC_FILE_STORAGE_PATH_SERVER = "/www/wwwroot/static";

    /**
     * 静态文件保存路径
     */
//    public static final String STATIC_FILE_STORAGE_PATH = STATIC_FILE_STORAGE_PATH_SERVER;
    public static final String STATIC_FILE_STORAGE_PATH = "D:/Downloads/static";
    /**
     * 头像保存路径
     */
    public static final String AVATAR_STORAGE_PATH = STATIC_FILE_STORAGE_PATH + "/avatar";
    /**
     * http 访问头像的链接
     */
    public static final String AVATAR_BASE_URL_SERVER = "//assets.wuog.top/avatar";

//    public static final String AVATAR_BASE_URL = AVATAR_BASE_URL_SERVER;
    public static final String AVATAR_BASE_URL = "//localhost:3002/avatar";
    /**
     * http 访问静态资源的链接
     *
     * @param filename
     * @return
     */
    public static final String STATIC_BASE_URL_SERVER = "//assets.wuog.top/static";
//    public static final String STATIC_BASE_URL = STATIC_BASE_URL_SERVER;
    public static final String STATIC_BASE_URL = "//localhost:3002/static";

    public static String getAssetUrl(String filename) {
        if (filename == null || ".".equals(filename)) {
            return filename;
        }
        return STATIC_BASE_URL + "/" + filename;
    }

    public static String getAvatarUrl(String filename) {
        if (filename == null || "".equals(filename)) {
            return filename;
        }
        return AVATAR_BASE_URL + "/" + filename;
    }
}
