package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class ErrorDTO {
    private String errorCode;
    private String kondisanerModel;
    private String kondisanerModelName;
    private String meaningCode;
    private String meaningCodeName;
    private String errorInfo;
    private String errorInfoName;
    private String correctionSequence;
    private String correctionSequenceName;
}
