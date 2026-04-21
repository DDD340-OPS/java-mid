package com.midterm.ledger.entity;

import com.midterm.ledger.util.DateUtil;

import java.time.LocalDate;
import java.util.Objects;

public class Transaction {
    private final String id;
    private LocalDate date;
    private TransactionType type;
    private String category;
    private double amount;
    private String note;

    public Transaction(String id, LocalDate date, TransactionType type, String category, double amount, String note) {
        this.id = requireText(id, "记录 ID 不能为空");
        this.date = Objects.requireNonNull(date, "日期不能为空");
        this.type = Objects.requireNonNull(type, "交易类型不能为空");
        this.category = requireText(category, "分类不能为空");
        setAmount(amount);
        this.note = note == null || note.isBlank() ? "无" : note.trim();
    }

    public String getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = Objects.requireNonNull(date, "日期不能为空");
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = Objects.requireNonNull(type, "交易类型不能为空");
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = requireText(category, "分类不能为空");
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("金额必须大于 0");
        }
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null || note.isBlank() ? "无" : note.trim();
    }

    @Override
    public String toString() {
        return String.format(
                "Transaction{id='%s', date=%s, type=%s, category='%s', amount=%.2f, note='%s'}",
                id,
                DateUtil.formatDate(date),
                type.getLabel(),
                category,
                amount,
                note
        );
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
