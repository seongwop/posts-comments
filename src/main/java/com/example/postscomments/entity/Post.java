package com.example.postscomments.entity;

import com.example.postscomments.dto.PostDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @JoinColumn(name = "USER_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @Column(nullable = false)
    private int likes;

    private Post(PostDto.Request.Create requestDto) {
        this.username = requestDto.getUsername();
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addComment(Comment comment) {
        this.commentList.add(comment);
    }

    public static Post from(PostDto.Request.Create requestDto) {
        return new Post(requestDto);
    }

    public void update(PostDto.Request.Update requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }

    public void updateLikes(boolean update) {
        this.likes = update ? likes + 1 : likes - 1;
    }
}
