package com.example.repository;

import com.example.dto.InternalBlockDTO;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
public class InternalBlockRepository {
 /*   ///  text file ///

 public InternalBlockDTO get(String seriya) {
        Optional<InternalBlockDTO> optional = getAll().stream()
                .filter(p -> p.getBlockSeriya().equals(seriya))
                .findFirst();

        return optional.orElse(null);
    }
    public List<InternalBlockDTO> getAll() {
        try {
            Stream<String> lines = Files.lines(Path.of("InternalBlock.txt"));
            return lines.map(s -> {
                String[] arr = s.split("#");
                InternalBlockDTO internalBlock = new InternalBlockDTO();
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

    public void save(InternalBlockDTO entity) {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter("InternalBlock.txt", true));
            printWriter.println(entity.writableString());

            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void update(InternalBlockDTO profileEntity) {
        List<InternalBlockDTO> profileEntityList = getAll();
        profileEntityList.removeIf(p -> p.getId().equals(profileEntity.getId()));
        profileEntityList.add(profileEntity);
        rewriteList(profileEntityList);
    }
    public void remove(Long id) {
        List<InternalBlockDTO> profileEntityList = getAll();
        profileEntityList.removeIf(p -> p.getId().equals(id));
        rewriteList(profileEntityList);
    }

    public void rewriteList(List<InternalBlockDTO> list) {

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
    public void rewrite(Long id,InternalBlockDTO entity){
        List<InternalBlockDTO> temp = getAll();
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).getId().equals(id)){
                temp.set(i,entity);
            }
        }
        rewriteList(temp);
    }*/
    public List<InternalBlockDTO> getListExel(){
        // faylni yuklash
        File file = new File("IchkiBloklar.xlsx");
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
        List<InternalBlockDTO> list = new LinkedList<>();
        InternalBlockDTO entity = new InternalBlockDTO();
        for (Row row : sheet) {
            c += 2;
            for (Cell cell : row) {
                b++;
                if (cell.getCellType() == CellType.BLANK) {
                    System.out.print(" ");
                }else if (b % 2 == 1){
                    entity.setBlockSeriya(dataFormatter.formatCellValue(cell));
                } else if (b % 2 == 0) {
                    entity.setNumber(dataFormatter.formatCellValue(cell));
                }
                if (b == c){
                    list.add(entity);
                    entity = new InternalBlockDTO();
                    break;
                }
            }
        }
        return list;
    }
    public InternalBlockDTO getInfoExel(String seriya){
        List<InternalBlockDTO> list = getListExel();
        for (InternalBlockDTO e : list){
            if (e.getBlockSeriya().toLowerCase().equals(seriya)){
                return e;
            }
        }
        return null;
    }
}
