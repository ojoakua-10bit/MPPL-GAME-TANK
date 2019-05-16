package com.fluxhydravault.restfrontendfx.model;

import java.util.Date;
import java.util.List;

public class SearchResponse<T> {
    private Date timestamp;
    private T matchedResult;
    private List<T> possibleResults;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public T getMatchedResult() {
        return matchedResult;
    }

    public void setMatchedResult(T matchedResult) {
        this.matchedResult = matchedResult;
    }

    public List<T> getPossibleResults() {
        return possibleResults;
    }

    public void setPossibleResults(List<T> possibleResults) {
        this.possibleResults = possibleResults;
    }

    @Override
    public String toString() {
        return "SearchResponse{" +
                "timestamp=" + timestamp +
                ", matchedResult=" + matchedResult +
                ", possibleResults=" + possibleResults +
                '}';
    }
}

