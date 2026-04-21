package com.midterm.ledger.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public final class IdGenerator {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final AtomicInteger COUNTER = new AtomicInteger(1);

    private IdGenerator() {
    }

    public static String nextTransactionId() {
        int sequence = COUNTER.getAndIncrement();
        return "TX" + LocalDateTime.now().format(FORMATTER) + String.format("%03d", sequence);
    }
}
