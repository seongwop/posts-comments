package com.example.postscomments.service;

import com.example.postscomments.dto.ResponseEntityDto;
import com.example.postscomments.dto.UserDto;
import com.example.postscomments.entity.User;
import com.example.postscomments.jwt.JwtUtil;
import com.example.postscomments.repository.UserRepository;
import com.example.postscomments.util.StatusCode;
import com.example.postscomments.util.UserRoleEnum;
import com.example.postscomments.util.Validate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Validate validate;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<ResponseEntityDto> signUp(UserDto.Request.signup requestDto, boolean admin) {
        validate.userExist(requestDto);
        UserRoleEnum role = UserRoleEnum.USER;
        if (admin) { role = UserRoleEnum.ADMIN; }
        User user = User.builder()
                .username(requestDto.getUsername())
                .password(requestDto.getPassword())
                .role(role)
                .build();

        User savedUser = userRepository.save(user);

        return new ResponseEntity<>(ResponseEntityDto.of(StatusCode.SIGNUP_SUCCESS, StatusCode.SIGNUP_SUCCESS.getMessage(), UserDto.Response.from(savedUser)), StatusCode.SIGNUP_SUCCESS.getHttpStatus());
    }

    @Transactional
    public ResponseEntity<ResponseEntityDto> login(UserDto.Request.login requestDto, HttpServletResponse response) {
        User user = validate.username(requestDto);
        validate.password(user, requestDto);

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));

        return new ResponseEntity<>(ResponseEntityDto.of(StatusCode.LOGIN_SUCCESS, StatusCode.LOGIN_SUCCESS.getMessage()), StatusCode.LOGIN_SUCCESS.getHttpStatus());
    }
}
