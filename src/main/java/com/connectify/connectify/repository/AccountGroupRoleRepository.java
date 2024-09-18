package com.connectify.connectify.repository;

import com.connectify.connectify.entity.AccountGroupRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountGroupRoleRepository extends JpaRepository<AccountGroupRole, String> {
}
