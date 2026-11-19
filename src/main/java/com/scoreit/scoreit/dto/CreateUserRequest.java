package com.scoreit.scoreit.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record CreateUserRequest(

        @NotBlank
        String username,

        @NotBlank
        @Email
        String email
) {
}
