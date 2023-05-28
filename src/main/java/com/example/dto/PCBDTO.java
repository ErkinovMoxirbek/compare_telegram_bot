package com.example.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class PCBDTO {
    private String id;
    private String group;
    private String name;
    private String PCBBoxCodeAssembly;
    private String PCBCodeWhole;
    private String PCBBoxCode;
    private String PCBCode;
    private String SAPCode;
    //private String model;
}
