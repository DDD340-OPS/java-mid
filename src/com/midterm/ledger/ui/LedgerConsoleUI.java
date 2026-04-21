package com.midterm.ledger.ui;

import com.midterm.ledger.entity.Ledger;
import com.midterm.ledger.entity.MonthlySummary;
import com.midterm.ledger.entity.StatisticsSummary;
import com.midterm.ledger.entity.Transaction;
import com.midterm.ledger.entity.TransactionType;
import com.midterm.ledger.service.FilterService;
import com.midterm.ledger.service.LedgerService;
import com.midterm.ledger.service.StatisticsService;
import com.midterm.ledger.util.DateUtil;
import com.midterm.ledger.util.InputHelper;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class LedgerConsoleUI {
    private final Scanner scanner = new Scanner(System.in);
    private final InputHelper inputHelper = new InputHelper(scanner);
    private final LedgerService ledgerService = new LedgerService();
    private final StatisticsService statisticsService = new StatisticsService();
    private final FilterService filterService = new FilterService();

    private Ledger ledger;

    public void run() {
        ledger = ledgerService.createLedger("我的账本");
        System.out.println("欢迎使用个人记账工具。");
        System.out.println("当前账本：" + ledger.getName());

        boolean running = true;
        while (running) {
            printMenu();
            int choice = inputHelper.readInt("请选择功能：", 0, 10);
            try {
                switch (choice) {
                    case 1:
                        createLedger();
                        break;
                    case 2:
                        addTransaction();
                        break;
                    case 3:
                        viewTransactions();
                        break;
                    case 4:
                        updateTransaction();
                        break;
                    case 5:
                        deleteTransaction();
                        break;
                    case 6:
                        showStatisticsReport();
                        break;
                    case 7:
                        showCategoryStatistics();
                        break;
                    case 8:
                        showMonthlyStatistics();
                        break;
                    case 9:
                        filterTransactions();
                        break;
                    case 10:
                        loadDemoData();
                        break;
                    case 0:
                        running = false;
                        break;
                    default:
                        break;
                }
            } catch (IllegalArgumentException | IllegalStateException exception) {
                System.out.println("操作失败：" + exception.getMessage());
            }
            System.out.println();
        }

        System.out.println("程序已退出，感谢使用。");
        scanner.close();
    }

    private void printMenu() {
        System.out.println("==== 个人记账工具 ====");
        System.out.println("当前账本：" + ledger.getName() + "，记录数：" + ledger.getTransactions().size());
        System.out.println("1. 创建新账本");
        System.out.println("2. 添加记录");
        System.out.println("3. 查看所有记录");
        System.out.println("4. 修改记录");
        System.out.println("5. 删除记录");
        System.out.println("6. 查看统计报表");
        System.out.println("7. 查看分类统计");
        System.out.println("8. 查看月度统计");
        System.out.println("9. 数据筛选");
        System.out.println("10. 加载演示数据");
        System.out.println("0. 退出系统");
    }

    private void createLedger() {
        if (!ledger.getTransactions().isEmpty()) {
            boolean confirmed = inputHelper.confirm("当前账本已有数据，创建新账本会清空当前内存数据，是否继续？(y/n)：");
            if (!confirmed) {
                System.out.println("已取消创建新账本。");
                return;
            }
        }

        String name = inputHelper.readRequiredString("请输入新账本名称：");
        ledger = ledgerService.createLedger(name);
        System.out.println("新账本创建成功：" + ledger.getName());
    }

    private void addTransaction() {
        LocalDate date = inputHelper.readDate("请输入日期（yyyy-MM-dd）：");
        TransactionType type = inputHelper.readType("请选择类型：");
        String category = inputHelper.readRequiredString("请输入分类：");
        double amount = inputHelper.readPositiveDouble("请输入金额：");
        String note = inputHelper.readOptionalString("请输入备注（可留空）：");

        Transaction transaction = ledgerService.addTransaction(ledger, date, type, category, amount, note);
        System.out.println("添加成功，记录 ID：" + transaction.getId());
    }

    private void viewTransactions() {
        List<Transaction> transactions = ledgerService.listTransactionsSorted(ledger);
        printTransactions(transactions);
    }

    private void updateTransaction() {
        if (ledger.getTransactions().isEmpty()) {
            System.out.println("当前没有可修改的记录。");
            return;
        }

        viewTransactions();
        String id = inputHelper.readRequiredString("请输入要修改的记录 ID：");
        Optional<Transaction> optionalTransaction = ledgerService.findById(ledger, id);
        if (optionalTransaction.isEmpty()) {
            System.out.println("未找到对应记录。");
            return;
        }

        System.out.println("请输入新的记录内容：");
        LocalDate date = inputHelper.readDate("请输入日期（yyyy-MM-dd）：");
        TransactionType type = inputHelper.readType("请选择类型：");
        String category = inputHelper.readRequiredString("请输入分类：");
        double amount = inputHelper.readPositiveDouble("请输入金额：");
        String note = inputHelper.readOptionalString("请输入备注（可留空）：");

        boolean updated = ledgerService.updateTransaction(ledger, id, date, type, category, amount, note);
        System.out.println(updated ? "修改成功。" : "修改失败。");
    }

    private void deleteTransaction() {
        if (ledger.getTransactions().isEmpty()) {
            System.out.println("当前没有可删除的记录。");
            return;
        }

        viewTransactions();
        String id = inputHelper.readRequiredString("请输入要删除的记录 ID：");
        boolean confirmed = inputHelper.confirm("确认删除该记录吗？(y/n)：");
        if (!confirmed) {
            System.out.println("已取消删除。");
            return;
        }

        boolean deleted = ledgerService.deleteTransaction(ledger, id);
        System.out.println(deleted ? "删除成功。" : "未找到对应记录。");
    }

    private void showStatisticsReport() {
        if (ledger.getTransactions().isEmpty()) {
            System.out.println("当前没有记录，无法生成统计报表。");
            return;
        }
        System.out.println(statisticsService.buildFullReport(ledger));
    }

    private void showCategoryStatistics() {
        if (ledger.getTransactions().isEmpty()) {
            System.out.println("当前没有记录，无法查看分类统计。");
            return;
        }

        StatisticsSummary summary = statisticsService.calculateSummary(ledger);
        Map<String, Double> categoryStats = statisticsService.calculateExpenseByCategory(ledger);
        if (categoryStats.isEmpty()) {
            System.out.println("当前没有支出记录。");
            return;
        }

        System.out.println("==== 分类支出统计 ====");
        categoryStats.forEach((category, amount) -> {
            double ratio = amount / summary.getTotalExpense() * 100;
            System.out.printf("%s：%.2f，占比 %.2f%%%n", category, amount, ratio);
        });
    }

    private void showMonthlyStatistics() {
        if (ledger.getTransactions().isEmpty()) {
            System.out.println("当前没有记录，无法查看月度统计。");
            return;
        }

        YearMonth month = inputHelper.readMonth("请输入要查询的月份（yyyy-MM）：");
        MonthlySummary monthlySummary = statisticsService.calculateMonthlySummary(ledger, month);
        List<Transaction> monthlyTransactions = filterService.filterByMonth(ledger, month);

        System.out.println("==== 月度统计 ====");
        System.out.println("月份：" + DateUtil.formatMonth(monthlySummary.getMonth()));
        System.out.printf("记录数：%d%n", monthlySummary.getRecordCount());
        System.out.printf("总收入：%.2f%n", monthlySummary.getTotalIncome());
        System.out.printf("总支出：%.2f%n", monthlySummary.getTotalExpense());
        System.out.printf("结余：%.2f%n", monthlySummary.getBalance());
        printTransactions(monthlyTransactions);
    }

    private void filterTransactions() {
        if (ledger.getTransactions().isEmpty()) {
            System.out.println("当前没有记录，无法筛选。");
            return;
        }

        System.out.println("直接回车可跳过该筛选条件。");
        LocalDate startDate = inputHelper.readOptionalDate("开始日期（yyyy-MM-dd）：");
        LocalDate endDate = inputHelper.readOptionalDate("结束日期（yyyy-MM-dd）：");
        String category = inputHelper.readOptionalString("分类：");
        TransactionType type = inputHelper.readOptionalType("类型（1-收入，2-支出，回车跳过）：");

        List<Transaction> transactions = filterService.filter(ledger, startDate, endDate, category, type);
        System.out.println("筛选结果：" + transactions.size() + " 条记录");
        printTransactions(transactions);
    }

    private void loadDemoData() {
        if (!ledger.getTransactions().isEmpty()) {
            System.out.println("当前账本已有记录。为避免重复，演示数据只能加载到空账本。");
            return;
        }
        ledgerService.loadDemoData(ledger);
        System.out.println("演示数据加载完成。");
    }

    private void printTransactions(List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            System.out.println("暂无记录。");
            return;
        }

        System.out.println("==== 交易记录 ====");
        System.out.printf("%-22s %-12s %-8s %-10s %-10s %-20s%n", "ID", "日期", "类型", "分类", "金额", "备注");
        for (Transaction transaction : transactions) {
            System.out.printf(
                    "%-22s %-12s %-8s %-10s %-10.2f %-20s%n",
                    transaction.getId(),
                    DateUtil.formatDate(transaction.getDate()),
                    transaction.getType().getLabel(),
                    transaction.getCategory(),
                    transaction.getAmount(),
                    transaction.getNote()
            );
        }
    }
}
