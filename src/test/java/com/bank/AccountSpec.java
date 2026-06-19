package com.bank;

import com.bank.adapters.out.ConsoleStatementPrinter;
import com.bank.adapters.out.InMemoryTransactionRepository;
import com.bank.application.BankAccountService;
import com.bank.domain.model.Amount;
import com.bank.ports.out.ClockProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AccountSpec {

    private BankAccountService service;

    @BeforeEach
    void setup() {
        ClockProvider clock = () -> LocalDate.of(2026, 6, 14);

        service = new BankAccountService(
                new InMemoryTransactionRepository(),
                clock,
                new ConsoleStatementPrinter());
    }

    @Test
    void should_deposit_money() {
        service.deposit(Amount.of("1000"));
        String statement = service.printStatement();

        assertThat(statement)
                .contains("+1000")
                .contains("1000");
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