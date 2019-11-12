package com.intuit.interview.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UrlShortenRequest {
    private String url;

    @JsonCreator
    public UrlShortenRequest() {}

    @JsonCreator
    public UrlShortenRequest(@JsonProperty("url") String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}