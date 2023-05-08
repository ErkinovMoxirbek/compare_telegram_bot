package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ConfidentialityDTO {
    private String id;
    private String login;
    private String password;
    public String writableString(){
        return id + "#" + login + "#" + password ;
    }
}
