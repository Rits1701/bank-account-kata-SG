package com.bank.adapters.out;

import com.bank.domain.model.Transaction;
import com.bank.ports.out.TransactionRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTransactionRepository implements TransactionRepository {

    private final List<Transaction> transactions = new ArrayList<>();

    public void save(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> findAll() {
        return List.copyOf(transactions);
    }
}