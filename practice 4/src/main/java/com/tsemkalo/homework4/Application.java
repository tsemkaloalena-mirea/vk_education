package com.tsemkalo.homework4;

import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Application {
    public static void main(String[] args) {
        if (args.length == 0) {
            log.debug("Name for property file is not set");
            return;
        }
        Injector injector = Guice.createInjector(new BasicModule(args[0]));
        DBInitializer dbInitializer = injector.getInstance(DBInitializer.class);
        dbInitializer.runMigration();
    }
}
