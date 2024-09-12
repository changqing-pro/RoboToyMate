package com.robot.entity.domain;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * @author songzonglin
 * @create 2024/9/11 17:29
 */

@Data
public class JsonResult<T> implements Serializable {
    public static final int SUCCESS;
    public static final int FAIL;
    private int status;
    private String code;
    private String msg;
    private T data;

    public JsonResult() {
        this.status = Constants.FAIL;
    }

    public JsonResult(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public JsonResult(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        if (ObjectUtil.isNotNull(data)) {
            this.data = data;
        }
    }

    public JsonResult(int status, String code, String msg) {
        this.status = status;
        this.code = code;
        this.msg = msg;
    }

    public JsonResult(int status, String code, String msg, T data) {
        this.status = status;
        this.code = code;
        this.msg = msg;
        if (ObjectUtil.isNotNull(data)) {
            this.data = data;
        }

    }

    public static JsonResult success() {
        return success("操作成功");
    }

    public static <T> JsonResult success(T data) {
        return success("操作成功", data);
    }

    public static JsonResult success(String msg) {
        return success((String) null, msg, (Object) null);
    }

    public static <T> JsonResult<T> success(String msg, T data) {
        return success((String) null, msg, data);
    }

    public static <T> JsonResult<T> success(String code, String msg, T data) {
        return new JsonResult(Constants.SUCCESS, code, msg, data);
    }

    public static JsonResult success(String code, String msg) {
        return new JsonResult(Constants.SUCCESS, code, msg, (Object) null);
    }

    public static JsonResult error() {
        return error("操作失败");
    }

    public static JsonResult error(String msg) {
        return new JsonResult(Constants.FAIL, (String) null, msg);
    }

    public static <T> JsonResult error(String msg, T data) {
        return new JsonResult(Constants.FAIL, msg, data);
    }

    public static <T> JsonResult error(String code, String msg, T data) {
        return new JsonResult(Constants.FAIL, code, msg, data);
    }

    public static JsonResult error(String code, String msg) {
        return new JsonResult(Constants.FAIL, code, msg, (Object) null);
    }

    static {
        SUCCESS = Constants.SUCCESS;
        FAIL = Constants.FAIL;
    }
}
