package com.example.repository;

import com.example.dto.ErrorDTO;
import com.example.dto.PCBDTO;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ErrorRepository {
    public List<ErrorDTO> getListExel(){
        // faylni yuklash
        File file = new File("xatolik_kodlari.xlsx");
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
        int a = 0;
        DataFormatter dataFormatter = new DataFormatter();
        List<ErrorDTO> list = new LinkedList<>();
        // har bir qator bo'yicha ma'lumotlarni o'qish
        for (Row row : sheet) {
            if(a > 0){
                ErrorDTO dto = new ErrorDTO();
                for (Cell cell : row) {
                    switch (cell.getColumnIndex()) {
                        case 0 -> dto.setErrorCode(dataFormatter.formatCellValue(cell));
                        case 1 -> dto.setKondisanerModel(dataFormatter.formatCellValue(cell));
                        case 2 -> dto.setErrorInfo(dataFormatter.formatCellValue(cell));
                        case 3 -> dto.setCorrectionSequence(dataFormatter.formatCellValue(cell));
                        default -> {
                        }
                    }
                }
                list.add(dto);
            }
            a++;
        }
        return list;
    }
    public ErrorDTO getInfoExelByCode(String code){
        List<ErrorDTO> list = getListExel();
        for (ErrorDTO e : list){
            if (e.getErrorCode().equalsIgnoreCase(code)){
                return e;
            }
        }
        return null;
    }
}
