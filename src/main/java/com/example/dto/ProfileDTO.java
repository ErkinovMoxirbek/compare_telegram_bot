package com.example.dto;

import com.example.enums.ProfileStep;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProfileDTO {
    private Long id;
    private ProfileStep step;
}
