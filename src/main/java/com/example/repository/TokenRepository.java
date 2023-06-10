package com.example.repository;

import com.example.dto.TokenDTO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TokenRepository {
    public TokenDTO get(String id) {
        Optional<TokenDTO> optional = getAll().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        return optional.orElse(null);
    }
    public List<TokenDTO> getAll() {
        try {
            Stream<String> lines = Files.lines(Path.of("Token.txt"));
            return lines.map(s -> {
                String[] arr = s.split("#");
                TokenDTO dto = new TokenDTO();
                dto.setId(arr[0]);
                dto.setUser(arr[1]);
                dto.setToken(arr[2]);
                return dto;
            }).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new LinkedList<>();
    }
}