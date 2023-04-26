package com.example.postscomments.dto;

import com.example.postscomments.entity.User;
import com.example.postscomments.util.UserRoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;


public class UserDto {

    public static class Request {

        @Getter
        @NoArgsConstructor
        public static class signup {
            @Pattern(regexp = "^[a-z0-9]{4,10}$",message = "아이디는 최소 4자 이상 10자 이하이며 알파벳 소문자와 숫자로 구성되어야 합니다.")
            private String username;

            @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,15}$",message = "비밀번호는 최소 8자 이상 15자 이하이며 알파벳 대소문자와 숫자로 구성되어야 합니다.")
            private String password;

            private boolean admin;
        }

        @Getter
        @NoArgsConstructor
        public static class login {
            private String username;
            private String password;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Response {
        private String username;
        private UserRoleEnum role;

        private Response(User user) {
            this.username = user.getUsername();
            this.role = user.getRole();
        }

        public static Response from(User user) {
            return new Response(user);
        }
    }
}