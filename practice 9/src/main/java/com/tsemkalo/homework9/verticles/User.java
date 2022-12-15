package com.tsemkalo.homework9.verticles;

import com.google.gson.JsonObject;
import com.tsemkalo.homework9.info.ClanInfo;
import com.tsemkalo.homework9.info.ParticipantInfo;
import io.vertx.core.eventbus.MessageConsumer;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.tsemkalo.homework9.verticles.Names.CLAN_MAP;
import static com.tsemkalo.homework9.verticles.Names.GREETING;
import static com.tsemkalo.homework9.verticles.Names.JOIN_REQUEST;
import static com.tsemkalo.homework9.verticles.Names.NEED_TO_RECONNECT;
import static com.tsemkalo.homework9.verticles.Names.PARTICIPANTS_MAP;

public final class User extends Participant {
    private final Long messageDelay;
    private Long messageTimer;

    public User(ParticipantInfo participantInfo, JsonObject additionalInfo) {
        super(participantInfo);
        this.messageDelay = additionalInfo.get("messageDelay").getAsLong();
    }

    @Override
    public void subscribe() {
        joinClan();
        vertx.eventBus().consumer(GREETING + getParticipantInfo().getId(), event -> {
            System.out.println(event.body() + getParticipantInfo().getName());
        });
    }

    private void joinClan() {
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
    }

    private void sendJoinRequest(Long clanId) {
        System.out.println("User " + getParticipantInfo().getName() + " is trying to join clan " + clanId + "...");
        vertx.eventBus().request(JOIN_REQUEST + clanId, getParticipantInfo().getId(), reply -> {
            if (reply.succeeded()) {
                System.out.println(getParticipantInfo().getName() + reply.result().body());
                getParticipantInfo().setClanId(clanId);
                putParticipantToMap();
                printClansUsers();
                if (messageTimer != null) {
                    vertx.cancelTimer(messageTimer);
                }
                messageTimer = sendMessageToRandomUser();
                handleIfReconnectNeeded();
            } else {
                System.out.println("Access denied for " + getParticipantInfo().getName() + reply.cause().getMessage());
                System.out.println(getParticipantInfo().getName() + ", your join request will be repeated in 5 seconds");
                vertx.setPeriodic(5_000L, timer -> {
                    if (ThreadLocalRandom.current().nextBoolean()) {
                        sendJoinRequest(clanId);
                        vertx.cancelTimer(timer);
                    }
                });
            }
        });
    }

    private Long sendMessageToRandomUser() {
        return vertx.setPeriodic(messageDelay, timer ->
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

    private void handleIfReconnectNeeded() {
        MessageConsumer<Object> consumer = vertx.eventBus().consumer(NEED_TO_RECONNECT + getParticipantInfo().getClanId());
        consumer.handler(event -> {
            vertx.sharedData().<Long, ParticipantInfo>getAsyncMap(PARTICIPANTS_MAP, map -> {
                map.result().remove(getParticipantInfo().getId(), complete -> {
                    System.out.println("User " + getParticipantInfo().getName() + " is unconnected from clan " + getParticipantInfo().getClanId());
                    consumer.pause();
                    joinClan();
                });
            });
        });
    }

    private void printClansUsers() {
        vertx.sharedData().<Long, ClanInfo>getAsyncMap(CLAN_MAP, map -> {
            map.result().entries(clans -> {
                System.out.println("__________________________________________");
                for (ClanInfo info : clans.result().values()) {
                    System.out.println("Users ids of clan " + info.getId() + ": " + info.getUsers());
                }
                System.out.println("__________________________________________");
            });
        });
    }
}