package com.card.service;

import com.card.entity.Account;
import com.card.entity.Card;
import com.card.entity.Transaction;
import com.card.entity.TransactionItem;
import com.card.entity.enums.TransactionItemType;
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

    public Transaction deposit(Account account, Long amount, TransactionType type, String orderId, Card card) {
        return createTransactionWithItems(account, amount, type, TransactionItemType.DEPOSIT, orderId, card);
    }

    public Transaction withdraw(Account account, Long amount, TransactionType type, String orderId, Card card) throws TransactionException {
        if (sumByAccount(account) - amount < 0)
            throw new TransactionException("Account does not have enough funds");

        return createTransactionWithItems(account, -amount, type, TransactionItemType.WITHDRAW, orderId, card);
    }

    private Transaction createTransactionWithItems(Account account, Long amount, TransactionType type,
                                                   TransactionItemType baseItemType, String orderId, Card card) {
        final var items = new ArrayList<TransactionItem>();
        final var transaction = transactionRepository.save(new Transaction(orderId, type, TransactionStatus.COMPLETED, items));
        items.add(new TransactionItem(amount, account, baseItemType, card, transaction));

        transactionFeeRepository.findByTypeAndAccount(type, account).ifPresent(fee ->
                items.add(new TransactionItem((amount > 0) ? -amount : amount * fee.getRate().longValue(), account, TransactionItemType.FEE, null, transaction)));


        log.info("{} transaction was created", type);

        return transaction;
    }

    private long sumByAccount(Account account) {
        return transactionItemRepository.findSumAmountByAccount(account).orElse(0L);
    }
}
