package com.card.entity;

import com.card.entity.enums.TransactionType;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;

@MappedEntity
public class TransactionFee {
    @Id
    @GeneratedValue(value = GeneratedValue.Type.SEQUENCE)
    private Long id;

    private TransactionType type;

    @Relation(value = Relation.Kind.MANY_TO_ONE)
    private Account account;

    private Float rate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }
}
