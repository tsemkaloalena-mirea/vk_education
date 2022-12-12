package com.tsemkalo.homework9.launchers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsemkalo.homework9.info.ParticipantInfo;
import com.tsemkalo.homework9.verticles.Moderator;
import com.tsemkalo.homework9.verticles.Participant;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ModeratorLauncher {
    public static void main(String[] args) throws URISyntaxException, IOException {
        Path filePath = Paths.get(ModeratorLauncher.class.getResource("/application_data.json").toURI());
        try (Reader reader = Files.newBufferedReader(filePath)) {
            JsonObject tree = new Gson().fromJson(reader, JsonObject.class);
            JsonArray moderators = tree.get("moderators").getAsJsonArray();
            for (JsonElement element : moderators) {
                JsonObject info = element.getAsJsonObject();

                ParticipantInfo participantInfo = new ParticipantInfo(info.get("name").getAsString());
                participantInfo.setClanId(info.get("clanId").getAsLong());
                run(participantInfo);
            }
        }
    }

    public static void run(ParticipantInfo info) {
        Vertx.clusteredVertx(
                new VertxOptions(),
                vertxResult -> {
                    Vertx vertx = vertxResult.result();
                    Moderator.Factory<Moderator> factory = new Moderator.Factory<>(Moderator.class, info);
                    vertx.registerVerticleFactory(factory);
                    DeploymentOptions options = new DeploymentOptions().setWorker(true);
                    vertx.deployVerticle(
                            factory.prefix() + ":" + Moderator.class.getSimpleName(),
                            options,
                            result -> System.out.println("Moderator deploy result: " + result.succeeded())
                    );
                }
        );
    }
}
