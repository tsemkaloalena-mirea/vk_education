package com.tsemkalo.homework2;

import lombok.extern.slf4j.Slf4j;

import java.util.NoSuchElementException;
import java.util.Scanner;

@Slf4j
public final class FileLogging extends Logging {
    public FileLogging(String tagName) {
        super(tagName);
    }

    @Override
    public void waitForInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Waiting for new lines. Key in Ctrl+D to exit.");
            while (true) {
                log.debug("{}. <{}>{}<{}/>", logNumber, tagName, scanner.nextLine(), tagName);
                logNumber++;
            }
        } catch (IllegalStateException | NoSuchElementException e) {
            e.printStackTrace();
        }
    }
}