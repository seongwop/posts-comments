package com.example.postscomments.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POSTLIKE_ID")
    private Long id;

    @Column(nullable = false)
    private boolean pressed;

    @JoinColumn(name = "POST_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @JoinColumn(name = "USER_ID", nullable = false)
    @OneToOne
    private User user;

    private PostLike(boolean pressed, Post post, User user) {
        this.pressed = pressed;
        this.post = post;
        this.user = user;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public static PostLike of(boolean pressed, Post post, User user) {
        return new PostLike(pressed, post, user);
    }
}
