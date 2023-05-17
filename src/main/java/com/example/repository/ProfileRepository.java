package com.example.repository;


import com.example.dto.AdminProfileDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.SuperAdminProfileDTO;
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
    // USER
    public ProfileDTO getProfile(Long id) {
        Optional<ProfileDTO> optional = getAll().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        return optional.orElse(null);
    }
    public List<ProfileDTO> getAll() {
        try {
            Stream<String> lines = Files.lines(Path.of("Profile.txt"));
            return lines.map(s -> {
                String[] arr = s.split("#");
                ProfileDTO profileEntity = new ProfileDTO();
                profileEntity.setId(Long.valueOf(arr[0]));
                profileEntity.setStep(ProfileStep.valueOf(arr[1]));
                profileEntity.setCreatedDate(LocalDateTime.parse(arr[2]));
                profileEntity.setNowPath(arr[3]);
                profileEntity.setLastMessageId(arr[4]);
                return profileEntity;
            }).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new LinkedList<>();
    }

    public void save(ProfileDTO entity) {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter("Profile.txt", true));
            printWriter.println(entity.writableString());

            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void update(ProfileDTO profileEntity) {
        List<ProfileDTO> profileEntityList = getAll();
        profileEntityList.removeIf(p -> p.getId().equals(profileEntity.getId()));
        profileEntityList.add(profileEntity);
        rewriteList(profileEntityList);
    }
    public void remove(Long id) {
        List<ProfileDTO> profileEntityList = getAll();
        profileEntityList.removeIf(p -> p.getId().equals(id));
        rewriteList(profileEntityList);
    }

    public void rewriteList(List<ProfileDTO> list) {

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
    // ADMIN
    public AdminProfileDTO getAdminProfile(Long id) {
        Optional<AdminProfileDTO> optional = getAdminAll().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        return optional.orElse(null);
    }
    public List<AdminProfileDTO> getAdminAll() {
        try {
            Stream<String> lines = Files.lines(Path.of("AdminProfile.txt"));
            return lines.map(s -> {
                String[] arr = s.split("#");
                AdminProfileDTO dto = new AdminProfileDTO();
                dto.setId(Long.valueOf(arr[0]));
                dto.setName(arr[1]);
                dto.setSurname(arr[2]);
                dto.setStep(ProfileStep.valueOf(arr[3]));
                dto.setPhone(arr[4]);
                dto.setVisible(Boolean.valueOf(arr[5]));
                return dto;
            }).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new LinkedList<>();
    }

    public void saveAdmin(AdminProfileDTO dto) {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter("AdminProfile.txt", true));
            printWriter.println(dto.writableString());

            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateAdmin(AdminProfileDTO dto) {
        List<AdminProfileDTO> dtoList = getAdminAll();
        dtoList.removeIf(p -> p.getId().equals(dto.getId()));
        dtoList.add(dto);
        rewriteAdminList(dtoList);
    }
    public void removeAdmin(Long id) {
        List<AdminProfileDTO>dtoList = getAdminAll();
        dtoList.removeIf(p -> p.getId().equals(id));
        rewriteAdminList(dtoList);
    }

    public void rewriteAdminList(List<AdminProfileDTO> list) {

        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter("AdminProfile.txt"));
            list.forEach(dto -> {
                printWriter.println(dto.writableString());
            });
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //SUPER ADMIN
    public SuperAdminProfileDTO getSuperAdminProfile(Long id) {
        Optional<SuperAdminProfileDTO> optional = getSuperAdminAll().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        return optional.orElse(null);
    }
    public List<SuperAdminProfileDTO> getSuperAdminAll() {
        try {
            Stream<String> lines = Files.lines(Path.of("SuperAdminProfile.txt"));
            return lines.map(s -> {
                String[] arr = s.split("#");
                SuperAdminProfileDTO dto = new SuperAdminProfileDTO();
                dto.setId(Long.valueOf(arr[0]));
                dto.setStep(ProfileStep.valueOf(arr[1]));
                dto.setVisible(Boolean.valueOf(arr[2]));
                return dto;
            }).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new LinkedList<>();
    }

    public void saveSuperAdmin(SuperAdminProfileDTO dto) {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter("SuperAdminProfile.txt", true));
            printWriter.println(dto.writableString());

            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateSuperAdmin(SuperAdminProfileDTO dto) {
        List<SuperAdminProfileDTO> dtoList = getSuperAdminAll();
        dtoList.removeIf(p -> p.getId().equals(dto.getId()));
        dtoList.add(dto);
        rewriteSuperAdminList(dtoList);
    }
    public void removeSuperAdmin(Long id) {
        List<SuperAdminProfileDTO>dtoList = getSuperAdminAll();
        dtoList.removeIf(p -> p.getId().equals(id));
        rewriteSuperAdminList(dtoList);
    }

    public void rewriteSuperAdminList(List<SuperAdminProfileDTO> list) {

        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter("SuperAdminProfile.txt"));
            list.forEach(dto -> {
                printWriter.println(dto.writableString());
            });
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
