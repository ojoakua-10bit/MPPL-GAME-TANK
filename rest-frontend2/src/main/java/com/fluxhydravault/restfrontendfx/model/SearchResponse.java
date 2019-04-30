package com.fluxhydravault.restfrontendfx.model;

import java.util.Date;
import java.util.List;

public class SearchResponse<T> {
    private Date timestamp;
    private T matched_result;
    private List<T> possible_results;

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

    public List<T> getPossible_results() {
        return possible_results;
    }

    public void setPossible_results(List<T> possible_results) {
        this.possible_results = possible_results;
    }

    @Override
    public String toString() {
        return "SearchResponse{" +
                "timestamp=" + timestamp +
                ", matched_result=" + matched_result +
                ", possible_results=" + possible_results +
                '}';
    }
}

