package com.example.postscomments.controller;

import com.example.postscomments.dto.CommentDto;
import com.example.postscomments.dto.ResponseEntityDto;
import com.example.postscomments.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/post{id}/create")
    public ResponseEntity<ResponseEntityDto> createComment(@PathVariable Long id, @RequestBody CommentDto.Request.Create requestDto, HttpServletRequest request) {
        return commentService.createComment(id, requestDto, request);
    }

    @PutMapping ("/update/{id}")
    public ResponseEntity<ResponseEntityDto> updateComment(@PathVariable Long id, @RequestBody CommentDto.Request.Update requestDto, HttpServletRequest request) {
        return commentService.updateComment(id, requestDto, request);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseEntityDto> deleteComment(@PathVariable Long id, HttpServletRequest request) {
        return commentService.deleteComment(id, request);
    }
}
