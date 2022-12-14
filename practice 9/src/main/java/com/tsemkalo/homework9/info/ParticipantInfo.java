package com.tsemkalo.homework9.info;

import lombok.Data;

import java.io.Serializable;

@Data
public final class ParticipantInfo implements Serializable {
    private Long id;
    private String name;
    private Long clanId;

    public ParticipantInfo(String name) {
        this.id = -1L;
        this.name = name;
        this.clanId = -1L;
    }
}
