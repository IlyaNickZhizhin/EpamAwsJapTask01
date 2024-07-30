package com.task05.dto;

import java.util.Map;

public class RequestDto {
    private Integer principalId;
    private Object content;

    public RequestDto() {
    }

    public Integer getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(Integer principalId) {
        this.principalId = principalId;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}