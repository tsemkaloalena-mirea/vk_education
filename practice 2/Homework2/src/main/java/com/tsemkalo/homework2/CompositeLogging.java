package com.tsemkalo.homework2;

import lombok.extern.slf4j.Slf4j;

import java.util.NoSuchElementException;
import java.util.Scanner;

@Slf4j
public final class CompositeLogging extends Logging {
    public CompositeLogging(String tagName) {
        super(tagName);
    }

    @Override
    public void waitForInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Waiting for new lines. Key in Ctrl+D to exit.");
            while (true) {
                String text = scanner.nextLine();
                log.info("{}. <{}>{}<{}/>", logNumber, tagName, text, tagName);
                logNumber++;
                log.debug("{}. <{}>{}<{}/>", logNumber, tagName, text, tagName);
                logNumber++;
            }
        } catch (IllegalStateException | NoSuchElementException e) {
            e.printStackTrace();
        }
    }
}