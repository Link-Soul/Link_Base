package com.link.core.common.exception.code;


/**
 * 错误码
 *
 * @author Link
 * @version V1.0
 * @date 2020年3月18日
 */
public enum BaseResponseCode implements ResponseCodeInterface {
    /**
     * 前三位区分业务功能，后三位区分具体错误   100为系统通用功能
     */
    SUCCESS(100000, "操作成功"),

    OBJECT_IS_NOT_NULL(100001, "对象已存在"),

    OBJECT_IS_NULL(100002, "找不到对象"),
    METHOD_ARGUMENT_NOT_VALID_EXCEPTION(100003, "方法参数校验异常"),
    GET_PROPERTIES_ERROR(100004, "方法参数校验异常"),
    TEMPLATE_RENDERING_FAILED(100005, "渲染模板失败"),
    UPLOAD_FAILED(100006, "上传失败"),
    FILE_NOT_EXIST(100007, "文件不存在"),
    UPLOAD_SUCCESS(100008, "上传成功"),
    UPLOAD_FAIL(100009, "上传失败"),
    ILLEGAL_DATA(100010, "非法的数据格式"),
    PARAM_NULL(100011, "请填写请求数据"),

    /**
     * 流水号已超长
     */
    SERIAL_NUMBER_ALREADY_TOO_LONG(100011, "流水号已超长"),
    IMPORT_EXCEL_TEMPLATE_ERROR(100012, "模板错误，请重新下载模板"),
    TOO_MANY_REQUEST(100997, "请求过于频繁，请稍候再试"),
    OPERATION_ERROR(100998, "操作失败"),
    SYSTEM_BUSY(100999, "系统繁忙，请稍候再试"),
    /**
     * 前三位区分业务功能，后三位区分具体错误   101为登录鉴权相关功能
     */
    NOT_TOKEN(101001, "未能读取到有效凭证，请重新登录"),

    INVALID_TOKEN(101002, "登录凭证已过期，请重新登录"),
    UNAUTHORIZED_ERROR(101003, "无操作权限"),

    /**
     * 前三位区分业务功能，后三位区分具体错误   102为用户相关功能
     */
    CHANGE_PASSWORD_ARGUMENT_ERROR(102001, "旧密码与新密码不能为空"),
    CAPTCHA_ERROR(102002, "验证码错误"),
    NEW_PASSWORD_EQUALS_OLD_PASSWORD(102003, "新密码不能与旧密码相同"),
    OLD_PASSWORD_MATCH_FAILED(102004, "旧密码错误"),
    USER_LOCKED(102005, "该用户已被锁定，请联系运营人员"),
    PASSWORD_ERROR(102006, "用户名或密码错误"),
    CAN_NOT_DELETE_USER(102007, "为自动创建的公司用户，请在公司删除"),
    CAN_NOT_UPDATE_USER(102008, "为自动创建的公司用户，请在公司修改"),
    EMAIL_CAPTCHA_EXISTS(102009, "邮箱验证码已发送，请稍后重试"),
    USER_PENDING(102010, "还在审核中，请过一段时间再试"),
    USER_EMAIL_REGISTERED(102011, "该邮箱已注册，请直接登录"),
    EMAIL_CAPTCHA_ERROR(102012, "邮箱验证码错误"),
    USER_TYPE_ERROR(102013, "仅个人用户和公司用户注册"),
    COMPANY_LICENSE_EMPTY(102014, "公司用户必须上传营业执照"),

    /**
     * 前三位区分业务功能，后三位区分具体错误   103为笔记相关功能
     */
    NOTES_EXISTS(103001, "笔记已存在!"),
    NOTES_NOT_EXISTS(103002, "笔记不存在!"),


    /**
     * 前三位区分业务功能，后三位区分具体错误   113为通知相关功能
     */
    NOTICE_TYPE_EXISTS(113001, "通知类型已存在!"),
    ;
    /**
     * 错误码
     */
    private final int code;
    /**
     * 错误消息
     */
    private final String msg;

    BaseResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
