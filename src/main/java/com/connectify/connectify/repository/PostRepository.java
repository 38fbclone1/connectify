package com.connectify.connectify.repository;

import com.connectify.connectify.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    @Query(value = "SELECT * FROM posts WHERE (:createdBy IS NULL OR created_by = :createdBy) " +
            "AND (:group IS NULL OR group_id = :group) " +
            "AND (:audience IS NULL OR audience = :audience) " +
            "AND (:keyword IS NULL OR content LIKE %:keyword%) " +
            "ORDER BY :sortBy :sortDir " +
            "LIMIT :pageSize OFFSET :offset", nativeQuery = true)
    List<Post> searchPost (
            @Param("offset") int offset,
            @Param("pageSize") int pageSize,
            @Param("sortBy") String sortBy,
            @Param("sortDir") String sortDir,
            @Param("keyword") String keyword,
            @Param("createdBy") String createdBy,
            @Param("audience") String audience,
            @Param("group") String group
    );
}
