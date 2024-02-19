package com.epam.xstack.models.dto.authentication_dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationChangeLoginRequestDTO {
    @NotEmpty(message = "User name should not be empty")
    String userName;
    @NotBlank(message = "Old password is required")
    String oldPassword;
    @NotBlank(message = "New password is required")
    String newPassword;
}
