package com.tsemkalo.homework2;

import com.google.inject.AbstractModule;

public final class InjectionModule extends AbstractModule {
    private final String[] args;

    public InjectionModule(String[] args) {
        this.args = args;
    }

    @Override
    protected void configure() {
        if (args.length < 2) {
            System.out.println("2 arguments are expected");
            return;
        }
        switch (args[0]) {
            case "console" -> bind(Logging.class).toInstance(new ConsoleLogging(args[1]));
            case "file" -> bind(Logging.class).toInstance(new FileLogging(args[1]));
            case "composite" -> bind(Logging.class).toInstance(new CompositeLogging(args[1]));
            default -> {System.out.println("Type of logging is not set");}
        }
    }
}
