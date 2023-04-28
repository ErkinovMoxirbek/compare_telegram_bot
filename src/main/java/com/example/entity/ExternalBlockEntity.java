package com.example.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class ExternalBlockEntity {
    private Integer id;
    private String blockSeriya;
    private Integer number;
    public String writableString(){
        return id + "#" + blockSeriya + "#" + number;
    }
}
