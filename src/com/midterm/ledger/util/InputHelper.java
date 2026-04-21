package com.midterm.ledger.util;

import com.midterm.ledger.entity.TransactionType;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Scanner;

public class InputHelper {
    private final Scanner scanner;

    public InputHelper(Scanner scanner) {
        this.scanner = scanner;
    }

    public int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                int value = Integer.parseInt(input.trim());
                if (value < min || value > max) {
                    System.out.printf("请输入 %d 到 %d 之间的数字。%n", min, max);
                    continue;
                }
                return value;
            } catch (NumberFormatException exception) {
                System.out.println("输入无效，请输入数字。");
            }
        }
    }

    public double readPositiveDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                double value = Double.parseDouble(input.trim());
                if (value <= 0) {
                    System.out.println("金额必须大于 0。");
                    continue;
                }
                return value;
            } catch (NumberFormatException exception) {
                System.out.println("输入无效，请输入正确的金额。");
            }
        }
    }

    public String readRequiredString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input != null && !input.isBlank()) {
                return input.trim();
            }
            System.out.println("输入不能为空。");
        }
    }

    public String readOptionalString(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        return input == null ? "" : input.trim();
    }

    public LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return DateUtil.parseDate(input);
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    public LocalDate readOptionalDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input == null || input.isBlank()) {
                return null;
            }
            try {
                return DateUtil.parseDate(input);
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    public YearMonth readMonth(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return DateUtil.parseMonth(input);
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    public TransactionType readType(String prompt) {
        System.out.println("1. 收入");
        System.out.println("2. 支出");
        int code = readInt(prompt, 1, 2);
        return TransactionType.fromCode(code);
    }

    public TransactionType readOptionalType(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input == null || input.isBlank()) {
                return null;
            }
            try {
                return TransactionType.fromCode(Integer.parseInt(input.trim()));
            } catch (NumberFormatException exception) {
                System.out.println("输入无效，请输入 1、2 或直接回车。");
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    public boolean confirm(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if ("y".equalsIgnoreCase(input) || "yes".equalsIgnoreCase(input)) {
                return true;
            }
            if ("n".equalsIgnoreCase(input) || "no".equalsIgnoreCase(input)) {
                return false;
            }
            System.out.println("请输入 y 或 n。");
        }
    }
}
