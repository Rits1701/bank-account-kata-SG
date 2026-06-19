package com.bank;

import com.bank.application.BankAccountService;
import com.bank.domain.model.Amount;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountSpec {

    private BankAccountService service;

    @Test
    void should_deposit_money() {

        service.deposit(Amount.of("1000"));
    }

    @Test
    void should_withdraw_money() {
        service.deposit(Amount.of("1000"));
        service.withdraw(Amount.of("300"));

        String statement = service.printStatement();

        assertThat(statement)
                .contains("-300")
                .contains("700");
    }
}