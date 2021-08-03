package com.card.dto;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotNull;

@Introspected
public class FundAccountDto {
    @NotNull(message = "account id is required")
    private Long accountId;

    @NotNull(message = "amount id is required")
    private Long amount;

    @NotNull(message = "order id is required")
    private String orderId;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
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
        return "FundAccountDto{" +
                "accountId=" + accountId +
                ", amount=" + amount +
                ", orderId='" + orderId + '\'' +
                '}';
    }
}
