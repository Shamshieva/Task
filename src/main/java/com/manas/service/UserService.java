package com.manas.service;

import com.manas.dto.request.AuthenticationRequest;
import com.manas.dto.request.RegisterRequest;
import com.manas.dto.response.AuthenticationResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
