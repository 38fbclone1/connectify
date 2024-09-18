package com.connectify.connectify.repository;

import com.connectify.connectify.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByIdentificationNumber(String identificationNumber);

    Optional<Account> findByPhoneNumber(String phoneNumber);
}
