package com.card.controller;

import com.card.dto.CreateTokenDto;
import com.card.service.MerchantService;
import com.card.service.TokenService;
import com.card.service.data.Token;
import com.card.service.exception.MerchantException;
import com.card.service.exception.TokenException;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

@Controller("/api/token")
public class TokenController {
    private final MerchantService merchantService;
    private final TokenService tokenService;

    public TokenController(MerchantService merchantService, TokenService tokenService) {
        this.merchantService = merchantService;
        this.tokenService = tokenService;
    }

    @Post
    public Token create(@Valid CreateTokenDto requestDto) throws MerchantException, TokenException, NoSuchAlgorithmException {
        return tokenService.create(merchantService.findById(requestDto.getMerchantId()), requestDto.getSecret());
    }
}
