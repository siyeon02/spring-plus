package org.example.expert.domain.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.SigninResponse;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.example.expert.domain.auth.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    public static final String AUTHORIZATION_HEADER = "Authorization";

    @PostMapping("/auth/signup")
    public SignupResponse signup(@Valid @RequestBody SignupRequest signupRequest) {
        return authService.signup(signupRequest);
    }

    @PostMapping("/auth/signin")
    public SigninResponse signin(
            @Valid @RequestBody SigninRequest signinRequest,
            HttpServletResponse res) {
        SigninResponse signinResponse = authService.signin(signinRequest);
        //        Cookie cookie = new Cookie(AUTHORIZATION_HEADER, signinResponse.getBearerToken());
        Cookie cookie = new Cookie(AUTHORIZATION_HEADER, URLEncoder.encode(signinResponse.getBearerToken()));

        cookie.setHttpOnly(true);
        res.addCookie(cookie);
        res.setHeader(AUTHORIZATION_HEADER, signinResponse.getBearerToken());
        return authService.signin(signinRequest);
    }
}
