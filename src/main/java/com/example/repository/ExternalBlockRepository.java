package com.example.repository;

import com.example.entity.ExternalBlockEntity;

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

public class ExternalBlockRepository {
    public ExternalBlockEntity get(String seriya) {
        Optional<ExternalBlockEntity> optional = getAll().stream()
                .filter(p -> p.getBlockSeriya().equals(seriya))
                .findFirst();

        return optional.orElse(null);
    }
    public List<ExternalBlockEntity> getAll() {
        try {
            Stream<String> lines = Files.lines(Path.of("ExternalBlock.txt"));
            return lines.map(s -> {
                String[] arr = s.split("#");
                ExternalBlockEntity internalBlock = new ExternalBlockEntity();
                internalBlock.setId(Integer.valueOf(arr[0]));
                internalBlock.setBlockSeriya(arr[1]);
                internalBlock.setNumber(Integer.valueOf(arr[2]));
                return internalBlock;
            }).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new LinkedList<>();
    }

    public void save(ExternalBlockEntity entity) {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter("ExternalBlock.txt", true));
            printWriter.println(entity.writableString());

            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void update(ExternalBlockEntity profileEntity) {
        List<ExternalBlockEntity> profileEntityList = getAll();
        profileEntityList.removeIf(p -> p.getId().equals(profileEntity.getId()));
        profileEntityList.add(profileEntity);
        rewriteList(profileEntityList);
    }
    public void remove(Integer id) {
        List<ExternalBlockEntity> profileEntityList = getAll();
        profileEntityList.removeIf(p -> p.getId().equals(id));
        rewriteList(profileEntityList);
    }

    public void rewriteList(List<ExternalBlockEntity> list) {

        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter("ExternalBlock.txt"));
            list.forEach(profileEntity -> {
                printWriter.println(profileEntity.writableString());
            });
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void rewrite(Integer id,ExternalBlockEntity entity){
        List<ExternalBlockEntity> temp = getAll();
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).getId().equals(id)){
                temp.set(i,entity);
            }
        }
        rewriteList(temp);
    }
}
