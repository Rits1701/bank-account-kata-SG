package com.bank.adapters.out;

import com.bank.domain.model.Transaction;
import com.bank.domain.model.TransactionType;
import com.bank.ports.out.StatementPrinter;

import java.util.List;

public class ConsoleStatementPrinter implements StatementPrinter {

    public String print(List<Transaction> transactions) {

        StringBuilder sb = new StringBuilder();

        for (Transaction t : transactions) {
            String sign = t.type() == TransactionType.DEPOSIT ? "+" : "-";

            sb.append(t.date())
                    .append(" || ")
                    .append(sign)
                    .append(t.amount())
                    .append(" || ")
                    .append(t.balanceAfterOperation())
                    .append(System.lineSeparator());
        }

        return sb.toString();
    }
}
