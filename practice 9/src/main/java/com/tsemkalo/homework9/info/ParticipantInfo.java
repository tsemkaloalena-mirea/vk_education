package com.tsemkalo.homework9.info;

import lombok.Data;

import java.io.Serializable;

@Data
public final class ParticipantInfo implements Serializable, DTO {
    private Long id;
    private String name;
    private Long clanId;

    public ParticipantInfo(String name) {
        this.id = null;
        this.name = name;
        this.clanId = null;
    }
//    public void setId(Long id) {
//        if (id == null)
//    }
}
