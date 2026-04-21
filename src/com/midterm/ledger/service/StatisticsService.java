package com.midterm.ledger.service;

import com.midterm.ledger.entity.Ledger;
import com.midterm.ledger.entity.MonthlySummary;
import com.midterm.ledger.entity.StatisticsSummary;
import com.midterm.ledger.entity.Transaction;
import com.midterm.ledger.entity.TransactionType;
import com.midterm.ledger.util.DateUtil;

import java.time.YearMonth;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class StatisticsService {
    public StatisticsSummary calculateSummary(Ledger ledger) {
        Objects.requireNonNull(ledger, "账本不能为空");
        return calculateSummary(ledger.getTransactions());
    }

    public StatisticsSummary calculateSummary(List<Transaction> transactions) {
        double totalIncome = transactions.stream()
                .filter(transaction -> transaction.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalExpense = transactions.stream()
                .filter(transaction -> transaction.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();

        return new StatisticsSummary(totalIncome, totalExpense);
    }

    public Map<String, Double> calculateExpenseByCategory(Ledger ledger) {
        Objects.requireNonNull(ledger, "账本不能为空");
        Map<String, Double> categoryTotals = ledger.getTransactions().stream()
                .filter(transaction -> transaction.getType() == TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        if (categoryTotals.isEmpty()) {
            return Collections.emptyMap();
        }

        return categoryTotals.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (left, right) -> left,
                        LinkedHashMap::new
                ));
    }

    public MonthlySummary calculateMonthlySummary(Ledger ledger, YearMonth month) {
        Objects.requireNonNull(ledger, "账本不能为空");
        Objects.requireNonNull(month, "月份不能为空");

        List<Transaction> monthlyTransactions = ledger.getTransactions().stream()
                .filter(transaction -> YearMonth.from(transaction.getDate()).equals(month))
                .collect(Collectors.toList());

        StatisticsSummary summary = calculateSummary(monthlyTransactions);
        return new MonthlySummary(
                month,
                summary.getTotalIncome(),
                summary.getTotalExpense(),
                monthlyTransactions.size()
        );
    }

    public String buildFullReport(Ledger ledger) {
        StatisticsSummary summary = calculateSummary(ledger);
        Map<String, Double> categoryStats = calculateExpenseByCategory(ledger);
        double totalExpense = summary.getTotalExpense();

        StringBuilder builder = new StringBuilder();
        builder.append("==== 统计报表 ====\n");
        builder.append("账本名称：").append(ledger.getName()).append('\n');
        builder.append(String.format("总收入：%.2f%n", summary.getTotalIncome()));
        builder.append(String.format("总支出：%.2f%n", summary.getTotalExpense()));
        builder.append(String.format("结余：%.2f%n", summary.getBalance()));
        builder.append('\n');
        builder.append("支出分类统计：\n");

        if (categoryStats.isEmpty()) {
            builder.append("暂无支出记录。\n");
            return builder.toString();
        }

        categoryStats.forEach((category, amount) -> {
            double ratio = totalExpense == 0 ? 0 : amount / totalExpense * 100;
            builder.append(String.format("%s：%.2f (%.2f%%)%n", category, amount, ratio));
        });

        builder.append('\n');
        builder.append("报表生成时间：").append(DateUtil.nowText()).append('\n');
        return builder.toString();
    }
}
