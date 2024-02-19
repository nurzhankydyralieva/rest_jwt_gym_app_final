package com.epam.xstack.mapper.authentication_mapper;

import com.epam.xstack.models.dto.authentication_dto.AuthenticationChangeLoginRequestDTO;
import com.epam.xstack.models.entity.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.Configuration;

@Mapper(componentModel = "spring")
public interface AuthenticationChangeLoginRequestMapper {
    @Mapping(source = "user.password", target = "oldPassword")
    AuthenticationChangeLoginRequestDTO toDto(User user);

    @InheritInverseConfiguration
    User toEntity(AuthenticationChangeLoginRequestDTO requestDTO);
}
