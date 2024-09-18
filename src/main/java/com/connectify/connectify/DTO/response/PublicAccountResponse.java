package com.connectify.connectify.DTO.response;

import com.connectify.connectify.enums.EGender;
import lombok.Data;

import java.util.Date;

@Data
public class PublicAccountResponse {
    private String id;
    private String displayName;
    private String profileImage;
    private String fullName;
    private Date dob;
    private EGender gender;
}
