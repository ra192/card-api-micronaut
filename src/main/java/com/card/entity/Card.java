package com.card.entity;

import com.card.entity.enums.CardType;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@MappedEntity
public class Card {
    @Id
    @GeneratedValue(ref = "card_id_seq", value = GeneratedValue.Type.SEQUENCE)
    private Long id;

    @NotNull
    private String providerReferenceId;

    private CardType type;

    @Relation(value = Relation.Kind.MANY_TO_ONE)
    private Customer customer;

    @Relation(value = Relation.Kind.MANY_TO_ONE)
    private Account account;

    @NotNull
    private LocalDateTime created;

    private String info;

    public Card() {
    }

    public Card(CardType type, Customer customer, Account account) {
        this.type = type;
        this.customer = customer;
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProviderReferenceId() {
        return providerReferenceId;
    }

    public void setProviderReferenceId(String providerReferenceId) {
        this.providerReferenceId = providerReferenceId;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
