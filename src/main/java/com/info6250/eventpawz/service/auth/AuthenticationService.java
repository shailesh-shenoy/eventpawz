package com.info6250.eventpawz.service.auth;

import com.info6250.eventpawz.config.jwt.JWTService;
import com.info6250.eventpawz.model.auth.AuthenticationResponse;
import com.info6250.eventpawz.model.auth.LoginRequest;
import com.info6250.eventpawz.model.auth.RegisterRequest;
import com.info6250.eventpawz.model.user.AppUser;
import com.info6250.eventpawz.model.user.AppUserDao;
import com.info6250.eventpawz.model.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AppUserDao appUserDao;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        AppUser appUser = AppUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .name(request.getName())
                .role(Role.MEMBER)
                .enabled(true)
                .build();

        appUserDao.save(appUser);
        var jwtToken = jwtService.generateToken(appUser);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var appUser = appUserDao.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(appUser);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
