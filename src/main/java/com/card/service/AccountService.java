package com.card.service;

import com.card.entity.Account;
import com.card.repository.AccountRepository;
import com.card.service.exception.AccountException;

import javax.inject.Singleton;

@Singleton
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account findActiveById(Long id) throws AccountException {
        return accountRepository.findByIdAndActive(id, true)
                .orElseThrow(()->new AccountException("Account does not exist"));
    }
}
