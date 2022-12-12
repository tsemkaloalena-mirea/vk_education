package com.tsemkalo.homework9.info;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public final class ClanInfo implements Serializable {
    private Long id;
    private int maxUsersNumber;
    private int maxModeratorsNumber;
    private List<Long> users;
    private List<Long> moderators;
    private Long administratorId;
    private Boolean isActive;

    public ClanInfo(Long id) {
        this.id = id;
        maxUsersNumber = 10;
        maxModeratorsNumber = 10;
        users = new ArrayList<>();
        moderators = new ArrayList<>();
        administratorId = -1L;
        isActive = false;
    }
}
