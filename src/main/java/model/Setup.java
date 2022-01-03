package model;

import lombok.Data;

@Data
public class Setup {
    private RequestParameters request;
    private ResponseParameters response;
    private Scenario scenario;
}