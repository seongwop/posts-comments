package com.example.postscomments.service;

import com.example.postscomments.dto.ResponseEntityDto;
import com.example.postscomments.dto.TokenDto;
import com.example.postscomments.dto.UserDto;
import com.example.postscomments.entity.RefreshToken;
import com.example.postscomments.entity.User;
import com.example.postscomments.jwt.JwtUtil;
import com.example.postscomments.repository.RefreshTokenRepository;
import com.example.postscomments.repository.UserRepository;
import com.example.postscomments.util.StatusCode;
import com.example.postscomments.util.UserRoleEnum;
import com.example.postscomments.util.Validate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Validate validate;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public ResponseEntity<ResponseEntityDto> signUp(UserDto.Request.signup requestDto, boolean admin) {
        validate.userExist(requestDto);
        UserRoleEnum role = UserRoleEnum.USER;
        if (admin) {
            role = UserRoleEnum.ADMIN;
        }

        User user = User.of(requestDto.getUsername(), requestDto.getPassword(), role);
        User savedUser = userRepository.save(user);

        return new ResponseEntity<>(ResponseEntityDto.of(StatusCode.SIGNUP_SUCCESS, StatusCode.SIGNUP_SUCCESS.getMessage(), UserDto.Response.from(savedUser)), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ResponseEntityDto> login(UserDto.Request.login requestDto, HttpServletResponse response) {
        User user = validate.username(requestDto);
        validate.password(user, requestDto);
        TokenDto tokenDto = jwtUtil.createTokenDto(user.getUsername(), user.getRole());

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUsername(user.getUsername());

        if (refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        } else {
            RefreshToken newToken = new RefreshToken(user.getUsername(), tokenDto.getRefreshToken());
            refreshTokenRepository.save(newToken);
        }

        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());

        return new ResponseEntity<>(ResponseEntityDto.of(StatusCode.LOGIN_SUCCESS, StatusCode.LOGIN_SUCCESS.getMessage()), HttpStatus.OK);
    }
}