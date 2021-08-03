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
import com.card.service.exception.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Optional;

@Singleton
public class TransactionService {
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepository transactionRepository;
    private final TransactionItemRepository transactionItemRepository;
    private final TransactionFeeRepository transactionFeeRepository;

    public TransactionService(TransactionRepository transactionRepository, TransactionItemRepository transactionItemRepository, TransactionFeeRepository transactionFeeRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionItemRepository = transactionItemRepository;
        this.transactionFeeRepository = transactionFeeRepository;
    }

    public Transaction deposit(Account srcAccount, Account destAccount, Account feeAccount, Long amount, TransactionType type, String orderId, Card card) {
        final var transaction = createTransaction(srcAccount, destAccount, amount, type, orderId, card);
        createFeeItem(destAccount, feeAccount, amount,type, transaction);

        log.info("{} transaction was created", type);

        return transaction;
    }

    public Transaction withdraw(Account srcAccount, Account destAccount, Account feeAccount, Long amount, TransactionType type, String orderId, Card card) throws TransactionException {
        if (sumByAccount(srcAccount) - amount < 0)
            throw new TransactionException("Account does not have enough funds");

        final var transaction = createTransaction(srcAccount, destAccount, amount, type, orderId, card);
        createFeeItem(destAccount, feeAccount, amount,type, transaction);

        log.info("{} transaction was created", type);

        return transaction;
    }

    private Transaction createTransaction(Account srcAccount, Account destAccount, Long amount, TransactionType type,
                                          String orderId, Card card) {
        final var transaction = transactionRepository.save(new Transaction(orderId, type, TransactionStatus.COMPLETED));
        transactionItemRepository.save(new TransactionItem(amount, srcAccount, destAccount, card, transaction));

        return transaction;
    }

    private Optional<TransactionItem> createFeeItem(Account srcAccount, Account destAccount, Long amount, TransactionType type,
                                                    Transaction transaction) {
        return transactionFeeRepository.findByTypeAndAccount(type, srcAccount).map(fee ->
                new TransactionItem(amount * fee.getRate().longValue(), srcAccount, destAccount, null, transaction));
    }

    private long sumByAccount(Account account) {
        return transactionItemRepository.findSumAmountByDestAccount(account).orElse(0L)
                - transactionItemRepository.findSumAmountBySrcAccount(account).orElse(0L);
    }
}
