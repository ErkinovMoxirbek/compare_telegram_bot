package com.example.repository;

import com.example.entity.ExternalBlockEntity;
import com.example.entity.ProfileEntity;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
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
    public List<ExternalBlockEntity> getListExel(){
        // faylni yuklash
        File file = new File("TashqiBlok.xlsx");
        FileInputStream inputStream = null;
        Workbook workbook = null;
        try {
            inputStream = new FileInputStream(file);
            workbook = WorkbookFactory.create(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // fayl o'qish uchun mo'ljal
        Sheet sheet = workbook.getSheetAt(0);
        int b = 0;
        int c = 0;
        DataFormatter dataFormatter = new DataFormatter();
        List<ExternalBlockEntity> list = new LinkedList<>();
        ExternalBlockEntity entity = new ExternalBlockEntity();
        // har bir qator bo'yicha ma'lumotlarni o'qish
        for (Row row : sheet) {
            c += 2;
            // har bir qatori ustida yurish
            for (Cell cell : row) {
                b++;
                // cell-ni bo'sh qolishi mumkin
                if (cell.getCellType() == CellType.BLANK) {
                    System.out.print(" ");
                }else if (b % 2 == 1){
                    entity.setBlockSeriya(dataFormatter.formatCellValue(cell));
                } else if (b % 2 == 0) {
                    entity.setNumber(Integer.valueOf(dataFormatter.formatCellValue(cell)));
                }
                if (b == c){
                    list.add(entity);
                    entity = new ExternalBlockEntity();
                    break;
                }
            }
        }
        return list;
    }
    public ExternalBlockEntity getInfoExel(String seriya){
        List<ExternalBlockEntity> list = getListExel();
        for (ExternalBlockEntity e : list){
            if (e.getBlockSeriya().equals(seriya)){
                return e;
            }
        }
        return null;
    }
}
