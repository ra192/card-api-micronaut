package com.card.service;

import com.card.entity.Account;
import com.card.entity.Transaction;
import com.card.entity.enums.TransactionType;
import com.card.repository.AccountRepository;
import com.card.service.exception.AccountException;

import javax.inject.Singleton;

@Singleton
public class AccountService {
    private static final Long CASH_ACCOUNT_ID = 1L;

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    public AccountService(AccountRepository accountRepository, TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    public Account findActiveById(Long id) throws AccountException {
        return accountRepository.findByIdAndActive(id, true)
                .orElseThrow(()->new AccountException("Account does not exist"));
    }

    public Transaction fund(Account account, Long amount, String orderId) throws AccountException {
        return transactionService.fund(findActiveById(CASH_ACCOUNT_ID),account,amount, TransactionType.FUND,orderId);
    }
}
