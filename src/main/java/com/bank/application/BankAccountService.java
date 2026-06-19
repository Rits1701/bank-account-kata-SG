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
    private final StatementPrinter printer;
    private Amount balance = Amount.of("0");

    public BankAccountService(
            TransactionRepository repository,
            ClockProvider clock, StatementPrinter printer) {
        this.repository = repository;
        this.clock = clock;
        this.printer = printer;
    }


    public void deposit(Amount amount) {
        validate(amount);
        balance = balance.add(amount);

        repository.save(
                new Transaction(
                        TransactionType.DEPOSIT,
                        clock.now(),
                        amount,
                        balance));

    }

    public void withdraw(Amount amount) {
        validate(amount);
        balance = balance.subtract(amount);

        repository.save(
                new Transaction(
                        TransactionType.WITHDRAWAL,
                        clock.now(),
                        amount,
                        balance));
    }

    public String printStatement() {
        return printer.print(repository.findAll());
    }

    private void validate(Amount amount) {
        if (amount == null || amount.isLessThanOrEqualZero()) {
            throw new IllegalArgumentException("Amount must be strictly positive");
        }
    }

}
