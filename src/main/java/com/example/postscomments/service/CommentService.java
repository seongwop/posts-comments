package com.example.postscomments.service;

import com.example.postscomments.dto.CommentDto;
import com.example.postscomments.dto.ResponseEntityDto;
import com.example.postscomments.entity.Comment;
import com.example.postscomments.entity.Post;
import com.example.postscomments.entity.User;
import com.example.postscomments.exception.CustomException;
import com.example.postscomments.repository.CommentRepository;
import com.example.postscomments.repository.PostRepository;
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

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final Validate validate;

    @Transactional
    public ResponseEntity<ResponseEntityDto> createComment(Long postId, CommentDto.Request.Create requestDto, HttpServletRequest request) {

        User user = validate.user(request);
        Post post = postRepository.findByIdAndUserId(postId, user.getId()).orElseThrow(
                () -> new CustomException(StatusCode.NO_SUCH_POST_EXCEPTION.getMessage())
        );
        Comment comment = Comment.from(requestDto);
        comment.setUserAndPost(user, post);
        post.addComment(comment);
        commentRepository.saveAndFlush(comment);

        return new ResponseEntity<>(ResponseEntityDto.of(StatusCode.COMMENT_CREATE_SUCCESS, StatusCode.COMMENT_CREATE_SUCCESS.getMessage(), CommentDto.Response.from(comment)), StatusCode.COMMENT_CREATE_SUCCESS.getHttpStatus());
    }

    @Transactional
    public ResponseEntity<ResponseEntityDto> updateComment(Long id, CommentDto.Request.Update requestDto, HttpServletRequest request) {
        User user = validate.user(request);
        Comment comment = commentRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                () -> new CustomException(StatusCode.NO_SUCH_COMMENT_EXCEPTION.getMessage())
        );
        comment.update(requestDto);

        return new ResponseEntity<>(ResponseEntityDto.of(StatusCode.COMMENT_UPDATE_SUCCESS, StatusCode.COMMENT_UPDATE_SUCCESS.getMessage(), CommentDto.Response.from(comment)), StatusCode.COMMENT_UPDATE_SUCCESS.getHttpStatus());
    }

    @Transactional
    public ResponseEntity<ResponseEntityDto> deleteComment(Long id, HttpServletRequest request) {
        User user = validate.user(request);
        Comment comment = commentRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                () -> new CustomException(StatusCode.NO_SUCH_COMMENT_EXCEPTION.getMessage())
        );
        commentRepository.delete(comment);

        return new ResponseEntity<>(ResponseEntityDto.of(StatusCode.COMMENT_DELETE_SUCCESS, StatusCode.COMMENT_DELETE_SUCCESS.getMessage(), CommentDto.Response.from(comment)), StatusCode.COMMENT_DELETE_SUCCESS.getHttpStatus());
    }

}