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
import com.example.postscomments.security.UserDetailsImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@RequiredArgsConstructor
@Component
public class Validate {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // 토큰이 필요한 API 요청에서 토큰을 전달하지 않았거나 정상 토큰이 아닐 경우
    public User userFromToken(UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new CustomException(StatusCode.TOKEN_VALIDATION_EXCEPTION.getMessage());
        }
        return userDetails.getUser();
    }

    // DB에 이미 존재하는 username으로 회원가입을 요청한 경우
    public void userExist(UserDto.Request.signup requestDto) {
        userRepository.findByUsername(requestDto.getUsername()).ifPresent(
                (i) -> { throw new CustomException(StatusCode.SAME_ID_EXIST_EXCEPTION.getMessage());
                }
        );
    }

    // 로그인 시, 전달된 username이 맞지 않는 경우
    public User username(UserDto.Request.login requestDto) {
        return userRepository.findByUsername(requestDto.getUsername()).orElseThrow(
                () -> new CustomException(StatusCode.NO_SUCH_USER_EXCEPTION.getMessage())
        );
    }

    // 로그인 시, 전달된 password가 맞지 않는 정보
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

    // 토큰이 있고, 유효한 토큰이지만 해당 사용자가 작성한 게시글이 아닌 경우
    public Post postWithUser(Long id, User user) {
        return postRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                () -> new CustomException(StatusCode.NO_SUCH_POST_EXCEPTION.getMessage())
        );
    }

    // 토큰이 있고, 유효한 토큰이지만 해당 사용자가 작성한 댓글이 아닌 경우
    public Comment commentWithUser(Long id, User user) {
        return commentRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                () -> new CustomException(StatusCode.NO_SUCH_COMMENT_EXCEPTION.getMessage())
        );
    }


}
