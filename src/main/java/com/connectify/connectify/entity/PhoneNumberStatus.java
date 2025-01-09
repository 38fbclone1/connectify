package com.connectify.connectify.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PhoneNumberStatus {
    @Id
    private String id;

    private int remainResent;

    private int remainRetried;

    private Date blockedTime;

    private String otp;

    private Date otpExpiryTime;
}
