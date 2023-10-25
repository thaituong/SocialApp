package com.example.socialmediaapp.dto;

import java.io.Serializable;

public class ResponseDTO implements Serializable {
    private int code;
    private String status;
    private String message;
    private ResultDTO result;

    public ResponseDTO(int code, String status, String message, ResultDTO result) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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

    public ResultDTO getResult() {
        return result;
    }

    public void setResult(ResultDTO result) {
        this.result = result;
    }
}
