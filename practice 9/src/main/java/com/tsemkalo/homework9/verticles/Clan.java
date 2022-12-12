package com.tsemkalo.homework9.verticles;

import com.tsemkalo.homework9.info.ClanInfo;
import com.tsemkalo.homework9.info.ParticipantInfo;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.impl.JavaVerticleFactory;

import java.io.Serializable;
import java.util.concurrent.Callable;

import static com.tsemkalo.homework9.verticles.Names.CLAN_CREATED;
import static com.tsemkalo.homework9.verticles.Names.CLAN_MAP;
import static com.tsemkalo.homework9.verticles.Names.SET_ADMIN;

public final class Clan extends AbstractVerticle implements Serializable {
    private final long id;
    private ClanInfo info;

    public Clan(long id) {
        this.id = id;
        this.info = new ClanInfo(id);
    }

    @Override
    public void start(Promise<Void> startPromise) {
        lookForAdmin().completionHandler(result -> {
            addToClans();
            vertx.eventBus().publish(CLAN_CREATED + this.id, null);
            if (result.succeeded()) {
                startPromise.complete();
                return;
            }
            startPromise.fail(result.cause());
        });
    }

    private void addToClans() {
        vertx.sharedData().<Long, ClanInfo>getAsyncMap(CLAN_MAP, map ->
                map.result().put(this.id, this.info,
                        completion -> System.out.println("Clan " + this.id + " is created"))
        );
    }

    private MessageConsumer<Long> lookForAdmin() {
        return vertx.eventBus().<Long>consumer(SET_ADMIN + this.id, event -> {
            Long adminId = event.body();
            vertx.sharedData().<Long, ClanInfo>getAsyncMap(CLAN_MAP, map ->
                    map.result().get(this.id, getResult -> {
                                ClanInfo clanInfo = getResult.result();
                                if (!clanInfo.getAdministratorId().equals(-1L)) {
                                    System.out.println("This clan already has admin");
                                    return;
                                }
                                clanInfo.setAdministratorId(adminId);
                                map.result().put(this.id, clanInfo,
                                        completion -> {
                                            event.reply("Admin with id " + adminId + " is admin of clan with id " + this.id);
                                        }
                                );
                            }
                    )
            );
        });
    }

    public static final class Factory extends JavaVerticleFactory {
        private static Long number = 0L;

        public Factory() {
        }

        @Override
        public String prefix() {
            return "clan_game";
        } // TODO понять


        @Override
        public void createVerticle(String verticleName,
                                   ClassLoader classLoader,
                                   Promise<Callable<Verticle>> promise) {
            promise.complete(() -> new Clan(++number));
        }
    }
}
