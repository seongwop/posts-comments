package com.example.postscomments.dto;

import com.example.postscomments.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class CommentDto {

    public static class Request {

        @Getter
        @NoArgsConstructor
        public static class Create {
            private String content;
        }

        @Getter
        @NoArgsConstructor
        public static class Update {
            private String content;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Response {

        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private String comment;

        private Response(Comment comment) {
            this.createdAt = comment.getCreatedAt();
            this.modifiedAt = comment.getModifiedAt();
            this.comment = comment.getContent();
        }

        public static Response from(Comment comment) {
            return new Response(comment);
        }
    }
}