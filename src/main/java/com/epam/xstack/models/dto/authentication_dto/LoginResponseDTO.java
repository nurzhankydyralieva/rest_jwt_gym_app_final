package com.epam.xstack.models.dto.authentication_dto;

import com.epam.xstack.models.enums.Code;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponseDTO implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    String jwtToken;
    Code code;
}