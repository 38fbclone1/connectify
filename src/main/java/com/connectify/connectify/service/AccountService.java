package com.connectify.connectify.service;

import com.connectify.connectify.DTO.request.EditAccountRequest;
import com.connectify.connectify.DTO.response.PrivateAccountResponse;
import com.connectify.connectify.DTO.response.CommonResponse;
import com.connectify.connectify.entity.Role;
import com.connectify.connectify.enums.EError;
import com.connectify.connectify.enums.ERole;
import com.connectify.connectify.exception.CustomException;
import com.connectify.connectify.entity.Account;
import com.connectify.connectify.repository.AccountRepository;
import com.connectify.connectify.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ModelMapper modelMapper;

    private BCryptPasswordEncoder passwordEncoder;

    AccountService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Account getCurrentAccount() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        if (authenticationToken != null && authenticationToken.getPrincipal() instanceof Account) {
            return (Account) authenticationToken.getPrincipal();
        }
        return null;
    }

    public ResponseEntity<CommonResponse<String>> createAccount (EditAccountRequest editAccountRequest) {
        if (accountRepository.existsByEmail(editAccountRequest.getEmail()))
            throw new CustomException(EError.EXISTED_BY_EMAIL);
        if (accountRepository.existsByPhoneNumber(editAccountRequest.getPhoneNumber()))
            throw  new CustomException(EError.EXISTED_BY_PHONE_NUMBER);
        if (accountRepository.existsByIdentificationNumber(editAccountRequest.getIdentificationNumber()))
            throw  new CustomException(EError.EXISTED_BY_IDENTIFICATION_NUMBER);
        String encodedPassword = passwordEncoder.encode(editAccountRequest.getPassword());

        Account newAccount = modelMapper.map(editAccountRequest, Account.class);
        newAccount.setCreatedAt(new Date());
        newAccount.setPassword(encodedPassword);

        // set default role: user
        Role accountRole = roleRepository.findByName(ERole.USER);

        accountRepository.save(newAccount);

        CommonResponse<String> response = new CommonResponse<>(200, "", "Create account successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<CommonResponse<?>> getAccount (String id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            PrivateAccountResponse privateAccountResponse = modelMapper.map(account, PrivateAccountResponse.class);
            CommonResponse<PrivateAccountResponse> response = new CommonResponse<>(200, privateAccountResponse, "Get account successfully!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        CommonResponse<?> response = new CommonResponse<>(500, null, "Bad request!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}
