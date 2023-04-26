package com.example.postscomments.util;

import com.example.postscomments.dto.UserDto;
import com.example.postscomments.entity.Comment;
import com.example.postscomments.entity.Post;
import com.example.postscomments.entity.User;
import com.example.postscomments.exception.CustomException;
import com.example.postscomments.jwt.JwtUtil;
import com.example.postscomments.repository.CommentRepository;
import com.example.postscomments.repository.PostRepository;
import com.example.postscomments.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Getter
@RequiredArgsConstructor
@Component
public class Validate {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // 토큰으로부터 사용자 인증
    public User userFromToken(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);

        if (token == null) return null;
        else {
            Claims claims;
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new CustomException(StatusCode.TOKEN_VALIDATION_EXCEPTION.getMessage());
            }
            return userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new CustomException(StatusCode.NO_SUCH_USER_EXCEPTION.getMessage())
            );
        }
    }

    // DB에 중복된 유저가 있는지 확인
    public void userExist(UserDto.Request.signup requestDto) {
        userRepository.findByUsername(requestDto.getUsername()).ifPresent(
                (i) -> { throw new CustomException(StatusCode.SAME_ID_EXIST_EXCEPTION.getMessage());
                }
        );
    }

    // DB에 유저가 존재하는지 확인
    public User username(UserDto.Request.login requestDto) {
        return userRepository.findByUsername(requestDto.getUsername()).orElseThrow(
                () -> new CustomException(StatusCode.NO_SUCH_USER_EXCEPTION.getMessage())
        );
    }

    // DB에 저장되어 있는 유저의 비밀번호와 입력된 비밀번호가 같은지 확인
    public void password(User user, UserDto.Request.login requestDto) {
        if (!user.getPassword().equals(requestDto.getPassword())) {
            throw new CustomException(StatusCode.INCORRECT_PASSWORD_EXCEPTION.getMessage());
        }
    }

    // DB에 게시글이 존재하는지 확인
    public Post postExist(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new CustomException(StatusCode.NO_POST_EXCEPTION.getMessage())
        );
    }

    // 유저에게 관리자 권한이 있는 지 확인
    public User userWithAdmin(HttpServletRequest request) {
        User user = userFromToken(request);
        if (user.getRole() != UserRoleEnum.ADMIN) {
            throw new CustomException(StatusCode.NO_AUTHORIZATION_EXCEPTION.getMessage());
        }
        return user;
    }

    // 사용자의 게시글 확인
    public Post postWithUser(Long id, User user) {
        return postRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                () -> new CustomException(StatusCode.NO_SUCH_POST_EXCEPTION.getMessage())
        );
    }

    // 사용자의 댓글 확인
    public Comment commentWithUser(Long id, User user) {
        return commentRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                () -> new CustomException(StatusCode.NO_SUCH_COMMENT_EXCEPTION.getMessage())
        );
    }


}
