package com.example.postscomments.service;

import com.example.postscomments.dto.PostDto;
import com.example.postscomments.dto.ResponseEntityDto;
import com.example.postscomments.entity.Post;
import com.example.postscomments.entity.PostLike;
import com.example.postscomments.entity.User;
import com.example.postscomments.repository.PostLikeRepository;
import com.example.postscomments.repository.PostRepository;
import com.example.postscomments.util.StatusCode;
import com.example.postscomments.util.Validate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final Validate validate;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<?> getPosts(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        List<PostDto.Response> postDtoList = postRepository.findAllByOrderByCreatedAtDesc(pageable)
                .stream()
                .map(PostDto.Response::from)
                .toList();

        return new ResponseEntity<>(ResponseEntityDto.of(StatusCode.CHECK_POST_SUCCESS, StatusCode.CHECK_POST_SUCCESS.getMessage(), postDtoList), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getPost(Long id) {
        Post post = validate.postExist(id);

        return new ResponseEntity<>(ResponseEntityDto.of(StatusCode.CHECK_POST_SUCCESS, StatusCode.CHECK_POST_SUCCESS.getMessage(), PostDto.Response.from(post)), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> createPost(PostDto.Request.Create requestDto, User user) {
        Post post = Post.from(requestDto);
        post.setUser(user);

        Post savedPost = postRepository.saveAndFlush(post);

        return new ResponseEntity<>(ResponseEntityDto.of(StatusCode.POST_CREATE_SUCCESS, StatusCode.POST_CREATE_SUCCESS.getMessage(), PostDto.Response.from(savedPost)), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updatePost(Long id, PostDto.Request.Update requestDto, User user) {
        Post post = validate.postWithUser(id, user);
        post.update(requestDto);

        return new ResponseEntity<>(ResponseEntityDto.of(StatusCode.POST_UPDATE_SUCCESS, StatusCode.POST_UPDATE_SUCCESS.getMessage(), PostDto.Response.from(post)), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deletePost(Long id, User user) {
        Post post = validate.postWithUser(id, user);
        postRepository.delete(post);

        return new ResponseEntity<>(ResponseEntityDto.of(StatusCode.POST_DELETE_SUCCESS, StatusCode.POST_DELETE_SUCCESS.getMessage(), PostDto.Response.from(post)), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> pressLike(Long id, User user) {
        Post post = validate.postExist(id);
        PostLike postLike = postLikeRepository.findByPostIdAndUserId(post.getId(), user.getId()).orElseGet(
                () -> postLikeRepository.saveAndFlush(PostLike.of(false, post, user))
        );
        pressLike(post, postLike);

        return new ResponseEntity<>(ResponseEntityDto.of(StatusCode.POST_LIKE_SUCCESS, StatusCode.POST_LIKE_SUCCESS.getMessage()), HttpStatus.OK);
    }

    private static void pressLike(Post post, PostLike postLike) {
        if (postLike.isPressed()) {
            postLike.setPressed(false);
            post.updateLikes(false);
        } else {
            postLike.setPressed(true);
            post.updateLikes(true);
        }
    }
}
