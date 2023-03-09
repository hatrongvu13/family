package com.jax.core.authentication.utils;

import com.jax.core.authentication.dvo.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenProvider {
    @Autowired
    private TokenKeyProvider tokenKeyProvider;

    @Value("${authentication.jwt.expire}")
    private int tokenTimeout;

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    public UserPrincipal getUserFromJwt(String token) {
        Claims claims = this.decodeJwt(token);
        List<GrantedAuthority> authorities = ((List<String>) claims.get("scopes"))
                .stream()
                .map(scope -> new SimpleGrantedAuthority(scope))
                .collect(Collectors.toList());
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setId((String) claims.get("id"));
        userPrincipal.setUsername((String) claims.get("username"));
        userPrincipal.setEmail((String) claims.get("email"));
        userPrincipal.setAuthorities(authorities);

        return userPrincipal;
    }

    private Claims decodeJwt(String token) {

        Claims claims = Jwts.parser()
                .setSigningKey(tokenKeyProvider.getPublicKey())
                .parseClaimsJws(token).getBody();

        return claims;
    }

    @SneakyThrows
    public String issueToken(UserPrincipal userPrincipal) {
        Claims claims = Jwts.claims().setSubject(userPrincipal.getId());
        claims.put("id", userPrincipal.getId());
        claims.put("username", userPrincipal.getUsername());
        claims.put("email", userPrincipal.getEmail());
        claims.put("scopes", userPrincipal.getAuthorities().stream().map(scope -> scope.toString()).collect(Collectors.toList()));
        claims.setIssuedAt(new Date());
        claims.setExpiration(new Date(System.currentTimeMillis() + tokenTimeout*1000));

        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.RS256, tokenKeyProvider.getPrivateKey())
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }
}
