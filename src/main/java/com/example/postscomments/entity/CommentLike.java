package com.example.postscomments.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENTLIKE_ID")
    private Long id;

    @Column(nullable = false)
    private boolean pressed;

    @JoinColumn(name = "COMMENT_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment comment;

    @JoinColumn(name = "USER_ID", nullable = false)
    @OneToOne
    private User user;


    public CommentLike(boolean pressed, Comment comment, User user) {
        this.pressed = pressed;
        this.comment = comment;
        this.user = user;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public static CommentLike of(boolean pressed, Comment comment, User user) {
        return new CommentLike(pressed, comment, user);
    }
}
