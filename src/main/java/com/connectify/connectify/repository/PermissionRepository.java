package com.connectify.connectify.repository;

import com.connectify.connectify.entity.Permission;
import com.connectify.connectify.enums.EPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {

    @Query("SELECT p FROM Permission p WHERE p.name IN :permissionNames")
    Set<Permission> findAllByName(@Param("permissionNames") Set<EPermission> permissionNames);
}
