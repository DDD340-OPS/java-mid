package com.midterm.ledger.entity;

public class StatisticsSummary {
    private final double totalIncome;
    private final double totalExpense;
    private final double balance;

    public StatisticsSummary(double totalIncome, double totalExpense) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.balance = totalIncome - totalExpense;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public double getBalance() {
        return balance;
    }
}
