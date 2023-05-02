package com.example.repository;

import com.example.dto.ProfileEntity;
import com.example.enums.ProfileStep;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProfileRepository {
    public ProfileEntity getProfile(Long id) {
        Optional<ProfileEntity> optional = getAll().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        return optional.orElse(null);
    }
    public List<ProfileEntity> getAll() {
        try {
            Stream<String> lines = Files.lines(Path.of("Profile.txt"));
            return lines.map(s -> {
                String[] arr = s.split("#");
                ProfileEntity profileEntity = new ProfileEntity();
                profileEntity.setId(Long.valueOf(arr[0]));
                profileEntity.setStep(ProfileStep.valueOf(arr[1]));
                profileEntity.setCreatedDate(LocalDateTime.parse(arr[2]));
                return profileEntity;
            }).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new LinkedList<>();
    }

    public void save(ProfileEntity entity) {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter("Profile.txt", true));
            printWriter.println(entity.writableString());

            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void update(ProfileEntity profileEntity) {
        List<ProfileEntity> profileEntityList = getAll();
        profileEntityList.removeIf(p -> p.getId().equals(profileEntity.getId()));
        profileEntityList.add(profileEntity);
        rewriteList(profileEntityList);
    }
    public void remove(Long id) {
        List<ProfileEntity> profileEntityList = getAll();
        profileEntityList.removeIf(p -> p.getId().equals(id));
        rewriteList(profileEntityList);
    }

    public void rewriteList(List<ProfileEntity> list) {

        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter("Profile.txt"));
            list.forEach(profileEntity -> {
                printWriter.println(profileEntity.writableString());
            });
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void rewriteDriver(Long id,ProfileEntity entity){
        List<ProfileEntity> temp = getAll();
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).getId().equals(id)){
                temp.set(i,entity);
            }
        }
        rewriteList(temp);
    }
}
