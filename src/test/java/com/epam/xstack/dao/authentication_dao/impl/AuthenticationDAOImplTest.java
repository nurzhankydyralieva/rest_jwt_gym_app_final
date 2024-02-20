package com.epam.xstack.dao.authentication_dao.impl;

import com.epam.xstack.mapper.authentication_mapper.AuthenticationChangeLoginRequestMapper;
import com.epam.xstack.models.dto.authentication_dto.AuthenticationChangeLoginRequestDTO;
import com.epam.xstack.models.dto.authentication_dto.AuthenticationResponseDTO;
import com.epam.xstack.models.entity.User;
import com.epam.xstack.models.enums.Code;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationDAOImplTest {
    @InjectMocks
    private AuthenticationDAOImpl service;

    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @Mock
    private AuthenticationChangeLoginRequestMapper requestMapper;

    @Test
    public void testShouldChangeLoginAuthentication() {
        UUID id = UUID.randomUUID();
        AuthenticationChangeLoginRequestDTO requestDTO = new AuthenticationChangeLoginRequestDTO();
        requestDTO.setUserName("Oscar.Wild");
        requestDTO.setNewPassword("newPassword");

        User userToBeUpdated = new User();
        userToBeUpdated.setPassword("oldPassword");

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(sessionFactory.getCurrentSession().get(User.class, id)).thenReturn(userToBeUpdated);
        when(requestMapper.toEntity(requestDTO)).thenReturn(new User());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(requestDTO.getNewPassword());

        AuthenticationResponseDTO responseDTO = service.authenticationChangeLogin(id, requestDTO);

        verify(sessionFactory.getCurrentSession()).update(userToBeUpdated);
        assertEquals("Login change response", responseDTO.getResponse());
        assertEquals(Code.STATUS_200_OK, responseDTO.getCode());
        assertNotNull(encodedPassword);
        assertNotNull(responseDTO);
        assertTrue(encoder.matches(requestDTO.getNewPassword(), userToBeUpdated.getPassword()));

    }
}
