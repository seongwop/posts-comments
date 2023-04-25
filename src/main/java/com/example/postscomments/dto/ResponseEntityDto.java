package com.example.postscomments.dto;

import com.example.postscomments.util.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseEntityDto<T> {
    private StatusCode statusCode;
    private String msg;
    private T data;

    public ResponseEntityDto(StatusCode statusCode, String msg, T data) {
        this.statusCode = statusCode;
        this.msg = msg;
        this.data = data;
    }

    public ResponseEntityDto(StatusCode statusCode, String msg) {
        this.statusCode = statusCode;
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


