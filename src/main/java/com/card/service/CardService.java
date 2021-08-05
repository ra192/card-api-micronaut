package com.card.service;

import com.card.entity.Card;
import com.card.entity.Transaction;
import com.card.entity.enums.TransactionType;
import com.card.repository.CardRepository;
import com.card.service.exception.AccountException;
import com.card.service.exception.CardException;
import com.card.service.exception.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.time.LocalDateTime;

@Singleton
public class CardService {
    private static final Logger logger = LoggerFactory.getLogger(CardService.class);

    private static final Long CARD_ACCOUNT_ID = 2L;
    private static final Long FEE_ACCOUNT_ID = 3L;

    private final CardRepository cardRepository;
    private final AccountService accountService;
    private final TransactionService transactionService;

    public CardService(CardRepository cardRepository, AccountService accountService, TransactionService transactionService) {
        this.cardRepository = cardRepository;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    public void create(Card card) {
        card.setProviderReferenceId("xxxx");
        card.setInfo("xxxx");
        card.setCreated(LocalDateTime.now());
        cardRepository.save(card);
        logger.info("Card was created with id: {}", card.getId());
    }

    public Transaction deposit(Card card, Long amount, String orderId) throws TransactionException, AccountException {
        return transactionService.withdraw(accountService.findActiveById(card.getAccount().getId()),
                accountService.findActiveById(CARD_ACCOUNT_ID),
                accountService.findActiveById(FEE_ACCOUNT_ID), amount,
                TransactionType.VIRTUAL_CARD_DEPOSIT, orderId, card);
    }

    public Card findById(Long id) throws CardException {
        return cardRepository.findById(id).orElseThrow(() -> new CardException("Card does not exist"));
    }

    public Transaction withdraw(Card card, Long amount, String orderId) throws AccountException, TransactionException {
        return transactionService.deposit(accountService.findActiveById(CARD_ACCOUNT_ID),
                accountService.findActiveById(card.getAccount().getId()),
                accountService.findActiveById(FEE_ACCOUNT_ID), amount,
                TransactionType.VIRTUAL_CARD_WITHDRAW, orderId, card);
    }
}
