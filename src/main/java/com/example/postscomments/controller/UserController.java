package com.example.postscomments.controller;

import com.example.postscomments.dto.ResponseEntityDto;
import com.example.postscomments.dto.UserDto;
import com.example.postscomments.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody @Valid UserDto.Request.signup requestDto, @RequestParam("admin") boolean admin) {
        return userService.signUp(requestDto);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserDto.Request.login requestDto, HttpServletResponse response) {
        return userService.login(requestDto, response);
    }
}