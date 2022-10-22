package com.tsemkalo.homework2;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;

public final class CompositeLogging extends Logging {
    public CompositeLogging(String tagName) {
        super(tagName);
    }

    @Override
    protected void configureLogs() {
        try {
            FileHandler fileHandler = new FileHandler("logfile.log");
            fileHandler.setFormatter(new MyFormatter());

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new MyFormatter());

            getLogger().addHandler(consoleHandler);
            getLogger().addHandler(fileHandler);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}