package helpers;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import exception.WireMockException;
import lombok.SneakyThrows;
import model.ResponseBody;
import model.ResponseParameters;
import model.Setup;
import org.apache.commons.lang.StringUtils;

public class ResponseHelper {
    private ResponseDefinitionBuilder mockResponse = new ResponseDefinitionBuilder();

    private ResponseDefinitionBuilder generateResponse(int status) {
        return mockResponse
                .withStatus(status);
    }

    private ResponseDefinitionBuilder generateResponseWithBodyFile(int status, String fileName) {
        return generateResponse(status)
                .withBodyFile(fileName);
    }

    private ResponseDefinitionBuilder generateResponseWithBody(int status, String body) {
        return generateResponse(status)
                .withBody(body);
    }

    @SneakyThrows
    public ResponseDefinitionBuilder generateResponseDefinitionBuilder(ResponseParameters response) {
        ResponseBody body = response.getResponseBody();
        int status = response.getStatus() == 0 ? 200 : response.getStatus();
        if (body == null)
            return generateResponse(status);
        else if (body.getText() != null && body.getFileName() != null)
            throw new WireMockException("Only one way can be specified for body: text or fileName");
        else if (body.getFileName() != null)
            return generateResponseWithBodyFile(status, body.getFileName());
        return generateResponseWithBody(status, body.getText());
    }

    public ResponseDefinitionBuilder getResponse(Setup setupParameters) {
        return getResponse(setupParameters.getResponse());
    }

    public ResponseDefinitionBuilder getResponse(ResponseParameters response) {
        mockResponse = generateResponseDefinitionBuilder(response);
        if (StringUtils.isNotEmpty(response.getContentType()))
            mockResponse.withHeader("Content-Type", response.getContentType());
        if (StringUtils.isNotEmpty(response.getClientRef()))
            mockResponse.withHeader("Client-Ref", response.getClientRef());
        if (StringUtils.isNotEmpty(response.getRcRequestId()))
            mockResponse.withHeader("RCRequestId", response.getRcRequestId());
        if (StringUtils.isNotEmpty(response.getRcRequestId()))
            mockResponse.withHeader("Content-Language", response.getContentLanguage());
        if (StringUtils.isNotEmpty(response.getContentLanguage()))
            mockResponse.withHeader("Content-Length", response.getContentLength());
        return mockResponse;
    }
}