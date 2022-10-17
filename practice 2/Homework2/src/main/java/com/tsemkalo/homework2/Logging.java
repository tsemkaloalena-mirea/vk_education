package com.tsemkalo.homework2;

public abstract class Logging {
    int logNumber = 1;
    String tagName;

    public Logging(String tagName) {
        this.tagName = tagName;
    }

    protected abstract void waitForInput();
}
