package helpers;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.ScenarioMappingBuilder;
import model.RequestParameters;
import model.ResponseParameters;

public class ScenarioHelper {
    public MappingBuilder getRequest(RequestParameters requestParameters) {
        return new RequestHelper().getRequest(requestParameters);
    }

    public ScenarioMappingBuilder setScenarioRequest(RequestParameters requestParameters, String scenarioName, String scenarioState) {
        return getRequest(requestParameters)
                .inScenario(scenarioName)
                .whenScenarioStateIs(scenarioState);
    }

    public ResponseDefinitionBuilder getResponse(ResponseParameters responseParameters) {
        return new ResponseHelper().getResponse(responseParameters);
    }
}