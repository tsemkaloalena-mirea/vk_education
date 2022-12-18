package com.tsemkalo.homework9.launchers;

import com.tsemkalo.homework9.verticles.Clan;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.io.IOException;
import java.net.URISyntaxException;

public final class ClanLauncher {
    public static void main(String[] args) throws URISyntaxException, IOException {
        run(JSONReader.readClansAmount("/clan_data.json"));
    }

    public static void run(int clansAmount) {
        Vertx.clusteredVertx(
                new VertxOptions(),
                vertxResult -> {
                    Vertx vertx = vertxResult.result();
                    if (vertx == null) {
                        System.out.println("Clan deploy was failed");
                        return;
                    }
                    Clan.Factory factory = new Clan.Factory();
                    vertx.registerVerticleFactory(factory);
                    DeploymentOptions options = new DeploymentOptions().setWorker(true).setInstances(clansAmount);
                    vertx.deployVerticle(
                            factory.prefix() + ":" + Clan.class.getSimpleName(),
                            options,
                            result -> System.out.println("Clan deploy result: " + result.succeeded())
                    );
                }
        );
    }
}
