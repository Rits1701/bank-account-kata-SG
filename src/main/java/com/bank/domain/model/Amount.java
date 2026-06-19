package com.bank.domain.model;

import java.math.BigDecimal;

public record Amount(BigDecimal value) {

    public static Amount of(String value) {
        return new Amount(new BigDecimal(value));
    }

    public Amount add(Amount other) {
        return new Amount(this.value.add(other.value));
    }

    public Amount subtract(Amount other) {
        return new Amount(this.value.subtract(other.value));
    }

    public boolean isLessThanOrEqualZero() {
        return value.compareTo(BigDecimal.ZERO) <= 0;
    }

    @Override
    public String toString() {
        return value.toPlainString();
    }
}
