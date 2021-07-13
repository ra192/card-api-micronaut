package com.card.service;

import com.card.entity.Merchant;
import com.card.service.data.Token;
import com.card.service.exception.TokenException;
import io.micronaut.cache.CacheManager;
import io.micronaut.cache.SyncCache;
import io.micronaut.context.annotation.Value;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;

@Singleton
public class TokenService {
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    private final SyncCache<?> cache;

    private final int tokenSize;
    private final long tokenLifetimeInMinutes;

    public TokenService(CacheManager cacheManager,
                        @Value("${com.card.auth.token.size}") int tokenSize,
                        @Value("${com.card.auth.token.lifetime}") long tokenLifetimeInMinutes) {
        cache = cacheManager.getCache("tokens");
        this.tokenSize = tokenSize;
        this.tokenLifetimeInMinutes = tokenLifetimeInMinutes;
    }

    public Token create(Merchant merchant, String secret) throws NoSuchAlgorithmException, TokenException {
        if (!merchant.getSecret().equalsIgnoreCase(sha256Hash(secret)))
            throw new TokenException("Secret is not valid");

        final var tokenStr = RandomStringUtils.randomAlphanumeric(tokenSize);
        final var expiredAt = LocalDateTime.now().plusMinutes(tokenLifetimeInMinutes);

        final var token = new Token(tokenStr, merchant.getId(), expiredAt);
        cache.put(tokenStr, token);

        logger.info("Token was created");

        return token;
    }

    public Token validate(String token) throws TokenException {
        final var tokenOpt = cache.get(token, Token.class);
        if (tokenOpt.isEmpty()) throw new TokenException("Token doesn't exist");
        if (LocalDateTime.now().isAfter(tokenOpt.get().getExpiredAt())) throw new TokenException("Token is expired");

        return tokenOpt.get();
    }

    private String sha256Hash(String text) throws NoSuchAlgorithmException {
        final var digest = MessageDigest.getInstance("SHA-256");
        return new String(Base64.getEncoder().encode(digest.digest(text.getBytes(StandardCharsets.UTF_8))),
                StandardCharsets.UTF_8);
    }
}
