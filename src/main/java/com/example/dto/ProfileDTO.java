package com.example.dto;

import com.example.enums.ProfileStep;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter @Setter
public class ProfileDTO {
    private Long id;
    private ProfileStep step;
    private LocalDateTime createdDate = LocalDateTime.now();
    private String nowPath ;
    public String writableString() {
        return id + "#" + step + "#" + createdDate + "#" + nowPath;
    }
}
