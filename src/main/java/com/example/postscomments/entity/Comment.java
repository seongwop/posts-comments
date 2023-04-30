package com.example.postscomments.entity;

import com.example.postscomments.dto.CommentDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends TimeStamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    @Column(nullable = false)
    private String content;

    @JoinColumn(name = "POST_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @JoinColumn(name = "USER_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    private int likes;

    @Column(nullable = false)
    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<Reply> replyList;

    private Comment(CommentDto.Request.Create requestDto) {
        this.content = requestDto.getContent();
    }

    public void setPostAndUser(Post post, User user) {
        this.post = post;
        this.user = user;
    }

    public static Comment from(CommentDto.Request.Create requestDto) {
        return new Comment(requestDto);
    }

    public void update(CommentDto.Request.Update requestDto) {
        this.content = requestDto.getContent();
    }

    public void updateLikes(boolean update) {
        this.likes = update ? likes + 1 : likes - 1;
    }
}
