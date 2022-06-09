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
    USER_PORTAL_IS_NULL(7014,"用户主页信息不存在"),
    MENU_NOT_EXIST(7015,"菜单信息不存在"),
    THE_USER_NO_ORDER(7016,"该用户没有任何订单"),
    ORDER_ERROR(7017,"订单付款异常"),
    INDEX_EXIST(7018,"该index已经被使用"),
    FILE_DOWNLOAD_FAIL(7019,"文件下载失败"),
    FILE_UPLOAD_FAIL(7020,"文件上传失败"),
    USER_PASSWORD_UPDATE_FAIL(7021, "用户密码更新失败"),
    WX_PAY_ORDER_FAIL(7022, "微信拉起失败"),
    WX_PAY_BACK_SIGN_FAIL(7023, "微信回调签名验证失败"),
    WX_PAY_BACK_ORDER_FAIL(7024, "微信回调订单不存在或已经支付"),
    ORDER_ID_IS_NULL(7025, "订单信息不存在"),
    WX_PAY_ORDER_CHECK_ERROR(7026, "微信支付查询订单失败"),
    EMAIL_VERIFY_CODE_ERROR(7002, "验证码错误");
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