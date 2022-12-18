package com.tsemkalo.homework9.launchers;

import com.tsemkalo.homework9.info.AdministratorDto;
import com.tsemkalo.homework9.verticles.Administrator;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public final class AdministratorLauncher {
    public static void main(String[] args) throws IOException, URISyntaxException {
        List<AdministratorDto> administrators = JSONReader.<AdministratorDto>readParticipants("/administrator_data.json", AdministratorDto[].class);
        if (administrators == null) {
            System.out.println("Can not read administrators from file");
            return;
        }
        for (AdministratorDto administratorDto : administrators) {
            if (administratorDto.getMaxModeratorsNumber() == null ||
                    administratorDto.getMaxUsersNumber() == null ||
                    administratorDto.getParticipantInfo().getName() == null ||
                    administratorDto.getParticipantInfo().getClanId() == null
            ) {
                System.out.println("Administrator doesn't have enough data");
            } else {
                run(administratorDto);
            }
        }
    }

    public static void run(AdministratorDto administratorDto) {
        Vertx.clusteredVertx(
                new VertxOptions(),
                vertxResult -> {
                    Vertx vertx = vertxResult.result();
                    if (vertx == null) {
                        System.out.println("Administrator deploy was failed");
                        return;
                    }
                    Administrator.Factory factory = new Administrator.Factory(administratorDto.getParticipantInfo(), administratorDto.getMaxModeratorsNumber(), administratorDto.getMaxUsersNumber());
                    vertx.registerVerticleFactory(factory);
                    DeploymentOptions options = new DeploymentOptions().setWorker(true);
                    vertx.deployVerticle(
                            factory.prefix() + ":" + Administrator.class.getSimpleName(),
                            options,
                            result -> System.out.println("Administrator deploy result: " + result.succeeded())
                    );
                }
        );
    }
}
