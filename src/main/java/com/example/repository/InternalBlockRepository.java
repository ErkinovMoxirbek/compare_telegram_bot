package com.example.repository;

import com.example.entity.InternalBlockEntity;
import com.example.enums.ProfileStep;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InternalBlockRepository {
    public InternalBlockEntity get(String seriya) {
        Optional<InternalBlockEntity> optional = getAll().stream()
                .filter(p -> p.getBlockSeriya().equals(seriya))
                .findFirst();

        return optional.orElse(null);
    }
    public List<InternalBlockEntity> getAll() {
        try {
            Stream<String> lines = Files.lines(Path.of("InternalBlock.txt"));
            return lines.map(s -> {
                String[] arr = s.split("#");
                InternalBlockEntity internalBlock = new InternalBlockEntity();
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

    public void save(InternalBlockEntity entity) {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter("InternalBlock.txt", true));
            printWriter.println(entity.writableString());

            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void update(InternalBlockEntity profileEntity) {
        List<InternalBlockEntity> profileEntityList = getAll();
        profileEntityList.removeIf(p -> p.getId().equals(profileEntity.getId()));
        profileEntityList.add(profileEntity);
        rewriteList(profileEntityList);
    }
    public void remove(Long id) {
        List<InternalBlockEntity> profileEntityList = getAll();
        profileEntityList.removeIf(p -> p.getId().equals(id));
        rewriteList(profileEntityList);
    }

    public void rewriteList(List<InternalBlockEntity> list) {

        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter("InternalBlock.txt"));
            list.forEach(profileEntity -> {
                printWriter.println(profileEntity.writableString());
            });
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void rewrite(Long id,InternalBlockEntity entity){
        List<InternalBlockEntity> temp = getAll();
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).getId().equals(id)){
                temp.set(i,entity);
            }
        }
        rewriteList(temp);
    }
}
