package com.example.postscomments.service;

import com.example.postscomments.dto.CommentDto;
import com.example.postscomments.dto.ResponseEntityDto;
import com.example.postscomments.entity.Comment;
import com.example.postscomments.entity.Post;
import com.example.postscomments.entity.User;
import com.example.postscomments.exception.CustomException;
import com.example.postscomments.jwt.JwtUtil;
import com.example.postscomments.repository.CommentRepository;
import com.example.postscomments.repository.PostRepository;
import com.example.postscomments.repository.UserRepository;
import com.example.postscomments.util.StatusCode;
import io.jsonwebtoken.Claims;
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
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public ResponseEntity<ResponseEntityDto> createComment(Long postId, CommentDto.Request.Create requestDto, HttpServletRequest request) {

        User user = validateUser(request);
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
        User user = validateUser(request);
        Comment comment = commentRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                () -> new CustomException(StatusCode.NO_SUCH_COMMENT_EXCEPTION.getMessage())
        );
        comment.update(requestDto);

        return new ResponseEntity<>(ResponseEntityDto.of(StatusCode.COMMENT_UPDATE_SUCCESS, StatusCode.COMMENT_UPDATE_SUCCESS.getMessage(), CommentDto.Response.from(comment)), StatusCode.COMMENT_UPDATE_SUCCESS.getHttpStatus());
    }

    @Transactional
    public ResponseEntity<ResponseEntityDto> deleteComment(Long id, HttpServletRequest request) {
        User user = validateUser(request);
        Comment comment = commentRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                () -> new CustomException(StatusCode.NO_SUCH_COMMENT_EXCEPTION.getMessage())
        );
        commentRepository.delete(comment);

        return new ResponseEntity<>(ResponseEntityDto.of(StatusCode.COMMENT_DELETE_SUCCESS, StatusCode.COMMENT_DELETE_SUCCESS.getMessage(), CommentDto.Response.from(comment)), StatusCode.COMMENT_DELETE_SUCCESS.getHttpStatus());
    }

    public User validateUser(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);

        if (token != null) {
            Claims claims;
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }
            return userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
        }
        return null;
    }
}