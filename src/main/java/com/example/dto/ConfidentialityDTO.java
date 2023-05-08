package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ConfidentialityDTO {
    private String id;
    private String login;
    private String password;
    private int superAdminCount;
    public String writableString(){
        return id + "#" + login + "#" + password + "#" + superAdminCount;
    }
}
