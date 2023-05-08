package com.example.repository;

import com.example.dto.ConfidentialityDTO;
import com.example.dto.SuperAdminProfileDTO;
import com.example.enums.ProfileStep;
import com.example.enums.Role;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfidentialityRepository {
    //Confidentiality
    public void rewriteConfidentialityList(List<ConfidentialityDTO> list) {

        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter("Confidentiality.txt"));
            list.forEach(dto -> {
                printWriter.println(dto.writableString());
            });
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public ConfidentialityDTO getConfidentiality(String id) {
        Optional<ConfidentialityDTO> optional = getConfidentialityAll().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        return optional.orElse(null);
    }
    public List<ConfidentialityDTO> getConfidentialityAll() {
        try {
            Stream<String> lines = Files.lines(Path.of("Confidentiality.txt"));
            return lines.map(s -> {
                String[] arr = s.split("#");
                ConfidentialityDTO dto = new ConfidentialityDTO();
                dto.setId(arr[0]);
                dto.setLogin(arr[1]);
                dto.setPassword(arr[2]);
                dto.setSuperAdminCount(Integer.parseInt(arr[3]));
                return dto;
            }).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new LinkedList<>();
    }
}
