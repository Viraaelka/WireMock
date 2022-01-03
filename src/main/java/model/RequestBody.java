package model;

import lombok.Data;

@Data
public class RequestBody {
    private String bodyMatcher;
    private String text;
    private String fileName;
}