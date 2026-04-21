package com.midterm.ledger.service;

import com.midterm.ledger.entity.Ledger;
import com.midterm.ledger.entity.Transaction;
import com.midterm.ledger.entity.TransactionType;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilterService {
    public List<Transaction> filter(
            Ledger ledger,
            LocalDate startDate,
            LocalDate endDate,
            String category,
            TransactionType type
    ) {
        Objects.requireNonNull(ledger, "账本不能为空");

        Stream<Transaction> stream = ledger.getTransactions().stream();

        if (startDate != null) {
            stream = stream.filter(transaction -> !transaction.getDate().isBefore(startDate));
        }
        if (endDate != null) {
            stream = stream.filter(transaction -> !transaction.getDate().isAfter(endDate));
        }
        if (category != null && !category.isBlank()) {
            String normalizedCategory = category.trim();
            stream = stream.filter(transaction -> transaction.getCategory().equalsIgnoreCase(normalizedCategory));
        }
        if (type != null) {
            stream = stream.filter(transaction -> transaction.getType() == type);
        }

        return stream.sorted(Comparator.comparing(Transaction::getDate).reversed()
                        .thenComparing(Transaction::getId))
                .collect(Collectors.toList());
    }

    public List<Transaction> filterByMonth(Ledger ledger, YearMonth month) {
        Objects.requireNonNull(month, "月份不能为空");
        return filter(
                ledger,
                month.atDay(1),
                month.atEndOfMonth(),
                null,
                null
        );
    }
}
