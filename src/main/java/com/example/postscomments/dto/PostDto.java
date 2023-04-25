package com.example.postscomments.dto;

import com.example.postscomments.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PostDto {

    public static class Request {

        @Getter
        @NoArgsConstructor
        public static class Create {
            private String username;
            private String title;
            private String content;
        }

        @Getter
        @NoArgsConstructor
        public static class Update {
            private String title;
            private String content;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Response {
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private String username;
        private String title;
        private String content;
        private List<CommentDto.Response> comments;


        private Response(Post post) {
            this.createdAt = post.getCreatedAt();
            this.modifiedAt = post.getModifiedAt();
            this.username = post.getUsername();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.comments = post.getCommentList().stream()
                    .map(CommentDto.Response::from)
                    .sorted(Comparator.comparing(CommentDto.Response::getCreatedAt).reversed())
                    .collect(Collectors.toList());
        }

        public static Response from(Post post) {
            return new Response(post);
        }
    }
}
