package model;

import lombok.Data;

import java.util.List;

@Data
public class Scenario {
    private String name;
    private List<ScenarioParameters> parameters;

    public enum ScenarioState {
        Started
    }
}