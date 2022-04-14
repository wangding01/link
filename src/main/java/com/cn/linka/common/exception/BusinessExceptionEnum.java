package com.cn.linka.common.exception;

/**
 * @program: linka
 * @description:
 * @author: wangding
 * @create: 2022-04-14 10:49
 */
public enum BusinessExceptionEnum {
    EMAIL_HAS_REGISTERED(7001, "该邮箱已经注册过"),
    USERNAME_PASSWORD_ERROR(7003, "用户名或密码错误"),
    USER_ID_ISNULL(7008, "用户id不能为空"),
    USER_MSG_UPDATE_FAIL(7009, "用户信息更新失败"),
    USER_MSG_NOT_EXIST(7010, "用户信息不存在"),
    USER_PORTAL_UPDATE_FAIL(7011, "用户主页信息修改失败"),
    AUTHORIZE_IS_NOT_NULL(7012, "Authorize不能为空"),
    AUTHORIZE_VERIFY_FAIL(7013, "Authorize校验失败"),
    EMAIL_VERIFY_CODE_ERROR(7002, "邮箱验证码错误");
    int code;
    String name;

    BusinessExceptionEnum(int code, String name) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }
}