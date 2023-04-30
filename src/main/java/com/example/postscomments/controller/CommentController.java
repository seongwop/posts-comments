package com.example.postscomments.controller;

import com.example.postscomments.dto.CommentDto;
import com.example.postscomments.dto.ResponseEntityDto;
import com.example.postscomments.security.UserDetailsImpl;
import com.example.postscomments.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/post/{postId}/create")
    public ResponseEntity<ResponseEntityDto> createComment(@PathVariable Long postId,
                                                           @RequestBody CommentDto.Request.Create requestDto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(postId, requestDto, userDetails.getUser());
    }

    @PutMapping ("/update/{id}")
    public ResponseEntity<ResponseEntityDto> updateComment(@PathVariable Long id,
                                                           @RequestBody CommentDto.Request.Update requestDto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(id, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseEntityDto> deleteComment(@PathVariable Long id,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(id, userDetails.getUser());
    }

    @PutMapping("/press-like/{id}")
    public ResponseEntity<ResponseEntityDto> pressLike(@PathVariable Long id,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.pressLike(id, userDetails.getUser());
    }
}
