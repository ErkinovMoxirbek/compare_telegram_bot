package com.example.repository;

import com.example.entity.InternalBlockEntity;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
public class InternalBlockRepository {
 /*   ///  text file ///

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
    }*/
    public List<InternalBlockEntity> getListExel(){
        // faylni yuklash
        File file = new File("IchkiBlok.xlsx");
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
        List<InternalBlockEntity> list = new LinkedList<>();
        InternalBlockEntity entity = new InternalBlockEntity();
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
                    entity = new InternalBlockEntity();
                    break;
                }
            }
        }
        return list;
    }
    public InternalBlockEntity getInfoExel(String seriya){
        List<InternalBlockEntity> list = getListExel();
        for (InternalBlockEntity e : list){
            if (e.getBlockSeriya().equals(seriya)){
                return e;
            }
        }
        return null;
    }
}
