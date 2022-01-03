package model;

import lombok.Data;

import java.util.List;

@Data
public class WireMockProcessor {
    private List<Setup> wiremock;
}