package com.example.postscomments.service;

import com.example.postscomments.dto.CommentDto;
import com.example.postscomments.dto.ResponseEntityDto;
import com.example.postscomments.entity.*;
import com.example.postscomments.repository.CommentLikeRepository;
import com.example.postscomments.repository.CommentRepository;
import com.example.postscomments.util.StatusCode;
import com.example.postscomments.util.Validate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final Validate validate;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public ResponseEntity<?> createComment(Long postId, CommentDto.Request.Create requestDto, User user) {
        Post post = validate.postExist(postId);
        Comment comment = Comment.from(requestDto);
        comment.setPostAndUser(post, user);
        post.addComment(comment);

        Comment savedComment = commentRepository.saveAndFlush(comment);

        return new ResponseEntity<>(ResponseEntityDto.of(StatusCode.COMMENT_CREATE_SUCCESS, StatusCode.COMMENT_CREATE_SUCCESS.getMessage(), CommentDto.Response.from(savedComment)), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateComment(Long id, CommentDto.Request.Update requestDto, User user) {
        Comment comment = validate.commentWithUser(id, user);
        comment.update(requestDto);

        return new ResponseEntity<>(ResponseEntityDto.of(StatusCode.COMMENT_UPDATE_SUCCESS, StatusCode.COMMENT_UPDATE_SUCCESS.getMessage(), CommentDto.Response.from(comment)), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteComment(Long id, User user) {
        Comment comment = validate.commentWithUser(id, user);
        commentRepository.delete(comment);

        return new ResponseEntity<>(ResponseEntityDto.of(StatusCode.COMMENT_DELETE_SUCCESS, StatusCode.COMMENT_DELETE_SUCCESS.getMessage(), CommentDto.Response.from(comment)), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> pressLike(Long id, User user) {
        Comment comment = validate.commentWithUser(id, user);
        CommentLike commentLike = commentLikeRepository.findByCommentIdAndUserId(comment.getId(), user.getId()).orElseGet(
                () -> commentLikeRepository.saveAndFlush(CommentLike.of(false, comment, user))
        );
        pressLike(comment, commentLike);

        return new ResponseEntity<>(ResponseEntityDto.of(StatusCode.COMMENT_LIKE_SUCCESS, StatusCode.COMMENT_LIKE_SUCCESS.getMessage()), HttpStatus.OK);
    }

    private static void pressLike(Comment comment, CommentLike commentLike) {
        if (commentLike.isPressed()) {
            commentLike.setPressed(false);
            comment.updateLikes(false);
        } else {
            commentLike.setPressed(true);
            comment.updateLikes(true);
        }
    }
}