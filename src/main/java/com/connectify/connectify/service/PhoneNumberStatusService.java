package com.connectify.connectify.service;

import com.connectify.connectify.DTO.request.GetOtpRequest;
import com.connectify.connectify.DTO.request.VerifyOtpRequest;
import com.connectify.connectify.DTO.response.GetOtpResponse;
import com.connectify.connectify.DTO.response.VerifyOtpResponse;
import com.connectify.connectify.entity.Account;
import com.connectify.connectify.entity.PhoneNumberStatus;
import com.connectify.connectify.enums.EError;
import com.connectify.connectify.exception.CustomException;
import com.connectify.connectify.repository.PhoneNumberStatusRepository;
import com.connectify.connectify.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class PhoneNumberStatusService {
    @Autowired
    private PhoneNumberStatusRepository phoneNumberStatusRepository;

    @Autowired
    private DateUtils dateUtils;

    @Autowired
    @Lazy
    private AccountService accountService;

    @Autowired
    private EmailService emailService;

    @Value("${OTP.MAX_RESEND}")
    private int maxResend;
    @Value("${OTP.MAX_RETRY}")
    private int maxRetry;

    public PhoneNumberStatus getPhoneNumberStatus(String phoneNumber) {
        return phoneNumberStatusRepository.findById(phoneNumber).orElse(null);
    }

    public GetOtpResponse getOtp (GetOtpRequest request) {
        Random random = new Random();
        String otp = String.format("%06d", random.nextInt(1000000));

        PhoneNumberStatus phoneNumberStatus = getPhoneNumberStatus(request.getPhoneNumber());
        if (phoneNumberStatus != null&& phoneNumberStatus.getRemainResent() == 0 && phoneNumberStatus.getBlockedTime() != null && phoneNumberStatus.getBlockedTime().after(new Date())) throw new CustomException(EError.PHONE_NUMBER_BLOCKED);
        Account account = accountService.getAccountByPhoneNumber(request.getPhoneNumber());
        if (account == null) throw new CustomException(EError.USER_NOT_EXISTED);

        String emailBody = "Mã OTP của bạn là: " + otp +" , mã sẽ có hiệu lực trong 5p";
        String emailSubject = "Connectify";
        emailService.sendEmail(account.getEmail(), emailSubject, emailBody);

        if (phoneNumberStatus == null) {
            phoneNumberStatus = new PhoneNumberStatus();
            phoneNumberStatus.setId(request.getPhoneNumber());
            phoneNumberStatus.setRemainResent(maxResend - 1);
        } else {
            int remainResent = phoneNumberStatus.getRemainResent() == 0 ? maxResend - 1 : phoneNumberStatus.getRemainResent() - 1;
            phoneNumberStatus.setRemainResent(remainResent);
        }
        phoneNumberStatus.setRemainRetried(maxRetry);
        phoneNumberStatus.setOtp(otp);
        phoneNumberStatus.setOtpExpiryTime(dateUtils.addSeconds(new Date(), 300));
        phoneNumberStatus.setBlockedTime(dateUtils.addSeconds(new Date(), 7200));
        phoneNumberStatusRepository.save(phoneNumberStatus);
        GetOtpResponse response = new GetOtpResponse();
        response.setRemainResent(phoneNumberStatus.getRemainResent());
        response.setRemainRetried(maxRetry);
        return response;
    }

    public VerifyOtpResponse verifyOtp (VerifyOtpRequest request) {
        PhoneNumberStatus phoneNumberStatus = getPhoneNumberStatus(request.getPhoneNumber());
        if (phoneNumberStatus == null) throw new CustomException(EError.BAD_REQUEST);
        VerifyOtpResponse response = new VerifyOtpResponse();
        if (phoneNumberStatus.getRemainRetried() == 0) {
            response.setCorrect(false);
            response.setRemainRetried(0);
            response.setRemainResent(phoneNumberStatus.getRemainResent());
            return response;
        }
        boolean isOptCorrect = phoneNumberStatus.getOtp().equals(request.getOtp()) && phoneNumberStatus.getOtpExpiryTime().after(new Date());
        if (isOptCorrect) {
            phoneNumberStatus.setRemainRetried(maxRetry);
            phoneNumberStatus.setRemainResent(maxResend);
        } else {
            phoneNumberStatus.setRemainRetried(phoneNumberStatus.getRemainRetried() - 1);
        }
        phoneNumberStatusRepository.save(phoneNumberStatus);
        response.setCorrect(isOptCorrect);
        response.setRemainRetried(phoneNumberStatus.getRemainRetried());
        response.setRemainResent(phoneNumberStatus.getRemainResent());
        return response;
    }
}
