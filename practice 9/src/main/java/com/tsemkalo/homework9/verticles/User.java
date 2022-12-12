package com.tsemkalo.homework9.verticles;

import com.google.gson.JsonObject;
import com.tsemkalo.homework9.info.ClanInfo;
import com.tsemkalo.homework9.info.ParticipantInfo;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.tsemkalo.homework9.verticles.Names.CLAN_MAP;
import static com.tsemkalo.homework9.verticles.Names.GREETING;
import static com.tsemkalo.homework9.verticles.Names.JOIN_REQUEST;
import static com.tsemkalo.homework9.verticles.Names.NEED_TO_RECONNECT;
import static com.tsemkalo.homework9.verticles.Names.PARTICIPANTS_MAP;

public final class User extends Participant {
    private Long messageDelay;

    public User(ParticipantInfo participantInfo, JsonObject additionalInfo) {
        super(participantInfo);
        this.messageDelay = additionalInfo.get("messageDelay").getAsLong();
    }

    @Override
    public void subscribe() {
        System.out.println(getParticipantInfo().getName() + " wants to join some clan");
        vertx.sharedData().<Long, ClanInfo>getAsyncMap(CLAN_MAP, map ->
                map.result().entries(clans -> {
                    if (clans.result().isEmpty()) {
                        System.out.println("There are no clans");
                        return;
                    }
                    List<ClanInfo> clanInfoList = clans.result().values().stream().filter(ClanInfo::getIsActive).toList();
                    if (clanInfoList.size() > 0) {
                        Long clanId = clanInfoList.get(ThreadLocalRandom.current().nextInt(clanInfoList.size())).getId();
                        sendJoinRequest(clanId);
                    }
                })
        );

        vertx.eventBus().consumer(NEED_TO_RECONNECT + getParticipantInfo().getClanId(), event -> {
            vertx.sharedData().<Long, ParticipantInfo>getAsyncMap(PARTICIPANTS_MAP, map ->
                    map.result().remove(getParticipantInfo().getId()));
            subscribe();
        });

        vertx.eventBus().consumer(GREETING + getParticipantInfo().getId(), event -> {
            System.out.println(event.body() + getParticipantInfo().getName());
        });
    }

    private void sendJoinRequest(Long clanId) {
        vertx.eventBus().request(JOIN_REQUEST + clanId, getParticipantInfo().getId(), reply -> {
            if (reply.succeeded()) {
                System.out.println(reply.result().body());
                getParticipantInfo().setClanId(clanId);
                addParticipantToMap();
                sendMessageToRandomUser();
            } else {
                vertx.setTimer(10_000L, timer -> {
                    if (ThreadLocalRandom.current().nextBoolean()) {
                        subscribe();
                    }
                });
            }
        });
    }

    public void sendMessageToRandomUser() {
        vertx.setPeriodic(messageDelay, timer ->
                vertx.sharedData().<Long, ClanInfo>getAsyncMap(CLAN_MAP, map ->
                        map.result().get(getParticipantInfo().getClanId(), getResult -> {
                                    List<Long> usersId = getResult.result().getUsers();
                                    usersId.remove(getParticipantInfo().getId());
                                    if (usersId.size() > 1) {
                                        Long to = usersId.get(ThreadLocalRandom.current().nextInt(usersId.size()));
                                        vertx.eventBus().send(GREETING + to, "User " + getParticipantInfo().getName() + " is greeting ");
                                    }
                                }
                        )
                )
        );
    }
}