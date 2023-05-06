package com.example.dto;

import com.example.enums.ProfileStep;
import com.example.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SuperAdminProfileDTO {
    private Long id;
    private ProfileStep step;
    private String login;
    private String password;
    private Role role = Role.SUPER_ADMIN;
    public String writableString(){
        return id + "#" + step + "#" + login + "#" + password + "#" + role;
    }
}
