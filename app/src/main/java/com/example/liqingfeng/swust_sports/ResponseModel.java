package com.example.liqingfeng.swust_sports;

public class ResponseModel {
    private Object data;
    private String code;
    private String token;

    public ResponseModel(Object data, String code, String token) {
        this.data = data;
        this.code = code;
        this.token = token;
    }
    public ResponseModel()
    {
        super();
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
