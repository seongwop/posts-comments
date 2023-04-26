package com.example.postscomments.util;

import com.example.postscomments.entity.Post;
import com.example.postscomments.entity.User;
import com.example.postscomments.exception.CustomException;
import com.example.postscomments.jwt.JwtUtil;
import com.example.postscomments.repository.CommentRepository;
import com.example.postscomments.repository.PostRepository;
import com.example.postscomments.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
public class Validate {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // 사용자 인증
    public User user(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);

        if (token == null) return null;
        else {
            Claims claims;
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }
            return userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new CustomException(StatusCode.NO_SUCH_USER_EXCEPTION.getMessage())
            );
        }
    }

    // 사용자의 게시글 확인 및 권한 인증
    public Post postWithAdmin(Long id, HttpServletRequest request) {
        User user = user(request);

        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            return postRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new CustomException(StatusCode.NO_SUCH_POST_EXCEPTION.getMessage())
            );
        } else {
            throw new CustomException(StatusCode.NO_AUTHORIZATION_EXCEPTION.getMessage());
        }
    }
}
