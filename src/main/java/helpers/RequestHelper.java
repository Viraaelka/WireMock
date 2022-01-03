package helpers;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import enums.HttpOptions;
import lombok.extern.slf4j.Slf4j;
import model.RequestBody;
import model.RequestParameters;
import model.Setup;
import org.apache.commons.lang.StringUtils;

import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

@Slf4j
public class RequestHelper {
    private MappingBuilder mappingBuilder;
    private RequestBodyHelper requestBodyHelper = new RequestBodyHelper();

    enum UrlMatcher {
        Url,
        UrlMatching
    }

    private MappingBuilder getHttpRequest(UrlMatcher urlMatcher, HttpOptions request, String mockingEndpoint) {
        UrlPattern urlPathPattern = getUrl(urlMatcher, mockingEndpoint);
        switch (request) {
            case GET:
                mappingBuilder = WireMock.get(urlPathPattern);
                break;
            case POST:
                mappingBuilder = WireMock.post(urlPathPattern);
                break;
            case PATCH:
                mappingBuilder = WireMock.patch(urlPathPattern);
                break;
            case PUT:
                mappingBuilder = WireMock.put(urlPathPattern);
                break;
            case DELETE:
                mappingBuilder = WireMock.delete(urlPathPattern);
                break;
            default:
                mappingBuilder = any(urlPathPattern);
        }
        return mappingBuilder;
    }

    private UrlPattern getUrl(UrlMatcher urlMatcher, String urlPath) {
        switch (urlMatcher) {
            case UrlMatching:
                return urlMatching(urlPath);
            default:
                return urlEqualTo(urlPath);
        }
    }

    public MappingBuilder getRequest(Setup setupParameters) {
        return getRequest(setupParameters.getRequest());
    }

    public MappingBuilder getRequest(RequestParameters request) {
        String urlMapper = request.getUrlMapper();
        RequestBody body = request.getRequestBody();
        UrlMatcher urlMatcherEnum = StringUtils.isEmpty(urlMapper) ? UrlMatcher.Url : UrlMatcher.valueOf(urlMapper);
        mappingBuilder = getHttpRequest(urlMatcherEnum, HttpOptions.valueOf(request.getHttpType()), request.getEndpoint());
        if (StringUtils.isNotEmpty(request.getContentType()))
            mappingBuilder.withHeader("Content-Type", equalTo(request.getContentType()));
        if (StringUtils.isNotEmpty(request.getClientRef()))
            mappingBuilder.withHeader("Client-Ref", equalTo(request.getClientRef()));
        if (StringUtils.isNotEmpty(request.getRcRequestId()))
            mappingBuilder.withHeader("RCRequestId", equalTo(request.getRcRequestId()));
        if (StringUtils.isNotEmpty(request.getAccept()))
            mappingBuilder.withHeader("Accept", equalTo(request.getAccept()));
        if (StringUtils.isNotEmpty(request.getCookies()))
            mappingBuilder.withCookie("Cookies", containing(request.getCookies()));
        if (body != null) {
            requestBodyHelper.setMappingBuilderWithRequestBody(body, mappingBuilder);
        }
        return mappingBuilder;
    }
}