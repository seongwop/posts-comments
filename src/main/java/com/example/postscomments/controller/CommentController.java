package com.example.postscomments.controller;

import com.example.postscomments.dto.CommentDto;
import com.example.postscomments.security.UserDetailsImpl;
import com.example.postscomments.service.CommentService;
import com.example.postscomments.util.Validate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("comments")
public class CommentController {

    private final CommentService commentService;
    private final Validate validate;

    @PostMapping("/post/{postId}/create")
    public ResponseEntity<?> createComment(@PathVariable Long postId,
                                                           @RequestBody CommentDto.Request.Create requestDto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(postId, requestDto, validate.userFromToken(userDetails));
    }

    @PutMapping ("/update/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id,
                                                           @RequestBody CommentDto.Request.Update requestDto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(id, requestDto, validate.userFromToken(userDetails));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(id, validate.userFromToken(userDetails));
    }

    @PutMapping("/press-like/{id}")
    public ResponseEntity<?> pressLike(@PathVariable Long id,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.pressLike(id, validate.userFromToken(userDetails));
    }
}
