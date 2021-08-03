package com.card.controller;

import com.card.dto.CardDto;
import com.card.dto.CreateCardDto;
import com.card.dto.CreateCardTransactionDto;
import com.card.dto.TransactionDto;
import com.card.entity.Card;
import com.card.entity.enums.CardType;
import com.card.service.*;
import com.card.service.exception.*;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;

@Controller("/api/card")
public class CardController extends WithAuthMerchantController {
    private static final Logger logger = LoggerFactory.getLogger(CardController.class);

    private final AccountService accountService;
    private final CustomerService customerService;
    private final CardService cardService;

    public CardController(MerchantService merchantService, TokenService tokenService, AccountService accountService,
                          CustomerService customerService, CardService cardService) {
        super(merchantService, tokenService);
        this.accountService = accountService;
        this.customerService = customerService;
        this.cardService = cardService;
    }

    @Post
    public CardDto createVirtual(@Header String authorization, @Valid @Body CreateCardDto requestObject)
            throws CustomerException, MerchantException, TokenException, InvocationTargetException,
            IllegalAccessException, AccountException {
        logger.info("Create virtual card method was called with params:");
        logger.info(requestObject.toString());

        validateToken(authorization);
        final var card = new Card(CardType.VIRTUAL, customerService.findActiveById(requestObject.getCustomerId()),
                accountService.findActiveById(requestObject.getAccountId()));
        cardService.create(card);

        final var result = new CardDto();
        BeanUtils.copyProperties(result, card);

        return result;
    }

    @Post("/deposit")
    public TransactionDto deposit(@Header String authorization, @Valid @Body CreateCardTransactionDto requestObject)
            throws TransactionException, CardException, MerchantException, TokenException, AccountException {
        logger.info("Card deposit method was called with params:");
        logger.info(requestObject.toString());

        validateToken(authorization);
        final var transaction = cardService.deposit(cardService.findById(requestObject.getCardId()),
                requestObject.getAmount(), requestObject.getOrderId());

        return new TransactionDto(transaction.getId(), transaction.getStatus());
    }

    @Post("/withdraw")
    public TransactionDto withdraw(@Header String authorization, @Valid @Body CreateCardTransactionDto requestObject)
            throws CardException, MerchantException, TokenException, AccountException {
        logger.info("Card withdraw method was called with params:");
        logger.info(requestObject.toString());

        validateToken(authorization);
        final var transaction = cardService.withdraw(cardService.findById(requestObject.getCardId()),
                requestObject.getAmount(), requestObject.getOrderId());

        return new TransactionDto(transaction.getId(), transaction.getStatus());
    }
}
