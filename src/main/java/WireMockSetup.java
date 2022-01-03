import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import helpers.RequestHelper;
import helpers.ResponseHelper;
import helpers.ScenarioHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import model.Scenario;
import model.ScenarioParameters;
import model.Setup;
import model.WireMockProcessor;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.List;

import static utils.WireMockFileReader.getWireMockMapper;

@Slf4j
public class WireMockSetup {
    private final String HOST = "localhost";
    private final int PORT = 8080;
    private final static WireMockServer server = new WireMockServer();
    private final ResponseHelper responseHelper = new ResponseHelper();
    private final RequestHelper requestHelper = new RequestHelper();
    private final ScenarioHelper scenarioHelper = new ScenarioHelper();

    public WireMockSetup() {
        log.info("Starting WireMock server");
        server.start();
        WireMock.configureFor(HOST, PORT);
    }

    public void stubEndpoints() throws IOException {
        WireMockProcessor processor = getWireMockMapper();
        for (Setup parameters : processor.getWiremock()) {
            stubEndpoint(parameters);
        }
    }

    @SneakyThrows
    private void stubEndpoint(Setup setupParameters) {
        Scenario scenario = setupParameters.getScenario();
        if (scenario == null) {
            log.info(String.format("Stubbing the endpoint [%s]", setupParameters.getRequest().getEndpoint()));
            WireMock.stubFor(requestHelper.getRequest(setupParameters).willReturn(responseHelper.getResponse(setupParameters)));
        } else {
            if (StringUtils.isEmpty(scenario.getName()) || scenario.getParameters().isEmpty())
                throw new Exception("No scenario parameters for setting up wiremock");
            runScenarioStub(scenario, setupParameters);
        }
    }

    private void runScenarioStub(Scenario scenario, Setup setupParameters) {
        log.info(String.format("Stubbing the scenario [%s]", scenario.getName()));
        List<ScenarioParameters> scenarioParameters = scenario.getParameters();
        log.info(String.format("Set a stub for the initial state of the endpoint [%s] at scenario [%s]",
                setupParameters.getRequest().getEndpoint(),
                scenario.getName()));
        WireMock.stubFor(scenarioHelper.setScenarioRequest(setupParameters.getRequest(), scenario.getName(), Scenario.ScenarioState.Started.name())
                .willReturn(scenarioHelper.getResponse(setupParameters.getResponse()))
                .willSetStateTo(scenarioParameters.get(0).getRequiredScenarioState()));
        for (ScenarioParameters scenarioParameter : scenarioParameters) {
            log.info(String.format("Stubbing the endpoint [%s] at scenario [%s]", scenarioParameter.getRequest().getEndpoint(), scenario.getName()));
            WireMock.stubFor(scenarioHelper.setScenarioRequest(scenarioParameter.getRequest(), scenario.getName(), scenarioParameter.getRequiredScenarioState())
                    .willReturn(scenarioHelper.getResponse(scenarioParameter.getResponse()))
                    .willSetStateTo(scenarioParameter.getNewScenarioState()));
        }
    }

    public static void closeWireMockServer() {
        if (server.isRunning() && null != server) {
            log.info("WireMock server is shutting down");
            server.shutdown();
        }
    }
}