package com.tsemkalo.homework4;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;

@Slf4j
public final class BasicModule extends AbstractModule {
    private final String confFileName;
    public BasicModule(String confFileName) {
        this.confFileName = confFileName;
    }

    @Override
    protected void configure() {
        registerConfigFile();
    }

    private void registerConfigFile() {
        try {
            Properties properties = new Properties();
            Path filePath = Paths.get(this.getClass().getResource(confFileName).toURI());
            FileInputStream fileInputStream = new FileInputStream(filePath.toString());
            properties.load(fileInputStream);
            Names.bindProperties(binder(), properties);
            fileInputStream.close();
        } catch (IOException | URISyntaxException exception) {
            log.debug(Arrays.toString(exception.getStackTrace()));
        }
    }
}
