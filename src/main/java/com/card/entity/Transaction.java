package com.card.entity;

import com.card.entity.enums.TransactionStatus;
import com.card.entity.enums.TransactionType;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import io.micronaut.data.jdbc.annotation.JoinColumn;

import java.util.List;

@MappedEntity
public class Transaction {
    @Id
    @GeneratedValue(value = GeneratedValue.Type.SEQUENCE)
    private Long id;
    private String orderId;
    private TransactionType type;
    private TransactionStatus status;
    @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "transaction")
    private List<TransactionItem>items;

    public Transaction() {
    }

    public Transaction(String orderId, TransactionType type, TransactionStatus status) {
        this.orderId = orderId;
        this.type = type;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public List<TransactionItem> getItems() {
        return items;
    }

    public void setItems(List<TransactionItem> items) {
        this.items = items;
    }
}
