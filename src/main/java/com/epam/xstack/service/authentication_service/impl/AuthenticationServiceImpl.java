package com.epam.xstack.service.authentication_service.impl;


import com.epam.xstack.dao.authentication_dao.AuthenticationDAO;
import com.epam.xstack.models.dto.authentication_dto.AuthenticationChangeLoginRequestDTO;
import com.epam.xstack.models.dto.authentication_dto.AuthenticationResponseDTO;
import com.epam.xstack.service.authentication_service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationDAO authenticationDAO;

    @Override
    public AuthenticationResponseDTO authenticationChangeLogin(UUID id, AuthenticationChangeLoginRequestDTO requestDTO) {
        return authenticationDAO.authenticationChangeLogin(id, requestDTO);
    }
}
