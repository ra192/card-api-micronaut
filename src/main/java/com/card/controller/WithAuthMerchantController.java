package com.card.controller;

import com.card.entity.Merchant;
import com.card.service.MerchantService;
import com.card.service.TokenService;
import com.card.service.exception.MerchantException;
import com.card.service.exception.TokenException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WithAuthMerchantController {
    private static final Logger logger = LoggerFactory.getLogger(WithAuthMerchantController.class);

    private final MerchantService merchantService;
    private final TokenService tokenService;

    public WithAuthMerchantController(MerchantService merchantService, TokenService tokenService) {
        this.merchantService = merchantService;
        this.tokenService = tokenService;
    }

    protected Merchant validateToken(String authHeader) throws TokenException, MerchantException {
        logger.info("Authorization header: {}", authHeader);
        final var token = StringUtils.replace(authHeader, "Bearer", "").trim();
        return merchantService.findById(tokenService.validate(token).getMerchantId());
    }
}
