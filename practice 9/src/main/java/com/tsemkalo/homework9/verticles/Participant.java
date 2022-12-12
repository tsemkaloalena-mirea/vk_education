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
                    subscribe();
                });
            }
        });
    }

    abstract public void subscribe();

    public void addParticipantToMap() {
        vertx.sharedData().<Long, ParticipantInfo>getAsyncMap(PARTICIPANTS_MAP, map ->
                map.result().put(getParticipantInfo().getId(), getParticipantInfo(),
                        adminCompletion -> System.out.println(getClass().getSimpleName() + " " + getParticipantInfo().getName() + " is added and has id " + getParticipantInfo().getId())
                )
        );
    }

    public static final class Factory<T extends Verticle> extends JavaVerticleFactory {
        private static Long number = 0L;
        private final Class<T> className;
        private final ParticipantInfo participantInfo;
        private JsonObject additionalInfo;

        public Factory(Class<T> className, ParticipantInfo participantInfo) {
            this.className = className;
            this.participantInfo = participantInfo;
            participantInfo.setId(++number);
            this.additionalInfo = null;
        }

        public Factory(Class<T> className, ParticipantInfo participantInfo, JsonObject clanData) {
            this(className, participantInfo);
            this.additionalInfo = clanData;
        }

        @Override
        public String prefix() {
            return "clan_game";
        }

        @Override
        public void createVerticle(String verticleName,
                                   ClassLoader classLoader,
                                   Promise<Callable<Verticle>> promise) {
            if (additionalInfo == null) {
                promise.complete(() -> className.getConstructor(ParticipantInfo.class).newInstance(participantInfo));
            } else {
                promise.complete(() -> className.getConstructor(ParticipantInfo.class, JsonObject.class).newInstance(participantInfo, additionalInfo));
            }
        }
    }
}
