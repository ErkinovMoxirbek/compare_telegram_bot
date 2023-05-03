package com.example.repository;

import com.example.dto.ExternalBlockDTO;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.*;

public class ExternalBlockRepository {

    public List<ExternalBlockDTO> getListExel(){
        // faylni yuklash
        File file = new File("TashqiBloklar.xlsx");
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
                    entity.setNumber(dataFormatter.formatCellValue(cell));
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
