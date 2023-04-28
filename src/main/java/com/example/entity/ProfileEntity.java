package com.example.entity;

import com.example.enums.ProfileStep;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter @Setter
public class ProfileEntity {
    private Long id;
    private ProfileStep step;
    private LocalDateTime createdDate = LocalDateTime.now();

    public String writableString() {
        return id + "#" + step + "#" + createdDate;
    }
}
