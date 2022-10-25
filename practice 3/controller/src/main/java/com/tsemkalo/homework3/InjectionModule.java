package com.tsemkalo.homework3;

import com.google.inject.AbstractModule;

public final class InjectionModule extends AbstractModule {
    private final String filePath;

    public InjectionModule(String filePath) {
        this.filePath = filePath;
    }

    @Override
    protected void configure() {
        bind(LibraryFactory.class).toInstance(new FileLibraryFactory(filePath));
    }
}
