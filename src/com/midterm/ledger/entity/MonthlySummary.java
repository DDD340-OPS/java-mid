package com.midterm.ledger.entity;

import java.time.YearMonth;

public class MonthlySummary extends StatisticsSummary {
    private final YearMonth month;
    private final int recordCount;

    public MonthlySummary(YearMonth month, double totalIncome, double totalExpense, int recordCount) {
        super(totalIncome, totalExpense);
        this.month = month;
        this.recordCount = recordCount;
    }

    public YearMonth getMonth() {
        return month;
    }

    public int getRecordCount() {
        return recordCount;
    }
}
