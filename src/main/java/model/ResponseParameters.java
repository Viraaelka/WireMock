package model;

import lombok.Data;

@Data
public class ResponseParameters {
    private ResponseBody responseBody;
    private String contentType;
    private int status;
    private String contentLanguage;
    private String contentLength;
    private String clientRef;
    private String rcRequestId;
}