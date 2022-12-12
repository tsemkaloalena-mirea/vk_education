package com.tsemkalo.homework9.launchers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsemkalo.homework9.info.ParticipantInfo;
import com.tsemkalo.homework9.verticles.Clan;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ClanLauncher {
    public static void main(String[] args) throws URISyntaxException, IOException {
        Path filePath = Paths.get(ClanLauncher.class.getResource("/application_data.json").toURI());
        try (Reader reader = Files.newBufferedReader(filePath)) {
            JsonObject tree = new Gson().fromJson(reader, JsonObject.class);
            int clansAmount = tree.get("clansAmount").getAsInt();
            run(clansAmount);
        }
    }
// TODO общий лаунчер
    public static void run(int n) {
        Vertx.clusteredVertx(
                new VertxOptions(),
                vertxResult -> {
                    Vertx vertx = vertxResult.result();
                    Clan.Factory factory = new Clan.Factory();
                    vertx.registerVerticleFactory(factory);
                    DeploymentOptions options = new DeploymentOptions().setWorker(true).setInstances(n);
                    vertx.deployVerticle(
                            factory.prefix() + ":" + Clan.class.getSimpleName(),
                            options,
                            result -> System.out.println("Clan deploy result: " + result.succeeded())
                    );
                }
        );
    }
}
