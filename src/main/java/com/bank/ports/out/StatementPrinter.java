package com.bank.ports.out;

import com.bank.domain.model.Transaction;
import java.util.List;

public interface StatementPrinter {
    String print(List<Transaction> transactions);
}
