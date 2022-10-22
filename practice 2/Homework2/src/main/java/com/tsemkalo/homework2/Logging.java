package com.tsemkalo.homework2;

import lombok.Getter;
import lombok.Setter;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Logging {
    private final String tagName;
    @Setter
    @Getter
    private Logger logger;

    public Logging(String tagName) {
        this.tagName = tagName;
        logger = Logger.getLogger(this.getClass().getName());
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
        configureLogs();
    }

    protected abstract void configureLogs();

    public void waitForInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Waiting for new lines. Key in Ctrl+D to exit.");
            while (true) {
                logger.info(String.format("<%s>%s<%s/>", tagName, scanner.nextLine(), tagName));
            }
        } catch (IllegalStateException | NoSuchElementException e) {
            e.printStackTrace();
        }
    }
}
