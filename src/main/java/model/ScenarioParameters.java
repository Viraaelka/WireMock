package model;

import lombok.Data;

@Data
public class ScenarioParameters {
    private String requiredScenarioState;
    private String newScenarioState;
    private RequestParameters request;
    private ResponseParameters response;
}
