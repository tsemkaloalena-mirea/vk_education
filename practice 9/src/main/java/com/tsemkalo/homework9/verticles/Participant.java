package com.tsemkalo.homework9.verticles;

import com.google.gson.JsonObject;
import com.tsemkalo.homework9.info.ParticipantInfo;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.impl.JavaVerticleFactory;
import lombok.Getter;

import java.util.concurrent.Callable;

import static com.tsemkalo.homework9.verticles.Names.PARTICIPANTS_MAP;

abstract public class Participant extends AbstractVerticle {
    @Getter
    private ParticipantInfo participantInfo;

    public Participant(ParticipantInfo participantInfo) {
        this.participantInfo = participantInfo;
    }

    @Override
    public void start() {
        vertx.sharedData().getCounter("participantNumber", counter -> {
            if (counter.succeeded()) {
                counter.result().incrementAndGet(number -> {
                    getParticipantInfo().setId(number.result());
                    System.out.println(getClass().getSimpleName() + " " + getParticipantInfo().getName() + " is added and has id " + getParticipantInfo().getId());
                    subscribe();
                });
            }
        });
    }

    abstract public void subscribe();

    public void putParticipantToMap() {
        vertx.sharedData().<Long, ParticipantInfo>getAsyncMap(PARTICIPANTS_MAP, map ->
                map.result().put(getParticipantInfo().getId(), getParticipantInfo())
        );
    }
}
