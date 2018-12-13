package com.jlt.common.util;

import java.io.Serializable;


@SuppressWarnings("serial")
public class ApiResultMessage implements Serializable {

    /**
     * 返回是否成功标志
     */
    private String status;
    /**
     * 返回描述
     */
    private String message;

    private Object data;


    public ApiResultMessage() {
        super();
    }

    public ApiResultMessage(String status) {
        super();
        this.status = status;
    }

    public ApiResultMessage(String status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
