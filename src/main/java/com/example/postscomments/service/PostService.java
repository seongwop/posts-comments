package com.example.postscomments.service;

import com.example.postscomments.dto.PostDto;
import com.example.postscomments.dto.ResponseEntityDto;
import com.example.postscomments.entity.Post;
import com.example.postscomments.entity.User;
import com.example.postscomments.exception.CustomException;
import com.example.postscomments.jwt.JwtUtil;
import com.example.postscomments.repository.PostRepository;
import com.example.postscomments.repository.UserRepository;
import com.example.postscomments.util.StatusCode;
import com.example.postscomments.util.UserRoleEnum;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public ResponseEntity<ResponseEntityDto> getPosts() {
        List<PostDto.Response> postDtoList = postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(PostDto.Response::from)
                .toList();

        return new ResponseEntity<>(ResponseEntityDto.of(StatusCode.CHECK_POST_SUCCESS, StatusCode.CHECK_POST_SUCCESS.getMessage(), postDtoList), StatusCode.CHECK_POST_SUCCESS.getHttpStatus());
    }

    @Transactional
    public ResponseEntity<ResponseEntityDto> createPost(PostDto.Request.Create requestDto, HttpServletRequest request) {
        User user = validateUser(request);
        Post post = Post.from(requestDto);
        post.setUser(user);
        postRepository.saveAndFlush(post);

        return new ResponseEntity<>(ResponseEntityDto.of(StatusCode.POST_CREATE_SUCCESS, StatusCode.POST_CREATE_SUCCESS.getMessage(), PostDto.Response.from(post)), StatusCode.POST_CREATE_SUCCESS.getHttpStatus());
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ResponseEntityDto> getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new CustomException(StatusCode.NO_POST_EXCEPTION.getMessage())
        );
        return new ResponseEntity<>(ResponseEntityDto.of(StatusCode.CHECK_POST_SUCCESS, StatusCode.CHECK_POST_SUCCESS.getMessage(), PostDto.Response.from(post)), StatusCode.CHECK_POST_SUCCESS.getHttpStatus());
    }

    @Transactional
    public ResponseEntity<ResponseEntityDto> updatePost(Long id, PostDto.Request.Update requestDto, HttpServletRequest request) {

        Post post = validatePostWithAdmin(id, request);
        post.update(requestDto);

        return new ResponseEntity<>(ResponseEntityDto.of(StatusCode.POST_UPDATE_SUCCESS, StatusCode.POST_UPDATE_SUCCESS.getMessage(), PostDto.Response.from(post)), StatusCode.POST_UPDATE_SUCCESS.getHttpStatus());
    }

    @Transactional
    public ResponseEntity<ResponseEntityDto> deletePost(Long id, HttpServletRequest request) {

        Post post = validatePostWithAdmin(id, request);
        postRepository.delete(post);

        return new ResponseEntity<>(ResponseEntityDto.of(StatusCode.POST_DELETE_SUCCESS, StatusCode.POST_DELETE_SUCCESS.getMessage(), PostDto.Response.from(post)), StatusCode.POST_DELETE_SUCCESS.getHttpStatus());
    }

    // 사용자 인증
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
                    () -> new CustomException(StatusCode.NO_SUCH_USER_EXCEPTION.getMessage())
            );
        }
        return null;
    }

    // 사용자의 게시글 확인 및 권한 인증
    public Post validatePostWithAdmin(Long id, HttpServletRequest request) {
        User user = validateUser(request);

        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            return postRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new CustomException(StatusCode.NO_SUCH_POST_EXCEPTION.getMessage())
            );
        } else {
            throw new CustomException(StatusCode.NO_AUTHORIZATION_EXCEPTION.getMessage());
        }
    }
}
