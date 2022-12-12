package com.tsemkalo.homework9.verticles;

import com.google.gson.JsonObject;
import com.tsemkalo.homework9.info.ClanInfo;
import com.tsemkalo.homework9.info.ParticipantInfo;

import java.util.ArrayList;
import java.util.List;

import static com.tsemkalo.homework9.verticles.Names.ADD_MODERATOR;
import static com.tsemkalo.homework9.verticles.Names.ADMIN_IS_SET;
import static com.tsemkalo.homework9.verticles.Names.CLAN_CREATED;
import static com.tsemkalo.homework9.verticles.Names.CLAN_MAP;
import static com.tsemkalo.homework9.verticles.Names.NEED_TO_RECONNECT;
import static com.tsemkalo.homework9.verticles.Names.SET_ADMIN;
import static com.tsemkalo.homework9.verticles.Names.TOO_MANY_USERS;

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
    }

    private void joinClan() {
        System.out.println("Admin " + getParticipantInfo().getName() + " is trying to connect to clan...");
        vertx.eventBus().request(SET_ADMIN + getParticipantInfo().getClanId(), getParticipantInfo().getId(), reply -> {
            if (reply.succeeded()) {
                addParticipantToMap();
                vertx.sharedData().<Long, ClanInfo>getAsyncMap(CLAN_MAP, map ->
                        map.result().get(getParticipantInfo().getClanId(), getResult -> {
                            ClanInfo clanInfo = getResult.result();
                            int maxUsersNumber = clanData.get("maxUsersNumber").getAsInt();
                            int maxModeratorsNumber = clanData.get("maxModeratorsNumber").getAsInt();
                            clanInfo.setMaxUsersNumber(maxUsersNumber);
                            clanInfo.setMaxModeratorsNumber(maxModeratorsNumber);
                            clanInfo.setIsActive(true); // TODO turn on / off
                            map.result().put(getParticipantInfo().getClanId(), clanInfo,
                                    completion -> System.out.println("Max users number is set to " + maxUsersNumber + "\n"
                                            + "Max moderators number is set to " + maxModeratorsNumber)
                            );
                            vertx.eventBus().publish(ADMIN_IS_SET + getParticipantInfo().getClanId(), null);
                        })
                );
            }
        });
    }

    private void addModerators() {
        vertx.eventBus().<Long>consumer(ADD_MODERATOR + getParticipantInfo().getClanId(), event -> {
            Long moderatorId = event.body();
            vertx.sharedData().<Long, ClanInfo>getAsyncMap(CLAN_MAP, map ->
                    map.result().get(getParticipantInfo().getClanId(), clanInfo -> {
                        if (clanInfo.result().getModerators().size() < clanInfo.result().getMaxModeratorsNumber()) {
                            List<Long> moderatorIds = clanInfo.result().getModerators();
                            moderatorIds.add(moderatorId);
                            ClanInfo info = clanInfo.result();
                            info.setModerators(moderatorIds);
                            map.result().put(clanInfo.result().getId(), info);
                            event.reply("Participant with id " + moderatorId + " is a moderator of clan " + getParticipantInfo().getClanId());
                        } else {
                            System.out.println("There are no free places for moderators");
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
                        map.result().put(clanInfo.result().getId(), info);
                        // TODO clear user's clanId
                    }));
            vertx.eventBus().publish(NEED_TO_RECONNECT + getParticipantInfo().getClanId(), null);
        });
    }
}
