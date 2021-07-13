package com.card.entity;

import com.card.entity.enums.TransactionItemType;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;

import java.time.LocalDateTime;

@MappedEntity
public class TransactionItem {
    @Id
    @GeneratedValue(ref = "transaction_item_id_seq", value = GeneratedValue.Type.SEQUENCE)
    private Long id;

    private Long amount;

    @Relation(value = Relation.Kind.MANY_TO_ONE)
    private Account account;

    private LocalDateTime created;

    private TransactionItemType type;

    private Card card;

    @Relation(value = Relation.Kind.MANY_TO_ONE)
    private Transaction transaction;

    public TransactionItem() {
    }
    public TransactionItem(Long amount, Account account, TransactionItemType type, Card card, Transaction transaction) {
        this.amount = amount;
        this.account = account;
        this.created = LocalDateTime.now();
        this.type = type;
        this.card = card;
        this.transaction=transaction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public TransactionItemType getType() {
        return type;
    }

    public void setType(TransactionItemType type) {
        this.type = type;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
