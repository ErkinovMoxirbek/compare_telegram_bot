package com.example.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class ExternalBlockDTO {
    private Integer id;
    private String blockSeriya;
    private Integer number;
    public String writableString(){
        return id + "#" + blockSeriya + "#" + number;
    }
}
