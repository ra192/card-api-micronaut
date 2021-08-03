package com.card.entity;

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
    private Account srcAccount;

    @Relation(value = Relation.Kind.MANY_TO_ONE)
    private Account destAccount;

    private LocalDateTime created;

    private Card card;

    @Relation(value = Relation.Kind.MANY_TO_ONE)
    private Transaction transaction;

    public TransactionItem() {
    }
    public TransactionItem(Long amount, Account srcAccount, Account destAccount, Card card, Transaction transaction) {
        this.amount = amount;
        this.srcAccount = srcAccount;
        this.destAccount=destAccount;
        this.created = LocalDateTime.now();
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

    public Account getSrcAccount() {
        return srcAccount;
    }

    public void setSrcAccount(Account srcAccount) {
        this.srcAccount = srcAccount;
    }

    public Account getDestAccount() {
        return destAccount;
    }

    public void setDestAccount(Account destAccount) {
        this.destAccount = destAccount;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
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
