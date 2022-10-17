package com.tsemkalo.homework2;

import com.google.inject.AbstractModule;

public final class InjectionModule extends AbstractModule {
    private final String[] args;

    public InjectionModule(String[] args) {
        this.args = args;
    }

    @Override
    protected void configure() {
        switch (args[0]) {
            case "console":
                bind(Logging.class).toInstance(new ConsoleLogging(args[1]));
                break;
            case "file":
                bind(Logging.class).toInstance(new FileLogging(args[1]));
                break;
            case "composite":
                bind(Logging.class).toInstance(new CompositeLogging(args[1]));
                break;
        }
    }
}
