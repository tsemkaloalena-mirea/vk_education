package com.tsemkalo.homework9.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

import static com.tsemkalo.homework9.verticles.Names.TURN_OFF_ADMIN;

public class AdminStopper extends AbstractVerticle {
    private final long adminId;

    public AdminStopper(long adminId) {
        this.adminId = adminId;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        vertx.eventBus().send(TURN_OFF_ADMIN + this.adminId, null);
        startPromise.complete();
    }
}
