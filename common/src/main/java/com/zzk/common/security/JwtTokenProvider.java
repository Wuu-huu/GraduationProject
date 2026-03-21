package com.zzk.common.security;

import com.zzk.common.constant.SecurityConstants;
import com.zzk.common.model.security.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

/**
 * JWT 生成与解析工具。
 */
@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String createToken(LoginUser loginUser) {
        Instant now = Instant.now();
        Instant expireAt = now.plusSeconds(jwtProperties.getAccessTokenExpireSeconds());
        return Jwts.builder()
                .issuer(jwtProperties.getIssuer())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expireAt))
                .claim(SecurityConstants.CLAIM_UID, loginUser.getUid())
                .claim(SecurityConstants.CLAIM_USERNAME, loginUser.getUsername())
                .claim(SecurityConstants.CLAIM_ROLE, loginUser.getRole())
                .signWith(getSecretKey())
                .compact();
    }

    public LoginUser parseToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return LoginUser.builder()
                .uid(claims.get(SecurityConstants.CLAIM_UID, Long.class))
                .username(claims.get(SecurityConstants.CLAIM_USERNAME, String.class))
                .role(claims.get(SecurityConstants.CLAIM_ROLE, Integer.class))
                .build();
    }

    private SecretKey getSecretKey() {
        // 当前阶段使用对称密钥签名，后续拆分服务时可切换到更独立的密钥管理方案。
        byte[] secretBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(secretBytes);
    }
}
