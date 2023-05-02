package com.example.repository;

import com.example.dto.ExternalBlockDTO;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.*;

public class ExternalBlockRepository {
/*

 /// with text file ///

 public ExternalBlockDTO get(String seriya) {
        Optional<ExternalBlockDTO> optional = getAll().stream()
                .filter(p -> p.getBlockSeriya().equals(seriya))
                .findFirst();

        return optional.orElse(null);
    }
    public List<ExternalBlockDTO> getAll() {
        try {
            Stream<String> lines = Files.lines(Path.of("ExternalBlock.txt"));
            return lines.map(s -> {
                String[] arr = s.split("#");
                ExternalBlockDTO internalBlock = new ExternalBlockDTO();
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

    public void save(ExternalBlockDTO entity) {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter("ExternalBlock.txt", true));
            printWriter.println(entity.writableString());

            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void update(ExternalBlockDTO profileEntity) {
        List<ExternalBlockDTO> profileEntityList = getAll();
        profileEntityList.removeIf(p -> p.getId().equals(profileEntity.getId()));
        profileEntityList.add(profileEntity);
        rewriteList(profileEntityList);
    }
    public void remove(Integer id) {
        List<ExternalBlockDTO> profileEntityList = getAll();
        profileEntityList.removeIf(p -> p.getId().equals(id));
        rewriteList(profileEntityList);
    }

    public void rewriteList(List<ExternalBlockDTO> list) {

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
    public void rewrite(Integer id,ExternalBlockDTO entity){
        List<ExternalBlockDTO> temp = getAll();
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).getId().equals(id)){
                temp.set(i,entity);
            }
        }
        rewriteList(temp);
    }*/
    public List<ExternalBlockDTO> getListExel(){
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
        List<ExternalBlockDTO> list = new LinkedList<>();
        ExternalBlockDTO entity = new ExternalBlockDTO();
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
                    entity = new ExternalBlockDTO();
                    break;
                }
            }
        }
        return list;
    }
    public ExternalBlockDTO getInfoExel(String seriya){
        List<ExternalBlockDTO> list = getListExel();
        for (ExternalBlockDTO e : list){
            if (e.getBlockSeriya().equals(seriya)){
                return e;
            }
        }
        return null;
    }
}
