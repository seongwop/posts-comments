package com.example.postscomments.controller;

import com.example.postscomments.dto.PostDto;
import com.example.postscomments.dto.ResponseEntityDto;
import com.example.postscomments.security.UserDetailsImpl;
import com.example.postscomments.service.PostService;
import com.example.postscomments.util.Validate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("posts")
public class PostController {

    private final PostService postService;
    private final Validate validate;

    @GetMapping
    public ResponseEntity<ResponseEntityDto> getPosts() {
        return postService.getPosts();
    }

    @PostMapping
    public ResponseEntity<ResponseEntityDto> createPost(@RequestBody PostDto.Request.Create requestDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(requestDto, validate.userFromToken(userDetails));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseEntityDto> getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseEntityDto> updatePost(@PathVariable Long id,
                                                        @RequestBody PostDto.Request.Update requestDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(id, requestDto, validate.userFromToken(userDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseEntityDto> deletePost(@PathVariable Long id,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(id, validate.userFromToken(userDetails));
    }

    @PutMapping("/press-like/{id}")
    public ResponseEntity<ResponseEntityDto> pressLike(@PathVariable Long id,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.pressLike(id, validate.userFromToken(userDetails));
    }
}
