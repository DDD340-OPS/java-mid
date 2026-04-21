package com.midterm.ledger.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Ledger {
    private String name;
    private final List<Transaction> transactions;

    public Ledger(String name) {
        this.name = validateName(name);
        this.transactions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = validateName(name);
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(Objects.requireNonNull(transaction, "交易记录不能为空"));
    }

    public boolean removeTransactionById(String id) {
        return transactions.removeIf(transaction -> transaction.getId().equals(id));
    }

    public Optional<Transaction> findTransactionById(String id) {
        return transactions.stream()
                .filter(transaction -> transaction.getId().equals(id))
                .findFirst();
    }

    public void clearTransactions() {
        transactions.clear();
    }

    private static String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("账本名称不能为空");
        }
        return name.trim();
    }
}
