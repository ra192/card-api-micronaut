package com.card.controller;

import com.card.dto.FundAccountDto;
import com.card.dto.TransactionDto;
import com.card.service.AccountService;
import com.card.service.MerchantService;
import com.card.service.TokenService;
import com.card.service.TransactionService;
import com.card.service.exception.AccountException;
import com.card.service.exception.MerchantException;
import com.card.service.exception.TokenException;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;

@Controller("/api/account")
public class AccountController extends WithAuthMerchantController {
    private static final Logger looger = LoggerFactory.getLogger(AccountController.class);

    private static final Long INTERNAL_MERCHANT_ID = 1L;

    private final AccountService accountService;
    private final TransactionService transactionService;

    public AccountController(MerchantService merchantService, TokenService tokenService, AccountService accountService, TransactionService transactionService) {
        super(merchantService, tokenService);
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @Post("/fund")
    public TransactionDto fund(@Header String authorization, @Valid @Body FundAccountDto requestObject) throws MerchantException, TokenException, AccountException {
        looger.info("Fund account method was called with params:");
        looger.info(requestObject.toString());

        final var merchant = validateToken(authorization);
        if (!merchant.getId().equals(INTERNAL_MERCHANT_ID)) throw new MerchantException("Internal merchant required");

        final var transaction = transactionService.fund(accountService.findActiveById(requestObject.getAccountId()), requestObject.getAmount(), requestObject.getOrderId());

        return new TransactionDto(transaction.getId(), transaction.getStatus());
    }
}
