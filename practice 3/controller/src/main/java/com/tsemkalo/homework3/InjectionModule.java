package com.tsemkalo.homework3;

import com.google.inject.AbstractModule;

public final class InjectionModule extends AbstractModule {
    private final String filePath;

    public InjectionModule(String filePath) {
        this.filePath = filePath;
    }

    @Override
    protected void configure() {
        bind(BookFactory.class).toInstance(new FileBookFactory(filePath));
        bind(LibraryFactory.class).to(LibraryFactoryImpl.class);
    }
}
