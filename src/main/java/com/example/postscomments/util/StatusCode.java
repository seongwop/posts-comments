package com.example.postscomments.util;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum StatusCode {

    OK("OK", HttpStatus.OK.value()),
    BAD_REQUEST("BAD_REQUEST", HttpStatus.BAD_REQUEST.value()),

    // ----------------------------------------------------------

    SIGNUP_SUCCESS("회원 가입 성공", HttpStatus.OK.value()),
    LOGIN_SUCCESS("로그인 성공", HttpStatus.OK.value()),
    CHECK_POST_SUCCESS("게시글 조회 성공", HttpStatus.OK.value()),
    POST_CREATE_SUCCESS("게시글 생성 성공", HttpStatus.OK.value()),
    POST_UPDATE_SUCCESS("게시글 수정 성공", HttpStatus.OK.value()),
    POST_DELETE_SUCCESS("게시글 삭제 성공", HttpStatus.OK.value()),
    CHECK_COMMENT_SUCCESS("댓글 조회 성공", HttpStatus.OK.value()),
    COMMENT_CREATE_SUCCESS("댓글 생성 성공", HttpStatus.OK.value()),
    COMMENT_UPDATE_SUCCESS("댓글 수정 성공", HttpStatus.OK.value()),
    COMMENT_DELETE_SUCCESS("댓글 삭제 성공", HttpStatus.OK.value()),
    POST_LIKE_SUCCESS("게시글 좋아요 성공", HttpStatus.OK.value()),
    COMMENT_LIKE_SUCCESS("댓글 좋아요 성공", HttpStatus.OK.value()),

    // -----------------------------------------------------------

    TOKEN_VALIDATION_EXCEPTION("토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST.value()),
    SAME_ID_EXIST_EXCEPTION("중복된 사용자가 존재합니다.", HttpStatus.BAD_REQUEST.value()),
    INCORRECT_PASSWORD_EXCEPTION("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST.value()),
    NO_SUCH_USER_EXCEPTION("등록된 사용자가 없습니다.", HttpStatus.BAD_REQUEST.value()),
    NO_POST_EXCEPTION("게시글이 존재하지 않습니다", HttpStatus.BAD_REQUEST.value()),
    NO_SUCH_POST_EXCEPTION("해당 사용자의 게시글이 존재하지 않습니다.", HttpStatus.BAD_REQUEST.value()),
    NO_SUCH_COMMENT_EXCEPTION("해당 사용자의 댓글이 존재하지 않습니다.", HttpStatus.BAD_REQUEST.value()),
    NO_AUTHORIZATION_EXCEPTION("권한이 없습니다.", HttpStatus.BAD_REQUEST.value());

    final String message;
    final int httpStatus;

    StatusCode(String message, int httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
