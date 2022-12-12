package com.tsemkalo.homework9.launchers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsemkalo.homework9.info.ParticipantInfo;
import com.tsemkalo.homework9.verticles.User;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public final class UserLauncher {
    public static void main(String[] args) throws IOException, URISyntaxException {
        Path filePath = Paths.get(UserLauncher.class.getResource("/application_data.json").toURI());
        try (Reader reader = Files.newBufferedReader(filePath)) {
            JsonObject tree = new Gson().fromJson(reader, JsonObject.class);
            JsonArray users = tree.get("users").getAsJsonArray();
            for (JsonElement element : users) {
                JsonObject info = element.getAsJsonObject();

                JsonObject data = new JsonObject();
                data.addProperty("messageDelay", info.get("messageDelay").getAsLong());

                ParticipantInfo participantInfo = new ParticipantInfo(info.get("name").getAsString());
                run(participantInfo, data);
            }
        }
    }

    public static void run(ParticipantInfo info, JsonObject data) {
        Vertx.clusteredVertx(
                new VertxOptions(),
                vertxResult -> {
                    Vertx vertx = vertxResult.result();
                    User.Factory<User> factory = new User.Factory<>(User.class, info, data);
                    vertx.registerVerticleFactory(factory);
                    DeploymentOptions options = new DeploymentOptions().setWorker(true);
                    vertx.deployVerticle(
                            factory.prefix() + ":" + User.class.getSimpleName(),
                            options,
                            result -> System.out.println("User deploy result: " + result.succeeded())
                    );
                }
        );
    }
}
