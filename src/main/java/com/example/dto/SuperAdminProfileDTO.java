package com.example.dto;

import com.example.enums.ProfileStep;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SuperAdminProfileDTO {
    private Long id;
    private ProfileStep step;
    private Boolean visible;
    public String writableString(){
        return id + "#" + step + "#" + visible;
    }
}
