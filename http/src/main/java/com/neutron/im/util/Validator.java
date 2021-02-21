package com.neutron.im.util;

public class Validator {
    public static boolean isEmail(String email) {
        return "^[A-Za-z0-9]+([.+-_][a-zA-Z0-9]+)*@[A-Za-z0-9]+([.-][a-zA-Z0-9]+)*\\.[A-Za-z]{2,14}$".matches(email);
    }

    /**
     * 用户名验证 正则： "^[^0-9][\\w_]{5,9}$"
     *
     * @param name 用户名
     * @return 验证结果
     */
    public static boolean checkName(String name) {
        // "[A-Za-z0-9_\\-\\u4e00-\\u9fa5]+"
        return name.matches("^[^0-9][\\w_]{5,9}$");
    }

    /**
     * 移动电话号码验证 正则格式标准 "^[1][3456789][0-9]{9}$"
     *
     * @param tel 电话号码
     * @return 验证结果
     */
    public static boolean checkMobilePhone(String tel) {
        return tel.matches("^[1][3456789][0-9]{9}$");
    }

    /**
     * 密码验证 正则："^[\\w_]{6,20}$"
     *
     * @param pwd 密码
     * @return 验证结果
     */
    public static boolean checkPassword(String pwd) {
        return pwd.matches("^[\\w_]{6,20}$");
    }


    /**
     * 复杂密码验证 正则： "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*?[#?!@$%^&*-]).{6,}$"
     *
     * @param pwd 密码
     * @return 验证结果
     */
    public static boolean checkComplexPassword(String pwd) {
        return pwd.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*?[#?!@$%^&*-]).{6,}$");
    }

    public static boolean isPassword(String password) {
        return "^\\w{6,16}$".matches(password);
    }
}
