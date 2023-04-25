package com.example.postscomments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PostsCommentsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostsCommentsApplication.class, args);
    }

}
