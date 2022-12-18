package com.tsemkalo.homework9.info;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public final class ClanInfo implements Serializable, DTO {
    private Long id;
    private int maxUsersNumber;
    private int maxModeratorsNumber;
    private List<Long> users;
    private List<Long> moderators;
    private Long administratorId;
    private Boolean isActive;

    public ClanInfo(Long id) {
        this.id = id;
        maxUsersNumber = 0;
        maxModeratorsNumber = 0;
        users = new ArrayList<>();
        moderators = new ArrayList<>();
        administratorId = null;
        isActive = false;
    }
}
