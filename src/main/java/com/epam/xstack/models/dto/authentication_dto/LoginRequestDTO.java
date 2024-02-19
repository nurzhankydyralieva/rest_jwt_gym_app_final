package com.epam.xstack.models.dto.authentication_dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequestDTO implements Serializable {
    private static final long serialVersionUID = 5926468583005150707L;
    String userName;
    String password;

}