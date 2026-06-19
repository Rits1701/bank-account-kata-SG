package com.bank;

import com.bank.application.BankAccountService;
import com.bank.domain.model.Amount;
import org.junit.jupiter.api.Test;

class AccountSpec {

    @Test
    void should_deposit_money() {

        BankAccountService service =
                new BankAccountService();

        service.deposit(Amount.of("1000"));
    }
}