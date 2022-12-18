package com.tsemkalo.homework9.launchers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.tsemkalo.homework9.info.DTO;
import com.tsemkalo.homework9.info.ParticipantInfo;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class JSONReader{
    public static <T extends DTO> List<T> readParticipants(String fileName, Class<T[]> type) throws URISyntaxException, IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        URL resource = AdministratorLauncher.class.getResource(fileName);
        if (resource != null) {
            Reader reader = new FileReader(Paths.get(resource.toURI()).toFile());
            return new ArrayList<>(Arrays.asList(gson.fromJson(reader, type)));
        }
        return null;
    }

    public static Integer readClansAmount(String fileName) throws IOException, URISyntaxException {
        Path filePath = Paths.get(ClanLauncher.class.getResource(fileName).toURI());
        try (Reader reader = Files.newBufferedReader(filePath)) {
            JsonObject tree = new Gson().fromJson(reader, JsonObject.class);
            return tree.get("clansAmount").getAsInt();
        }
    }
}
