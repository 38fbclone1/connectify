package com.connectify.connectify.repository;

import com.connectify.connectify.entity.Comment;
import com.connectify.connectify.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findByPostId(String postId);

    @Query(value = "SELECT * FROM posts WHERE (:createdBy IS NULL OR created_by = :createdBy) " +
            "AND (:postId) IS NULL OR post_id = postId) " +
            "AND (:keyword IS NULL OR content LIKE %:keyword%) " +
            "ORDER BY :sortBy :sortDir " +
            "LIMIT :pageSize OFFSET :offset", nativeQuery = true)
    List<Comment> searchComment (
            @Param("offset") int offset,
            @Param("pageSize") int pageSize,
            @Param("sortBy") String sortBy,
            @Param("sortDir") String sortDir,
            @Param("keyword") String keyword,
            @Param("postId") String postId,
            @Param("createdBy") String createdBy
    );
}
