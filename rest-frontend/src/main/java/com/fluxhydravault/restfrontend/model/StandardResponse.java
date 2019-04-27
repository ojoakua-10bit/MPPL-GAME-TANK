package com.fluxhydravault.restfrontend.model;

import java.util.Date;

public class StandardResponse<T> {
    private Date timestamp;
    private String response;
    private String message;
    private T data;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "StandardResponse{" +
                "timestamp=" + timestamp +
                ", response='" + response + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
