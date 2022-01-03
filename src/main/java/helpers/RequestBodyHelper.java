package helpers;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import enums.BodyMatcher;
import exception.WireMockException;
import lombok.SneakyThrows;
import model.RequestBody;
import utils.WireMockFileReader;

import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToIgnoreCase;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToXml;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.notMatching;

public class RequestBodyHelper {
    public MappingBuilder setMappingBuilderWithRequestBody(RequestBody body, MappingBuilder mappingBuilder) {
        String requestBody = getBodyText(body);
        BodyMatcher bodyMatcher = BodyMatcher.valueOf(body.getBodyMatcher());
        StringValuePattern stringValuePattern;
        switch (bodyMatcher) {
            case Containing:
                stringValuePattern = containing(requestBody);
                break;
            case EqualTo:
                stringValuePattern = equalTo(requestBody);
                break;
            case EqualToJson:
                stringValuePattern = equalToJson(requestBody);
                break;
            case EqualToIgnoreCase:
                stringValuePattern = equalToIgnoreCase(requestBody);
                break;
            case EqualToXml:
                stringValuePattern = equalToXml(requestBody);
                break;
            case Matching:
                stringValuePattern = matching(requestBody);
                break;
            case NotMatching:
                stringValuePattern = notMatching(requestBody);
                break;
            default:
                throw new IllegalStateException("Unexpected value at stringValuePattern: " + bodyMatcher);
        }
        return mappingBuilder.withRequestBody(stringValuePattern);
    }

    @SneakyThrows
    private String getBodyText(RequestBody body) {
        String text = "";
        if (body.getText() == null && body.getFileName() == null)
            throw new WireMockException("No text or file name is specified to set the body");
        else if (body.getText() != null && body.getFileName() != null)
            throw new WireMockException("Only one way can be specified for body: text or fileName");
        else if (body.getFileName() != null)
            text = WireMockFileReader.getFileContent(body.getFileName());
        else if (body.getText() != null)
            text = body.getText();
        return text;
    }
}