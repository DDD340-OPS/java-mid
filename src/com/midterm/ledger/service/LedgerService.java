package com.midterm.ledger.service;

import com.midterm.ledger.entity.Ledger;
import com.midterm.ledger.entity.Transaction;
import com.midterm.ledger.entity.TransactionType;
import com.midterm.ledger.util.IdGenerator;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class LedgerService {
    public Ledger createLedger(String name) {
        return new Ledger(name);
    }

    public Transaction addTransaction(
            Ledger ledger,
            LocalDate date,
            TransactionType type,
            String category,
            double amount,
            String note
    ) {
        validateInput(ledger, date, type, category, amount);
        Transaction transaction = new Transaction(
                IdGenerator.nextTransactionId(),
                date,
                type,
                category,
                amount,
                note
        );
        ledger.addTransaction(transaction);
        return transaction;
    }

    public boolean updateTransaction(
            Ledger ledger,
            String id,
            LocalDate date,
            TransactionType type,
            String category,
            double amount,
            String note
    ) {
        validateInput(ledger, date, type, category, amount);
        Optional<Transaction> optionalTransaction = findById(ledger, id);
        if (optionalTransaction.isEmpty()) {
            return false;
        }

        Transaction transaction = optionalTransaction.get();
        transaction.setDate(date);
        transaction.setType(type);
        transaction.setCategory(category);
        transaction.setAmount(amount);
        transaction.setNote(note);
        return true;
    }

    public boolean deleteTransaction(Ledger ledger, String id) {
        Objects.requireNonNull(ledger, "账本不能为空");
        if (id == null || id.isBlank()) {
            return false;
        }
        return ledger.removeTransactionById(id.trim());
    }

    public Optional<Transaction> findById(Ledger ledger, String id) {
        Objects.requireNonNull(ledger, "账本不能为空");
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        return ledger.findTransactionById(id.trim());
    }

    public List<Transaction> listTransactionsSorted(Ledger ledger) {
        Objects.requireNonNull(ledger, "账本不能为空");
        return ledger.getTransactions().stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed()
                        .thenComparing(Transaction::getId))
                .collect(Collectors.toList());
    }

    public void loadDemoData(Ledger ledger) {
        Objects.requireNonNull(ledger, "账本不能为空");
        if (!ledger.getTransactions().isEmpty()) {
            throw new IllegalStateException("当前账本已有数据，不能重复导入演示数据。");
        }

        addTransaction(ledger, LocalDate.now().minusDays(5), TransactionType.INCOME, "工资", 6500, "四月工资");
        addTransaction(ledger, LocalDate.now().minusDays(4), TransactionType.EXPENSE, "餐饮", 42.5, "午饭");
        addTransaction(ledger, LocalDate.now().minusDays(3), TransactionType.EXPENSE, "交通", 18, "地铁");
        addTransaction(ledger, LocalDate.now().minusDays(2), TransactionType.EXPENSE, "购物", 299, "日用品");
        addTransaction(ledger, LocalDate.now().minusDays(1), TransactionType.INCOME, "兼职", 300, "家教");
        addTransaction(ledger, LocalDate.now(), TransactionType.EXPENSE, "娱乐", 88, "电影票");
    }

    private void validateInput(
            Ledger ledger,
            LocalDate date,
            TransactionType type,
            String category,
            double amount
    ) {
        Objects.requireNonNull(ledger, "账本不能为空");
        Objects.requireNonNull(date, "日期不能为空");
        Objects.requireNonNull(type, "交易类型不能为空");
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("分类不能为空");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("金额必须大于 0");
        }
    }
}
