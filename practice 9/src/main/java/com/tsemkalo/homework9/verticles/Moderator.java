package com.tsemkalo.homework9.verticles;

import com.tsemkalo.homework9.info.ClanInfo;
import com.tsemkalo.homework9.info.ParticipantInfo;
import io.vertx.core.eventbus.MessageConsumer;

import java.util.List;

import static com.tsemkalo.homework9.verticles.Names.ADD_MODERATOR;
import static com.tsemkalo.homework9.verticles.Names.ADMIN_IS_SET;
import static com.tsemkalo.homework9.verticles.Names.CLAN_MAP;
import static com.tsemkalo.homework9.verticles.Names.JOIN_REQUEST;
import static com.tsemkalo.homework9.verticles.Names.TOO_MANY_USERS;

public final class Moderator extends Participant {
    public Moderator(ParticipantInfo participantInfo) {
        super(participantInfo);
    }

    // TODO undeploy for everybody
    @Override
    public void subscribe() {
        vertx.sharedData().<Long, ClanInfo>getAsyncMap(CLAN_MAP, map ->
                map.result().entries(clans -> {
                    if (clans.result().containsKey(getParticipantInfo().getClanId())) {
                        if (!clans.result().get(getParticipantInfo().getClanId()).getAdministratorId().equals(-1L)) {
                            joinClan();
                            return;
                        }
                    }
                    vertx.eventBus().consumer(ADMIN_IS_SET + getParticipantInfo().getClanId(), event -> joinClan());
                })
        );
        addUsersToClan();
    }

    private void joinClan() {
        System.out.println("Moderator " + getParticipantInfo().getName() + " is trying to connect to clan " + getParticipantInfo().getClanId() + "...");
        vertx.eventBus().request(ADD_MODERATOR + getParticipantInfo().getClanId(), getParticipantInfo().getId(), reply -> {
            if (reply.succeeded()) {
                System.out.println(getParticipantInfo().getName() + " " + reply.result().body());
                addParticipantToMap();
            } else {
                System.out.println("Access denied for " + getParticipantInfo().getName() + reply.cause().getMessage());
            }
        });
    }

    public void addUsersToClan() {
        MessageConsumer<Long> consumer = vertx.eventBus().<Long>consumer(JOIN_REQUEST + getParticipantInfo().getClanId());
        consumer.handler(event -> {
            consumer.pause();
            Long userId = event.body();
            vertx.sharedData().<Long, ClanInfo>getAsyncMap(CLAN_MAP, map ->
                    map.result().get(getParticipantInfo().getClanId(), clanInfo -> {
                        if (clanInfo.result().getIsActive() && clanInfo.result().getUsers().size() < clanInfo.result().getMaxUsersNumber()) {
                            List<Long> userIds = clanInfo.result().getUsers();
                            userIds.add(userId);
                            ClanInfo info = clanInfo.result();
                            info.setUsers(userIds);
                            map.result().put(clanInfo.result().getId(), info, result ->
                                    event.reply(" (#" + userId + ") joined the clan " + getParticipantInfo().getClanId())
                            );
                        } else {
                            event.fail(-1, " (#" + userId + "): Clan " + getParticipantInfo().getClanId() + " has no free places for users");
                            vertx.eventBus().send(TOO_MANY_USERS + getParticipantInfo().getClanId(), null);
                        }
                        consumer.resume();
                    }));
        });
    }
}
