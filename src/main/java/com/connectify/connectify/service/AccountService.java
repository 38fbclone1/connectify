package com.connectify.connectify.service;

import com.connectify.connectify.DTO.request.*;
import com.connectify.connectify.DTO.response.*;
import com.connectify.connectify.enums.EError;
import com.connectify.connectify.exception.CustomException;
import com.connectify.connectify.entity.Account;
import com.connectify.connectify.repository.AccountRepository;
import com.connectify.connectify.repository.RoleRepository;
import com.connectify.connectify.util.RSAUtils;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

@Service
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthService authService;

    @Autowired
    RSAUtils rsaUtils;

    @Autowired
    RelationshipService relationshipService;

    private BCryptPasswordEncoder passwordEncoder;

    AccountService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public ResponseEntity<?> saveDevice (EditDeviceRequest request) {
        Account account = authService.getCurrentAccount();
        if (account == null) throw new CustomException(EError.BAD_REQUEST);
        if (account.getDevices() == null) {
            account.setDevices(request.getDeviceId() + ";");
        }
        if (!authService.checkDeviceExisted(account.getDevices(), request.getDeviceId())) {
            account.setDevices(account.getDevices() + request.getDeviceId() + ";");
        }
        accountRepository.save(account);
        CommonResponse<?> response = new CommonResponse<>(200, account.getDevices(), "Save device successfully!");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> findAllDevices () {
        Account account = authService.getCurrentAccount();
        if (account == null) throw new CustomException(EError.BAD_REQUEST);
        CommonResponse<?> response = new CommonResponse<>(200, account.getDevices(), "Find devices successfully!");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> removeDevice (EditDeviceRequest request) {
        Account account = authService.getCurrentAccount();
        if (account == null) throw new CustomException(EError.BAD_REQUEST);
        if (account.getDevices() == null || account.getDevices().isEmpty()) throw new CustomException(EError.DEVICE_NOT_EXISTED);
        List<String> devices = new ArrayList<>(List.of(account.getDevices().split(";")));
        devices.remove(request.getDeviceId());
        account.setDevices(String.join(";", devices) + ";");

        CommonResponse<?> response = new CommonResponse<>(200, account.getDevices(), "Delete device successfully!");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<CommonResponse<String>> createAccount (EditAccountRequest editAccountRequest) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        if (accountRepository.existsByEmail(editAccountRequest.getEmail()))
            throw new CustomException(EError.EXISTED_BY_EMAIL);
        if (accountRepository.existsByPhoneNumber(editAccountRequest.getPhoneNumber()))
            throw  new CustomException(EError.EXISTED_BY_PHONE_NUMBER);
        if (accountRepository.existsByIdentificationNumber(editAccountRequest.getIdentificationNumber()))
            throw  new CustomException(EError.EXISTED_BY_IDENTIFICATION_NUMBER);

        String encodedEmail = rsaUtils.encrypt(editAccountRequest.getEmail());
        String encodedIdentificationNumber = rsaUtils.encrypt(editAccountRequest.getIdentificationNumber());
        String encodedFullName = rsaUtils.encrypt(editAccountRequest.getFullName());
        String encodedAddress = rsaUtils.encrypt(editAccountRequest.getAddress());
        String encodedPassword = passwordEncoder.encode(editAccountRequest.getPassword());

        Account newAccount = modelMapper.map(editAccountRequest, Account.class);
        newAccount.setCreatedAt(new Date());
        newAccount.setPassword(encodedPassword);
        newAccount.setEmail(encodedEmail);
        newAccount.setIdentificationNumber(encodedIdentificationNumber);
        newAccount.setFullName(encodedFullName);
        newAccount.setAddress(encodedAddress);

        accountRepository.save(newAccount);

        CommonResponse<String> response = new CommonResponse<>(200, "", "Create account successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<CommonResponse<?>> getAccount (String id) throws Exception {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) throw new CustomException(EError.BAD_REQUEST);
        Account account = optionalAccount.get();
        System.out.println(rsaUtils.decrypt(account.getEmail()));
        Account currentAccount = authService.getCurrentAccount();
        CommonResponse<?> response;
        if (account.getId().equals(currentAccount.getId())) {
            PrivateAccountResponse accountResponse = modelMapper.map(account, PrivateAccountResponse.class);
            response = new CommonResponse<>(200, accountResponse, "Get account successfully!");
        } else {
            PublicAccountDetailResponse accountResponse = modelMapper.map(account, PublicAccountDetailResponse.class);
            RelationshipResponse relationshipResponse = relationshipService.getRelationshipByAccounts(id);
            if (relationshipResponse != null) {
                accountResponse.setRelationship(relationshipResponse);
            }
            response = new CommonResponse<>(200, accountResponse, "Get account successfully!");
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private PublicAccountResponse accountToPublicAccountResponse(Account account) {
        return modelMapper.map(account, PublicAccountResponse.class);
    }

    public ResponseEntity<?> search (CommonSearchRequest request) {
        List<Account> accounts = accountRepository.search(request.getPage() * request.getPageSize(), request.getPageSize(), request.getSortBy(), request.getSortDir(), request.getKeyword());
        List<PublicAccountResponse> accountResponses = accounts.stream().map(this::accountToPublicAccountResponse).toList();
        CommonResponse<?> response = new CommonResponse<>(200, accountResponses, "Search accounts successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    public ResponseEntity<?> updateRole (ERole roleName, String accountId) {
//        Optional<Account> optionalAccount = accountRepository.findById(accountId);
//        if (optionalAccount.isEmpty()) throw  new CustomException(EError.USER_NOT_EXISTED);
//        Account currentAccount = authService.getCurrentAccount();
//        if (!authService.hasRole(currentAccount.getId(), ERole.ADMIN)) throw new CustomException(EError.UNAUTHORIZED);
//        Account targetAccount = optionalAccount.get();
//        Set<Role> targetAccountRoles = targetAccount.getRoles().isEmpty() ? new HashSet<Role>() : targetAccount.getRoles();
//
//        Role role = roleRepository.findByName(roleName);
//        targetAccountRoles.add(role);
//        targetAccount.setRoles(targetAccountRoles);
//        accountRepository.save(targetAccount);
//
//        CommonResponse<?> response = new CommonResponse<>(200, null, "Update role successfully!");
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

    public ResponseEntity<?> delete (CommonDeleteRequest request) {
        accountRepository.deleteAllById(request.getIds());
        CommonResponse<?> response = new CommonResponse<>(200, null, "Delete accounts successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> searchByPhoneNumbers (SearchAccountByPhoneNumbersRequest request) {
        List<Account> accounts = accountRepository.searchByPhoneNumbers(request.getPhoneNumbers());
        List<PublicAccountResponse> accountResponses = accounts.stream().map(this::accountToPublicAccountResponse).toList();
        CommonResponse<?> response = new CommonResponse<>(200, accountResponses, "Search accounts successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public Account getAccountByPhoneNumber(String phoneNumber) {
        return accountRepository.findByPhoneNumber(phoneNumber).orElse(null);
    }
}
