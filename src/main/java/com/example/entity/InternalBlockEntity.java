package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class InternalBlockEntity {
    private Integer id;
    private String blockSeriya;
    private Integer number;
    public String writableString() {
        return id + "#" + blockSeriya + "#" + number;
    }
}
