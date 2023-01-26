package com.tsemkalo.homework9.info;

import lombok.Data;

@Data
public final class UserDto implements DTO {
    private ParticipantInfo participantInfo;
    private Long messageDelay;
}
