package com.spil.shapeManagementApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBean {
    // "00" for success, "01" for failure
    private String responseCode;
    private String responseMessage;
    private Object content;
}
