package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.WireMockProcessor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class WireMockFileReader {

    private static final String WIREMOCK_SETUP_FOLDER = "/src/test/resources/__files/";
    private static final String WIREMOCK_SETUP_FILE = WIREMOCK_SETUP_FOLDER + "setup.json";

    public static String getFileContent(String pathToFile) throws IOException {
        return Files
                .lines(Paths.get(System.getProperty("user.dir") + WIREMOCK_SETUP_FOLDER + pathToFile))
                .collect(Collectors.joining());
    }

    public static WireMockProcessor getWireMockMapper() throws IOException {
        return new ObjectMapper()
                .readValue(new File(System.getProperty("user.dir") + WIREMOCK_SETUP_FILE),
                        WireMockProcessor.class);
    }
}