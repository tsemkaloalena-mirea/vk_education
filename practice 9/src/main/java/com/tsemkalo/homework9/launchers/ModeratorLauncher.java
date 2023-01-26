package com.tsemkalo.homework9.launchers;

import com.tsemkalo.homework9.info.ParticipantInfo;
import com.tsemkalo.homework9.verticles.Moderator;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public final class ModeratorLauncher {
    public static void main(String[] args) throws URISyntaxException, IOException {
        List<ParticipantInfo> moderators = JSONReader.<ParticipantInfo>readParticipants("/moderator_data.json", ParticipantInfo[].class);
        if (moderators == null) {
            System.out.println("Can not read moderators from file");
            return;
        }
        for (ParticipantInfo participantInfo : moderators) {
            if (participantInfo.getName() == null || participantInfo.getClanId() == null) {
                System.out.println("Moderator doesn't have enough data");
            } else {
                run(participantInfo);
            }
        }
    }

    public static void run(ParticipantInfo info) {
        Vertx.clusteredVertx(
                new VertxOptions(),
                vertxResult -> {
                    Vertx vertx = vertxResult.result();
                    if (vertx == null) {
                        System.out.println("Moderator deploy was failed");
                        return;
                    }
                    Moderator.Factory factory = new Moderator.Factory(info);
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
