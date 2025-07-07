package com.assessment.epicor.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Response {

    public Response() {
    }

    private Integer count;
    private Map<String, Long> top5Words;
    private Set<String> top50Words;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Map<String, Long> getTop5Words() {
        return top5Words;
    }

    public void setTop5Words(Map<String, Long> top5Words) {
        this.top5Words = top5Words;
    }

    public Set<String> getTop50Words() {
        return top50Words;
    }

    public void setTop50Words(Set<String> top50Words) {
        this.top50Words = top50Words;
    }
}
