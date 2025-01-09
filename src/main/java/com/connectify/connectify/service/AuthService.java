package com.connectify.connectify.service;

import com.connectify.connectify.DTO.request.AuthenticationRequest;
import com.connectify.connectify.DTO.request.EditAccountRequest;
import com.connectify.connectify.DTO.request.GetOtpRequest;
import com.connectify.connectify.DTO.request.VerifyOtpRequest;
import com.connectify.connectify.DTO.response.*;
import com.connectify.connectify.JWT.JWTUtil;
import com.connectify.connectify.entity.PhoneNumberStatus;
import com.connectify.connectify.enums.EError;
import com.connectify.connectify.enums.ERole;
import com.connectify.connectify.exception.CustomException;
import com.connectify.connectify.entity.Account;
import com.connectify.connectify.repository.AccountRepository;
import com.nimbusds.jose.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ModelMapper mapper;

    @Autowired
    JWTUtil jwtUtil;

    @Autowired
    PhoneNumberStatusService phoneNumberStatusService;

    public ResponseEntity<CommonResponse<?>> authenticate (AuthenticationRequest request) {
        System.out.println(accountRepository.existsByPhoneNumber(request.getPhoneNumber()));
        Account account = accountRepository.findByPhoneNumber(request.getPhoneNumber()).orElse(null);
        if (account == null) throw new CustomException(EError.USER_NOT_EXISTED);

        boolean isAuthenticated = passwordEncoder.matches(request.getPassword(), account.getPassword());
        if (!isAuthenticated) throw new CustomException(EError.INCORRECT_PASSWORD);
        LoginResponse loginResponse = new LoginResponse();
        if (account.getDevices() != null && !account.getDevices().isEmpty()) {
            if (checkDeviceExisted(account.getDevices(), request.getDeviceId())) {
                try {
                    String token = jwtUtil.generateToken(account);
                    loginResponse.setOtpAuthenticated(true);
                    loginResponse.setAuthenticated(true);
                    loginResponse.setToken(token);
                    CommonResponse<LoginResponse> response = new CommonResponse<>(200, loginResponse, "Login successfully!");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (JOSEException e) {
                    throw new CustomException(EError.CANNOT_CREATE_TOKEN);
                }
            }
        }
        loginResponse.setAuthenticated(true);
        CommonResponse<LoginResponse> response = new CommonResponse<>(200, loginResponse, "Login successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public boolean checkDeviceExisted (String devices, String deviceId) {
        if (deviceId == null || deviceId.isEmpty()) return false;
        if (devices == null || devices.isEmpty()) throw new CustomException(EError.BAD_REQUEST);
        List<String> deviceList = List.of(devices.split(";"));
        return deviceList.contains(deviceId);
    }

    public ResponseEntity<?> getOtp (GetOtpRequest request) {
        GetOtpResponse getOtpResponse = phoneNumberStatusService.getOtp(request);
        CommonResponse<?> response = new CommonResponse<>(200, getOtpResponse,"Get otp successfully!");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> verifyOtp (VerifyOtpRequest request) {
        try {
            VerifyOtpResponse verifyOtpResponse = phoneNumberStatusService.verifyOtp(request);
            if (verifyOtpResponse.isCorrect()) {
                Account account = accountRepository.findByPhoneNumber(request.getPhoneNumber()).orElse(null);
                if (account == null) throw new CustomException(EError.USER_NOT_EXISTED);
                String token = jwtUtil.generateToken(account);
                verifyOtpResponse.setToken(token);
            }
            CommonResponse<?> response = new CommonResponse<>(200, verifyOtpResponse,"Get otp successfully!");
            return ResponseEntity.ok(response);
        } catch (JOSEException e) {
            throw new CustomException(EError.CANNOT_CREATE_TOKEN);
        }
    }

    public Account getCurrentAccount() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        if (authenticationToken != null && authenticationToken.getPrincipal() instanceof Account) {
            return (Account) authenticationToken.getPrincipal();
        }
        return null;
    }

    public boolean checkIsCurrentAccount (String accountId) {
        Account currentAccount = getCurrentAccount();
        return currentAccount.getId().equals(accountId);
    }


    public ResponseEntity<?> checkValidRegisterInfo(EditAccountRequest request) {
        if (accountRepository.existsByPhoneNumber(request.getPhoneNumber())) throw new CustomException(EError.EXISTED_BY_PHONE_NUMBER);
        if (accountRepository.existsByEmail(request.getEmail())) throw new CustomException(EError.EXISTED_BY_EMAIL);
        if (accountRepository.existsByIdentificationNumber(request.getEmail())) throw new CustomException(EError.EXISTED_BY_IDENTIFICATION_NUMBER);
        CommonResponse<?> response = new CommonResponse<>(200, true, "Info is valid!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
