package com.card.dto;

import com.card.entity.enums.TransactionStatus;

public class TransactionDto {
    private final Long id;
    private final TransactionStatus status;

    public TransactionDto(Long id, TransactionStatus status) {
        this.id = id;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public TransactionStatus getStatus() {
        return status;
    }
}
