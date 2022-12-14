package com.tsemkalo.homework9.verticles;

import com.google.gson.JsonObject;
import com.tsemkalo.homework9.info.ClanInfo;
import com.tsemkalo.homework9.info.ParticipantInfo;
import io.vertx.core.eventbus.MessageConsumer;

import java.util.ArrayList;
import java.util.List;

import static com.tsemkalo.homework9.verticles.Names.ADD_MODERATOR;
import static com.tsemkalo.homework9.verticles.Names.ADMIN_IS_SET;
import static com.tsemkalo.homework9.verticles.Names.CLAN_CREATED;
import static com.tsemkalo.homework9.verticles.Names.CLAN_MAP;
import static com.tsemkalo.homework9.verticles.Names.NEED_TO_RECONNECT;
import static com.tsemkalo.homework9.verticles.Names.SET_ADMIN;
import static com.tsemkalo.homework9.verticles.Names.TOO_MANY_USERS;
import static com.tsemkalo.homework9.verticles.Names.TURN_OFF_ADMIN;

public final class Administrator extends Participant {
    private final JsonObject clanData;

    public Administrator(ParticipantInfo participantInfo, JsonObject clanData) {
        super(participantInfo);
        this.clanData = clanData;
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
                            int maxUsersNumber = clanData.get("maxUsersNumber").getAsInt();
                            int maxModeratorsNumber = clanData.get("maxModeratorsNumber").getAsInt();
                            clanInfo.setMaxUsersNumber(maxUsersNumber);
                            clanInfo.setMaxModeratorsNumber(maxModeratorsNumber);
                            clanInfo.setIsActive(true); // TODO turn on / off
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
                        System.out.println("Administrator " + getParticipantInfo().getName() + " is offline now, so clan is inactive");
                        map.result().put(info.getId(), info, result -> {
                            vertx.eventBus().publish(NEED_TO_RECONNECT + getParticipantInfo().getClanId(), null);
                        });
                    }));
        });
    }
}
