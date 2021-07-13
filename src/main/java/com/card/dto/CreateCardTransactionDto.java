package com.card.dto;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotNull;

@Introspected
public class CreateCardTransactionDto {
    @NotNull(message = "card id is required")
    private Long cardId;
    @NotNull(message = "amount is required")
    private Long amount;
    @NotNull(message = "order id is required")
    private String orderId;

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "CreateCardTransactionDto{" +
                "cardId=" + cardId +
                ", amount=" + amount +
                ", orderId='" + orderId + '\'' +
                '}';
    }
}
