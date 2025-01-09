package com.connectify.connectify.controller;

import com.connectify.connectify.DTO.request.AuthenticationRequest;
import com.connectify.connectify.DTO.request.EditAccountRequest;
import com.connectify.connectify.DTO.request.GetOtpRequest;
import com.connectify.connectify.DTO.request.VerifyOtpRequest;
import com.connectify.connectify.DTO.response.CommonResponse;
import com.connectify.connectify.service.AccountService;
import com.connectify.connectify.service.AuthService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    AccountService accountService;

    @PostMapping("/log-in")
    public ResponseEntity<?> authenticate (@RequestBody AuthenticationRequest request) {
        return authService.authenticate(request);
    }

    @PostMapping("/register")
    public ResponseEntity<CommonResponse<String>> register (@RequestBody EditAccountRequest request) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        return accountService.createAccount(request);
    }

    @PostMapping("/check-valid-register-info")
    public ResponseEntity<?> checkValidRegisterInfo (@RequestBody EditAccountRequest request) {
        return authService.checkValidRegisterInfo(request);
    }

    @PostMapping("/get-otp")
    public ResponseEntity<?> getOTP (@RequestBody GetOtpRequest request) {
        return authService.getOtp(request);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp (@RequestBody VerifyOtpRequest request) {
        return authService.verifyOtp(request);
    }
}
