package com.example.postscomments.dto;

import com.example.postscomments.util.StatusCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseEntityDto<T> {
    private int statusCode;
    private String msg;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public ResponseEntityDto(StatusCode statusCode, String msg, T data) {
        this.statusCode = statusCode.getHttpStatus();
        this.msg = msg;
        this.data = data;
    }

    public ResponseEntityDto(StatusCode statusCode, String msg) {
        this.statusCode = statusCode.getHttpStatus();
        this.msg = msg;
    }

    public static <T> ResponseEntityDto<T> of(StatusCode statusCode, String msg, T data) {
        return new ResponseEntityDto<>(statusCode, msg, data);
    }

    public static <T> ResponseEntityDto<T> of(StatusCode statusCode, String msg) {
        return new ResponseEntityDto<>(statusCode, msg);
    }

    public static <T> ResponseEntityDto<T> exceptionStatus(String msg) {
        return new ResponseEntityDto<>(StatusCode.BAD_REQUEST, msg);
    }
}


