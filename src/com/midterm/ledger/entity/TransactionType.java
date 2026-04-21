package com.midterm.ledger.entity;

public enum TransactionType {
    INCOME((short) 1, "收入"),
    EXPENSE((short) 2, "支出");

    private final short code;
    private final String label;

    TransactionType(short code, String label) {
        this.code = code;
        this.label = label;
    }

    public short getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static TransactionType fromCode(int code) {
        for (TransactionType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的交易类型，请输入 1 或 2。");
    }
}
