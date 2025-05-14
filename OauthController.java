package com.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dto.GoogleLoginRequestDTO;
import com.ecommerce.dto.LoginResponseDTO;
import com.ecommerce.services.oauth.GoogleOAuthService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class OauthController {

    private final GoogleOAuthService googleOAuthService;

    public OauthController(GoogleOAuthService googleOAuthService) {
        this.googleOAuthService = googleOAuthService;
    }

    @PostMapping("/google-login")
    public ResponseEntity<String> loginWithGoogle(@RequestBody GoogleLoginRequestDTO dto,HttpServletResponse response) {
    	LoginResponseDTO loginResponseDTO = googleOAuthService.loginOrRegisterWithGoogle(dto.getIdToken());
        response.setHeader("Authorization", "Bearer " + loginResponseDTO.getToken());
        return ResponseEntity.ok(loginResponseDTO.getMessage());
    }
}

