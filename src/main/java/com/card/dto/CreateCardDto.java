package com.card.dto;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotNull;

@Introspected
public class CreateCardDto {
    @NotNull(message = "account id is required")
    private Long accountId;
    @NotNull(message = "customer id is required")
    private Long customerId;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "CreateCardDto{" +
                "accountId=" + accountId +
                ", customerId=" + customerId +
                '}';
    }
}
