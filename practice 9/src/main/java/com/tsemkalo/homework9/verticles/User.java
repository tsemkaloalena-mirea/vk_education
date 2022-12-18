package com.tsemkalo.homework9.verticles;

import com.tsemkalo.homework9.info.ClanInfo;
import com.tsemkalo.homework9.info.ParticipantInfo;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.impl.JavaVerticleFactory;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

import static com.tsemkalo.homework9.verticles.Names.CLAN_MAP;
import static com.tsemkalo.homework9.verticles.Names.GREETING;
import static com.tsemkalo.homework9.verticles.Names.JOIN_REQUEST;
import static com.tsemkalo.homework9.verticles.Names.NEED_TO_RECONNECT;
import static com.tsemkalo.homework9.verticles.Names.PARTICIPANTS_MAP;

public final class User extends Participant {
    private final Long messageDelay;
    private Long messageTimer;

    public User(ParticipantInfo participantInfo, Long messageDelay) {
        super(participantInfo);
        this.messageDelay = messageDelay;
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
        vertx.sharedData().<Long, ClanInfo>getAsyncMap(CLAN_MAP, map -> {
            if (map.result() != null) {
                map.result().entries(clans -> {
                    if (clans != null && !clans.result().isEmpty()) {
                        List<ClanInfo> clanInfoList = clans.result().values().stream().filter(ClanInfo::getIsActive).toList();
                        if (clanInfoList.size() > 0) {
                            Long clanId = clanInfoList.get(ThreadLocalRandom.current().nextInt(clanInfoList.size())).getId();
                            sendJoinRequest(clanId);
                            return;
                        }
                    }
                    System.out.println("There are no clans");
                });
            } else {
                System.out.println("There are no clans");
            }
        });
    }

    private void sendJoinRequest(Long clanId) {
        if (getParticipantInfo().getClanId() == null) {
            System.out.println("User " + getParticipantInfo().getName() + " is trying to join clan " + clanId + "...");
            vertx.eventBus().request(JOIN_REQUEST + clanId, getParticipantInfo().getId(), reply -> {
                if (reply.succeeded()) {
                    System.out.println(getParticipantInfo().getName() + reply.result().body());
                    getParticipantInfo().setClanId(clanId);
                    putParticipantToMap();
                    printClansUsers();
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
            consumer.pause();
            vertx.sharedData().<Long, ParticipantInfo>getAsyncMap(PARTICIPANTS_MAP, map -> {
                map.result().remove(getParticipantInfo().getId(), complete -> {
                    System.out.println("User " + getParticipantInfo().getName() + " is unconnected from clan " + getParticipantInfo().getClanId());
                    if (messageTimer != null) {
                        vertx.cancelTimer(messageTimer);
                    }
                    getParticipantInfo().setClanId(null);
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

    public static final class Factory extends JavaVerticleFactory {
        private final ParticipantInfo participantInfo;
        private final Long messageDelay;

        public Factory(ParticipantInfo participantInfo, Long messageDelay) {
            this.participantInfo = participantInfo;
            this.messageDelay = messageDelay;
        }

        @Override
        public String prefix() {
            return "clan_game";
        }

        @Override
        public void createVerticle(String verticleName,
                                   ClassLoader classLoader,
                                   Promise<Callable<Verticle>> promise) {
            promise.complete(() -> new User(participantInfo, messageDelay));
        }
    }
}