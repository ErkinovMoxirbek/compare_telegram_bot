package com.example.dto;

import com.example.enums.ProfileStep;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class AdminProfileDTO {
    private Long id;
    private String name;
    private String surname;
    private ProfileStep step;
    private String phone;
    private Boolean visible = false;
    public String writableString (){
        return id + "#" + name + "#" + surname + "#" + step + "#" + phone + "#" + visible;
    }
}
