package com.example.dto;

import com.example.enums.ProfileStep;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AdminProfileDTO {
    private Long id;
    private String name;
    private String surname;
    private ProfileStep step;
    private String phone;
    public String writableString (){
        return id + "#" + name + "#" + surname + "#" + step + "#" + phone;
    }
}
