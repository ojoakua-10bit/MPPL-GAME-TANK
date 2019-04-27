package com.fluxhydravault.restfrontend.model;

import java.util.Date;
import java.util.List;

public class SearchResponse<T> {
    private Date timestamp;
    private T matched_result;
    private List<T> possible_result;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public T getMatched_result() {
        return matched_result;
    }

    public void setMatched_result(T matched_result) {
        this.matched_result = matched_result;
    }

    public List<T> getPossible_result() {
        return possible_result;
    }

    public void setPossible_result(List<T> possible_result) {
        this.possible_result = possible_result;
    }

    @Override
    public String toString() {
        return "SearchResponse{" +
                "timestamp=" + timestamp +
                ", matched_result=" + matched_result +
                ", possible_result=" + possible_result +
                '}';
    }
}
