package com.example.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class InternalBlockDTO {
    private Integer id;
    private String blockSeriya;
    private String number;
    public String writableString() {
        return id + "#" + blockSeriya + "#" + number;
    }
}
