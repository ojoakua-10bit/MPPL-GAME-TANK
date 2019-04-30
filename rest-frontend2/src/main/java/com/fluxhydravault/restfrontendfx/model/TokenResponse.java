package com.fluxhydravault.restfrontendfx.model;

import java.util.Date;

public class TokenResponse<T> {
    private Date timestamp;
    private String token;
    private T data;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TokenResponse{" +
                "timestamp=" + timestamp +
                ", token='" + token + '\'' +
                ", data=" + data +
                '}';
    }
}
