package com.connectify.connectify.repository;

import com.connectify.connectify.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, String> {
    List<File> findByPostId(String postId);
}
