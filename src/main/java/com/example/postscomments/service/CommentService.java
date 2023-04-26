package com.example.postscomments.service;

import com.example.postscomments.dto.CommentDto;
import com.example.postscomments.dto.ResponseEntityDto;
import com.example.postscomments.entity.Comment;
import com.example.postscomments.entity.Post;
import com.example.postscomments.entity.User;
import com.example.postscomments.repository.CommentRepository;
import com.example.postscomments.util.StatusCode;
import com.example.postscomments.util.Validate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final Validate validate;
    private final CommentRepository commentRepository;

    @Transactional
    public ResponseEntity<ResponseEntityDto> createComment(Long postId, CommentDto.Request.Create requestDto, HttpServletRequest request) {
        User user = validate.userFromToken(request);
        Post post = validate.postExist(postId);
        Comment comment = Comment.from(requestDto);
        comment.setPostAndUser(post, user);
        post.addComment(comment);

        Comment savedComment = commentRepository.saveAndFlush(comment);

        return new ResponseEntity<>(ResponseEntityDto.of(StatusCode.COMMENT_CREATE_SUCCESS, StatusCode.COMMENT_CREATE_SUCCESS.getMessage(), CommentDto.Response.from(savedComment)), StatusCode.COMMENT_CREATE_SUCCESS.getHttpStatus());
    }

    @Transactional
    public ResponseEntity<ResponseEntityDto> updateComment(Long id, CommentDto.Request.Update requestDto, HttpServletRequest request) {
        User user = validate.userWithAdmin(request);
        Comment comment = validate.commentWithUser(id, user);

        comment.update(requestDto);

        return new ResponseEntity<>(ResponseEntityDto.of(StatusCode.COMMENT_UPDATE_SUCCESS, StatusCode.COMMENT_UPDATE_SUCCESS.getMessage(), CommentDto.Response.from(comment)), StatusCode.COMMENT_UPDATE_SUCCESS.getHttpStatus());
    }

    @Transactional
    public ResponseEntity<ResponseEntityDto> deleteComment(Long id, HttpServletRequest request) {
        User user = validate.userWithAdmin(request);
        Comment comment = validate.commentWithUser(id, user);

        commentRepository.delete(comment);

        return new ResponseEntity<>(ResponseEntityDto.of(StatusCode.COMMENT_DELETE_SUCCESS, StatusCode.COMMENT_DELETE_SUCCESS.getMessage(), CommentDto.Response.from(comment)), StatusCode.COMMENT_DELETE_SUCCESS.getHttpStatus());
    }

}