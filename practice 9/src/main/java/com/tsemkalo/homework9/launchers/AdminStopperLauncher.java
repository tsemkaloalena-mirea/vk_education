package com.tsemkalo.homework9.launchers;

import com.tsemkalo.homework9.verticles.AdminStopper;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

// При запуске этого лаунчера админ выйдет из сети
public class AdminStopperLauncher {
    public static void main(String[] args) {
        Vertx.clusteredVertx(
                new VertxOptions(),
                vertxResult -> {
                    final var options = new DeploymentOptions().setWorker(true);
                    vertxResult.result().deployVerticle(new AdminStopper(1L), options);
                }
        );
    }
}
