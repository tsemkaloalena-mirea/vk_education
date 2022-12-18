package com.tsemkalo.homework9.verticles;

import com.tsemkalo.homework9.info.ClanInfo;
import com.tsemkalo.homework9.info.ParticipantInfo;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.impl.JavaVerticleFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static com.tsemkalo.homework9.verticles.Names.ADD_MODERATOR;
import static com.tsemkalo.homework9.verticles.Names.ADMIN_IS_SET;
import static com.tsemkalo.homework9.verticles.Names.CLAN_CREATED;
import static com.tsemkalo.homework9.verticles.Names.CLAN_MAP;
import static com.tsemkalo.homework9.verticles.Names.NEED_TO_RECONNECT;
import static com.tsemkalo.homework9.verticles.Names.SET_ADMIN;
import static com.tsemkalo.homework9.verticles.Names.TOO_MANY_USERS;
import static com.tsemkalo.homework9.verticles.Names.TURN_OFF_ADMIN;

public final class Administrator extends Participant {
    private final Integer maxModeratorsNumber;
    private final Integer maxUsersNumber;

    public Administrator(ParticipantInfo participantInfo, Integer maxModeratorsNumber, Integer maxUsersNumber) {
        super(participantInfo);
        this.maxModeratorsNumber = maxModeratorsNumber;
        this.maxUsersNumber = maxUsersNumber;
    }

    @Override
    public void subscribe() {
        vertx.sharedData().<Long, ClanInfo>getAsyncMap(CLAN_MAP, map ->
                map.result().entries(clans -> {
                    if (clans.result().containsKey(getParticipantInfo().getClanId())) {
                        joinClan();
                    } else {
                        vertx.eventBus().consumer(CLAN_CREATED + getParticipantInfo().getClanId(), event -> joinClan());
                    }
                })
        );
        addModerators();
        handleIfTooManyUsers();
        handleIfBecomeOffline();
    }

    private void joinClan() {
        System.out.println("Admin " + getParticipantInfo().getName() + " is trying to connect to clan " + getParticipantInfo().getClanId() + "...");
        vertx.eventBus().request(SET_ADMIN + getParticipantInfo().getClanId(), getParticipantInfo().getId(), reply -> {
            if (reply.succeeded()) {
                putParticipantToMap();
                System.out.println(getParticipantInfo().getName() + reply.result().body());
                vertx.sharedData().<Long, ClanInfo>getAsyncMap(CLAN_MAP, map ->
                        map.result().get(getParticipantInfo().getClanId(), getResult -> {
                            ClanInfo clanInfo = getResult.result();
                            clanInfo.setMaxUsersNumber(maxUsersNumber);
                            clanInfo.setMaxModeratorsNumber(maxModeratorsNumber);
                            clanInfo.setIsActive(true);
                            map.result().put(getParticipantInfo().getClanId(), clanInfo,
                                    completion -> {
                                        System.out.println("Max users number for clan " + getParticipantInfo().getClanId() + " is set to " + maxUsersNumber);
                                        System.out.println("Max moderators number for clan " + getParticipantInfo().getClanId() + " is set to " + maxModeratorsNumber);
                                    }
                            );
                            vertx.eventBus().publish(ADMIN_IS_SET + getParticipantInfo().getClanId(), null);
                        })
                );
            } else {
                System.out.println("Access denied for " + getParticipantInfo().getName() + reply.cause().getMessage());
            }
        });
    }

    private void addModerators() {
        MessageConsumer<Long> consumer = vertx.eventBus().<Long>consumer(ADD_MODERATOR + getParticipantInfo().getClanId());
        consumer.handler(event -> {
            consumer.pause();
            Long moderatorId = event.body();
            vertx.sharedData().<Long, ClanInfo>getAsyncMap(CLAN_MAP, map ->
                    map.result().get(getParticipantInfo().getClanId(), clanInfo -> {
                        ClanInfo info = clanInfo.result();
                        List<Long> moderatorIds = info.getModerators();
                        if (moderatorIds.size() < info.getMaxModeratorsNumber()) {
                            moderatorIds.add(moderatorId);
                            info.setModerators(moderatorIds);
                            map.result().put(clanInfo.result().getId(), info, result -> {
                                event.reply("(#" + moderatorId + ") is a moderator of clan " + getParticipantInfo().getClanId());
                                consumer.resume();
                            });
                        } else {
                            event.fail(-1, " (#" + moderatorId + "): Clan " + getParticipantInfo().getClanId() + " has no free places for moderators");
                            consumer.resume();
                        }
                    })
            );
        });
    }

    private void handleIfTooManyUsers() {
        vertx.eventBus().<Long>consumer(TOO_MANY_USERS + getParticipantInfo().getClanId(), event -> {
            vertx.sharedData().<Long, ClanInfo>getAsyncMap(CLAN_MAP, map ->
                    map.result().get(getParticipantInfo().getClanId(), clanInfo -> {
                        ClanInfo info = clanInfo.result();
                        info.setUsers(new ArrayList<>());
                        map.result().put(info.getId(), info, result -> {
                            System.out.println("Clan " + getParticipantInfo().getClanId() + " has too many users. Everybody needs to reconnect");
                            vertx.eventBus().publish(NEED_TO_RECONNECT + getParticipantInfo().getClanId(), null);
                        });
                    }));
        });
    }

    private void handleIfBecomeOffline() {
        vertx.eventBus().consumer(TURN_OFF_ADMIN + getParticipantInfo().getId(), event -> {
            vertx.sharedData().<Long, ClanInfo>getAsyncMap(CLAN_MAP, map ->
                    map.result().get(getParticipantInfo().getClanId(), clanInfo -> {
                        ClanInfo info = clanInfo.result();
                        info.setIsActive(false);
                        info.setUsers(new ArrayList<>());
                        System.out.println("Administrator " + getParticipantInfo().getName() + " is offline now, so clan is inactive");
                        map.result().put(info.getId(), info, result -> {
                            vertx.eventBus().publish(NEED_TO_RECONNECT + getParticipantInfo().getClanId(), null);
                        });
                    }));
        });
    }

    public static final class Factory extends JavaVerticleFactory {
        private final ParticipantInfo participantInfo;
        private final Integer maxModeratorsNumber;
        private final Integer maxUsersNumber;

        public Factory(ParticipantInfo participantInfo, Integer maxModeratorsNumber, Integer maxUsersNumber) {
            this.participantInfo = participantInfo;
            this.maxModeratorsNumber = maxModeratorsNumber;
            this.maxUsersNumber = maxUsersNumber;
        }

        @Override
        public String prefix() {
            return "clan_game";
        }

        @Override
        public void createVerticle(String verticleName,
                                   ClassLoader classLoader,
                                   Promise<Callable<Verticle>> promise) {
            promise.complete(() -> new Administrator(participantInfo, maxModeratorsNumber, maxUsersNumber));
        }
    }
}
