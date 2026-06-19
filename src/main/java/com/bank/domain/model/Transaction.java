package com.bank.domain.model;

import java.time.LocalDate;

public record Transaction(
        TransactionType type,
        LocalDate date,
        Amount amount,
        Amount balanceAfterOperation
) {
}
