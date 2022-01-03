package model;

import lombok.Data;

@Data
public class RequestParameters {
    private String endpoint;
    private String httpType;
    private String urlMapper;
    private RequestBody requestBody;
    private String contentType;
    private String accept;
    private String clientRef;
    private String rcRequestId;
    private String cookies;
}