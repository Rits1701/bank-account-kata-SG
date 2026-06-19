package com.bank.application;

import com.bank.domain.model.Amount;
import com.bank.domain.model.Transaction;
import com.bank.domain.model.TransactionType;
import com.bank.ports.in.BankAccountUseCase;
import com.bank.ports.out.ClockProvider;
import com.bank.ports.out.StatementPrinter;
import com.bank.ports.out.TransactionRepository;

public class BankAccountService implements BankAccountUseCase {
    private final TransactionRepository repository;
    private final ClockProvider clock;
    private Amount balance = Amount.of("0");

    public BankAccountService(
            TransactionRepository repository,
            ClockProvider clock) {
        this.repository = repository;
        this.clock = clock;
    }


    public void deposit(Amount amount) {
        balance = balance.add(amount);

        repository.save(
                new Transaction(
                        TransactionType.DEPOSIT,
                        clock.now(),
                        amount,
                        balance));

    }

    public void withdraw(Amount amount) {
        balance = balance.subtract(amount);

        repository.save(
                new Transaction(
                        TransactionType.WITHDRAWAL,
                        clock.now(),
                        amount,
                        balance));
    }

    public String printStatement() {
        return "";
    }

}
