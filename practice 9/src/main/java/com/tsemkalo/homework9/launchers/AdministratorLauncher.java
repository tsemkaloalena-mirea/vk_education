package com.tsemkalo.homework9.launchers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsemkalo.homework9.info.ParticipantInfo;
import com.tsemkalo.homework9.verticles.Administrator;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class AdministratorLauncher {
    public static void main(String[] args) throws IOException, URISyntaxException {
        Path filePath = Paths.get(AdministratorLauncher.class.getResource("/application_data.json").toURI());
        try (Reader reader = Files.newBufferedReader(filePath)) {
            JsonObject tree = new Gson().fromJson(reader, JsonObject.class);
            JsonArray admins = tree.get("admins").getAsJsonArray();
            for (JsonElement element : admins) {
                JsonObject info = element.getAsJsonObject();

                JsonObject clanData = new JsonObject();
                clanData.addProperty("maxUsersNumber", info.get("maxUsersNumber").getAsInt());
                clanData.addProperty("maxModeratorsNumber", info.get("maxModeratorsNumber").getAsInt());
                ParticipantInfo participantInfo = new ParticipantInfo(info.get("name").getAsString());
                participantInfo.setClanId(info.get("clanId").getAsLong());
                run(participantInfo, clanData);
            }
        }
    }

    public static void run(ParticipantInfo info, JsonObject clanData) {
        Vertx.clusteredVertx(
                new VertxOptions(),
                vertxResult -> {
                    Vertx vertx = vertxResult.result();
                    Administrator.Factory<Administrator> factory = new Administrator.Factory<>(Administrator.class, info, clanData);
                    vertx.registerVerticleFactory(factory);
                    DeploymentOptions options = new DeploymentOptions().setWorker(true);
                    vertx.deployVerticle(
                            factory.prefix() + ":" + Administrator.class.getSimpleName(),
                            options,
                            result -> System.out.println("Administrator deploy result: " + result.succeeded())
                    );
                }
        );
    }
}
