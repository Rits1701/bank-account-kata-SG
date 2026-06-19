package com.bank.ports.in;

import com.bank.domain.model.Amount;

public interface BankAccountUseCase {
    void deposit(Amount amount);
    void withdraw(Amount amount);
    String printStatement();
}
