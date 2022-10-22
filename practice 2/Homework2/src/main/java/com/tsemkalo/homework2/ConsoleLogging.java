package com.tsemkalo.homework2;

import java.util.logging.ConsoleHandler;

public final class ConsoleLogging extends Logging {
    public ConsoleLogging(String tagName) {
        super(tagName);
    }

    @Override
    public void configureLogs() {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new MyFormatter());
        getLogger().addHandler(consoleHandler);
    }
}
