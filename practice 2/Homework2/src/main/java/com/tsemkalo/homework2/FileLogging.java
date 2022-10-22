package com.tsemkalo.homework2;

import java.io.IOException;
import java.util.logging.FileHandler;

public final class FileLogging extends Logging {
    public FileLogging(String tagName) {
        super(tagName);
    }

    @Override
    protected void configureLogs() {
        try {
            FileHandler fileHandler = new FileHandler("logfile.log");
            fileHandler.setFormatter(new MyFormatter());
            getLogger().addHandler(fileHandler);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}