package com.example.postscomments.repository;

import com.example.postscomments.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    CommentLike findByCommentIdAndUserId(Long commentId, Long userId);
}
