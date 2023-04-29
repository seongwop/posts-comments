package com.example.postscomments.repository;

import com.example.postscomments.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    PostLike findByPostIdAndUserId(Long postId, Long userId);
}
