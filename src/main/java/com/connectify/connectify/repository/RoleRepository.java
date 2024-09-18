package com.connectify.connectify.repository;

import com.connectify.connectify.entity.Role;
import com.connectify.connectify.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<com.connectify.connectify.entity.Role, String> {

    Role findByName(ERole role);
}
