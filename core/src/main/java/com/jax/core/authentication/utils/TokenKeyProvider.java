package com.jax.core.authentication.utils;

import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Data
@Component
public class TokenKeyProvider {
    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;

    @Autowired
    @SneakyThrows
    public TokenKeyProvider(@Value("${authentication.jwt.public-key}") String publicKey,
                            @Value("${authentication.jwt.private-key}") String privateKey) {
        this.privateKey = RSAUtil.getPrivateKeyFromString(privateKey);
        this.publicKey = RSAUtil.getPublicKeyFromString(publicKey);
    }
}
