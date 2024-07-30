package com.task05.dto;

public class ResponseDto {

    private Integer statusCode;
    private EventDto event;

    public ResponseDto(Integer statusCode, EventDto event) {
        this.statusCode = statusCode;
        this.event = event;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public EventDto getEvent() {
        return event;
    }

    public void setEvent(EventDto event) {
        this.event = event;
    }
}
