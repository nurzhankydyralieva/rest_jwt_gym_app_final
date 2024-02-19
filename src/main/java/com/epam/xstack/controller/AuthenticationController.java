package com.epam.xstack.controller;

import com.epam.xstack.configuration.jwt_config.JwtTokenUtil;
import com.epam.xstack.models.dto.authentication_dto.AuthenticationChangeLoginRequestDTO;
import com.epam.xstack.models.dto.authentication_dto.AuthenticationResponseDTO;
import com.epam.xstack.models.dto.authentication_dto.LoginRequestDTO;
import com.epam.xstack.models.dto.authentication_dto.LoginResponseDTO;
import com.epam.xstack.models.enums.Code;
import com.epam.xstack.service.authentication_service.AuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Api(tags = "Authentication controller")
@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationService authenticationService;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User authenticated successfully"),
            @ApiResponse(code = 403, message = "Access denied, check user name or id"),
            @ApiResponse(code = 401, message = "Bad credentials"),
            @ApiResponse(code = 422, message = "User or password is null"),
            @ApiResponse(code = 404, message = "User with user name or id not found")
    })
    @ApiOperation(value = "Login the user")
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequestDTO authenticationRequest) throws Exception {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword()));
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final String token = jwtTokenUtil.generateToken(userDetails);
        return new ResponseEntity<>(LoginResponseDTO.builder().jwtToken(token).code(Code.STATUS_200_OK).build(), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User is updated"),
            @ApiResponse(code = 403, message = "Access denied, check user name or id"),
            @ApiResponse(code = 401, message = "Bad credentials"),
            @ApiResponse(code = 422, message = "User or password is null"),
            @ApiResponse(code = 404, message = "User with user name or id not found")
    })
    @ApiOperation(value = "Changes login")
    @PutMapping("/update/{id}")
    public ResponseEntity<AuthenticationResponseDTO> updateLogin(@PathVariable("id") UUID id, @Valid @RequestBody AuthenticationChangeLoginRequestDTO requestDTO) {
        return new ResponseEntity<>(authenticationService.authenticationChangeLogin(id, requestDTO), HttpStatus.OK);
    }

}
