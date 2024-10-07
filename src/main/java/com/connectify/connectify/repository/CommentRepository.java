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

    @Query(value = "SELECT * FROM comments WHERE " +
            "(:createdBy IS NULL OR created_by = :createdBy) " +
            "AND (:postId is NULL or post_id = :postId) " +
            "AND (:keyword IS NULL OR content LIKE CONCAT('%', :keyword, '%')) " +
            "ORDER BY " +
            "CASE WHEN :sortDir = 'ASC' THEN " +
            "CASE WHEN :sortBy IS NOT NULL THEN :sortBy END END ASC, " +
            "CASE WHEN :sortDir = 'DESC' THEN " +
            "CASE WHEN :sortBy IS NOT NULL THEN :sortBy END END DESC " +
            "LIMIT :pageSize OFFSET :offset",
            nativeQuery = true)
    List<Comment> searchComment(
            @Param("offset") int offset,
            @Param("pageSize") int pageSize,
            @Param("sortBy") String sortBy,
            @Param("sortDir") String sortDir,
            @Param("keyword") String keyword,
            @Param("postId") String postId,
            @Param("createdBy") String createdBy
    );
}
