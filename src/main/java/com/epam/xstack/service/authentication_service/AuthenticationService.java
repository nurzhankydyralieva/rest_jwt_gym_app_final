package com.epam.xstack.service.authentication_service;

import com.epam.xstack.models.dto.authentication_dto.AuthenticationChangeLoginRequestDTO;
import com.epam.xstack.models.dto.authentication_dto.AuthenticationResponseDTO;

import java.util.UUID;


public interface AuthenticationService {
    AuthenticationResponseDTO authenticationChangeLogin(UUID id, AuthenticationChangeLoginRequestDTO requestDTO);
}
