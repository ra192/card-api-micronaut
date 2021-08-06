package com.card.service;

import com.card.entity.Account;
import com.card.entity.Card;
import com.card.entity.Transaction;
import com.card.entity.TransactionItem;
import com.card.entity.enums.TransactionStatus;
import com.card.entity.enums.TransactionType;
import com.card.repository.TransactionFeeRepository;
import com.card.repository.TransactionItemRepository;
import com.card.repository.TransactionRepository;
import com.card.service.exception.AccountException;
import com.card.service.exception.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class TransactionService {
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    private static final Long CASH_ACCOUNT_ID = 1L;

    private final TransactionRepository transactionRepository;
    private final TransactionItemRepository transactionItemRepository;
    private final TransactionFeeRepository transactionFeeRepository;
    private final AccountService accountService;

    public TransactionService(TransactionRepository transactionRepository, TransactionItemRepository transactionItemRepository, TransactionFeeRepository transactionFeeRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.transactionItemRepository = transactionItemRepository;
        this.transactionFeeRepository = transactionFeeRepository;
        this.accountService = accountService;
    }

    public Transaction deposit(Account srcAccount, Account destAccount, Account feeAccount, Long amount, TransactionType type, String orderId, Card card) throws TransactionException {
        final var feeAmount = calculateFee(amount, type, destAccount);
        if (sumByAccount(srcAccount) - amount < 0)
            throw new TransactionException("Account does not have enough funds");

        final var transaction = createTransaction(srcAccount, destAccount, amount, type, orderId, card);
        if (feeAmount > 0)
            transactionItemRepository.save(new TransactionItem(amount, destAccount, feeAccount, null, transaction));

        log.info("{} transaction was created", type);

        return transaction;
    }

    public Transaction fund(Account account, Long amount, String orderId) throws AccountException {
        final var cashAccount = accountService.findActiveById(CASH_ACCOUNT_ID);
        final var transaction = createTransaction(cashAccount, account, amount, TransactionType.FUND, orderId, null);

        log.info("{} transaction was created", TransactionType.FUND);

        return transaction;
    }

    public Transaction withdraw(Account srcAccount, Account destAccount, Account feeAccount, Long amount, TransactionType type, String orderId, Card card) throws TransactionException {
        final var feeAmount = calculateFee(amount, type, srcAccount);
        if (sumByAccount(srcAccount) - amount - feeAmount < 0)
            throw new TransactionException("Account does not have enough funds");

        final var transaction = createTransaction(srcAccount, destAccount, amount, type, orderId, card);
        if (feeAmount > 0)
            transactionItemRepository.save(new TransactionItem(amount, srcAccount, feeAccount, null, transaction));

        log.info("{} transaction was created", type);

        return transaction;
    }

    private Transaction createTransaction(Account srcAccount, Account destAccount, Long amount, TransactionType type,
                                          String orderId, Card card) {
        final var transaction = transactionRepository.save(new Transaction(orderId, type, TransactionStatus.COMPLETED));
        transactionItemRepository.save(new TransactionItem(amount, srcAccount, destAccount, card, transaction));

        return transaction;
    }

    private long calculateFee(Long amount, TransactionType type, Account account) {
        return transactionFeeRepository.findByTypeAndAccount(type, account)
                .map(fee -> amount * fee.getRate().longValue()).orElse(0L);
    }

    private Long sumByAccount(Account account) {
        return transactionItemRepository.findSumAmountByDestAccount(account).map(Number::longValue).orElse(0L)
                - transactionItemRepository.findSumAmountBySrcAccount(account).map(Number::longValue).orElse(0L);
    }
}
