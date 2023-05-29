package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ErrorDTO {
    private String errorCode;
    private String kondisanerModel;
    private String errorInfo;
    private String correctionSequence;
}
