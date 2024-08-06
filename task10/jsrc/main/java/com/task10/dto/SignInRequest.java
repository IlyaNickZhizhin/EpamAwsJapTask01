package com.task10.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignInRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @Override
    public String toString() {
        return "SignInRequest{" +
                "email='" + email + '\'' +
                ", password='" + "***" + password.charAt(password.length()/2) + "***" + '\'' +
                '}';
    }
}
