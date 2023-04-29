package com.example.postscomments.controller;

import com.example.postscomments.dto.PostDto;
import com.example.postscomments.dto.ResponseEntityDto;
import com.example.postscomments.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<ResponseEntityDto> getPosts() {
        return postService.getPosts();
    }

    @PostMapping
    public ResponseEntity<ResponseEntityDto> createPost(@RequestBody PostDto.Request.Create requestDto, HttpServletRequest request) {
        return postService.createPost(requestDto, request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseEntityDto> getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseEntityDto> updatePost(@PathVariable Long id, @RequestBody PostDto.Request.Update requestDto, HttpServletRequest request) {
        return postService.updatePost(id, requestDto, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseEntityDto> deletePost(@PathVariable Long id, HttpServletRequest request) {
        return postService.deletePost(id, request);
    }

    @PutMapping("/press-like/{id}")
    public ResponseEntity<ResponseEntityDto> pressLike(@PathVariable Long id, HttpServletRequest request) {
        return postService.pressLike(id, request);
    }
}
