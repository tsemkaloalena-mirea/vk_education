package com.tsemkalo.homework2;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.jetbrains.annotations.NotNull;

public class Main {
    public static void main(@NotNull String[] args) {
        final Injector injector = Guice.createInjector(new InjectionModule(args));
        injector.getInstance(Logging.class).waitForInput();
    }
}
