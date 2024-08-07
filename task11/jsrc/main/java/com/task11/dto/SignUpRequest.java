package com.task11.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters long and contain alphanumeric characters and special symbols.")
    private String password;

    @Override
    public String toString() {
        return "SignUpRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + "***" + password.charAt(password.length()/2) + "***" + '\'' +
                '}';
    }
}
