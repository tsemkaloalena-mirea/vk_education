package com.tsemkalo.homework9.launchers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsemkalo.homework9.info.ParticipantInfo;
import com.tsemkalo.homework9.info.UserDto;
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
import java.util.List;


public final class UserLauncher {
    public static void main(String[] args) throws IOException, URISyntaxException {
        List<UserDto> users = JSONReader.<UserDto>readParticipants("/user_data.json", UserDto[].class);
        if (users == null) {
            System.out.println("Can not read users from file");
            return;
        }
        for (UserDto userDto : users) {
            if (userDto.getParticipantInfo().getName() == null || userDto.getMessageDelay() == null) {
                System.out.println("User doesn't have enough data");
            } else {
                run(userDto);
            }
        }
    }

    public static void run(UserDto userDto) {
        Vertx.clusteredVertx(
                new VertxOptions(),
                vertxResult -> {
                    Vertx vertx = vertxResult.result();
                    if (vertx == null) {
                        System.out.println("User deploy was failed");
                        return;
                    }
                    User.Factory factory = new User.Factory(userDto.getParticipantInfo(), userDto.getMessageDelay());
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
