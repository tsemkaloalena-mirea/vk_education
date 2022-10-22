package com.tsemkalo.homework2;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public final class MyFormatter extends Formatter {
    private static int logNumber = 1;

    @Override
    public String format(LogRecord record) {
        return String.format("%d. %s %n", logNumber++, record.getMessage());
    }
}
