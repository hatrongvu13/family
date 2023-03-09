package com.jax.core.authentication.service.impl;

import com.jax.core.authentication.dvo.ERoleDVO;
import com.jax.core.authentication.dvo.UserDVO;
import com.jax.core.authentication.dvo.UserPrincipal;
import com.jax.core.authentication.dvo.request.LoginRequest;
import com.jax.core.authentication.dvo.request.RegisterRequest;
import com.jax.core.authentication.dvo.responses.AuthenticationResponse;
import com.jax.core.authentication.dvo.responses.UserInfo;
import com.jax.core.authentication.repo.UserRepository;
import com.jax.core.authentication.service.AuthenticationService;
import com.jax.core.authentication.utils.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final TokenProvider tokenProvider;
    private final PasswordEncoder encoder;
//    private final DozerBeanMapper mapper;
    private final UserRepository userRepository;
//    private final LogRepository logRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {
        UserDVO userDVO = userRepository.findByEmail(loginRequest.getUsername()).orElse(null);
        if (userDVO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid account !");
        }
        if (!encoder.matches(userDVO.getPassword(), loginRequest.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username or password wrong !");
        }
        return authorization(userDVO);
    }

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already exist account !");
        }
        UserDVO userDVO = new UserDVO();
        userDVO.setEmail(request.getEmail());
        userDVO.setUsername(request.getUsername());
        userDVO.setPassword(encoder.encode(request.getPassword()));
        userDVO.setRole(ERoleDVO.USER);
        userDVO = userRepository.save(userDVO);
        return authorization(userDVO);
    }

    @Override
    public UserInfo info(UserPrincipal currentUser) {
        UserDVO userDVO = userRepository.findByEmail(currentUser.getEmail()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token")
        );
        return UserInfo.builder()
                .username(userDVO.getUsername())
                .email(userDVO.getEmail())
                .build();
    }

    private AuthenticationResponse authorization(UserDVO userDVO) {
        String token = tokenProvider.issueToken(
                UserPrincipal.create(
                        userDVO.getId().toString()
                        , userDVO.getUsername()
                        , userDVO.getEmail(),
                        List.of(userDVO.getRole().toString()))
        );
        String key = "TOKEN_" + UUID.randomUUID();
        redisTemplate.opsForValue().set(key, token);
        redisTemplate.expire(key, 3600, TimeUnit.SECONDS);

        return new AuthenticationResponse(key);
    }
}
