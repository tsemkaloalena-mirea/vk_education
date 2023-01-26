package com.tsemkalo.homework9.info;

import lombok.Data;

@Data
public final class AdministratorDto implements DTO {
    private Integer maxUsersNumber;
    private Integer maxModeratorsNumber;
    private ParticipantInfo participantInfo;
}
