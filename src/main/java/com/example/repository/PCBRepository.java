package com.example.repository;

import com.example.dto.ExternalBlockDTO;
import com.example.dto.PCBDTO;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class PCBRepository {
    public List<PCBDTO> getListExel(){
        // faylni yuklash
        File file = new File("PCB_Information.xlsx");
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
        List<PCBDTO> list = new LinkedList<>();
        // har bir qator bo'yicha ma'lumotlarni o'qish
        for (Row row : sheet) {
            if(a > 0){
                PCBDTO pcbdto = new PCBDTO();
                for (Cell cell : row) {
                    switch (cell.getColumnIndex()) {
                        case 0 -> pcbdto.setId(dataFormatter.formatCellValue(cell));
                        case 1 -> pcbdto.setGroup(dataFormatter.formatCellValue(cell));
                        case 2 -> pcbdto.setName(dataFormatter.formatCellValue(cell));
                        case 3 ->{
                            pcbdto.setPCBBoxCodeAssembly(dataFormatter.formatCellValue(cell));
                            if (dataFormatter.formatCellValue(cell).length() > 13){
                                pcbdto.setPCBBoxCode(dataFormatter.formatCellValue(cell).substring(0,14));
                            }else {
                                pcbdto.setPCBBoxCode(" ");
                            }
                        }
                        case 4 ->{
                            pcbdto.setPCBCodeWhole(dataFormatter.formatCellValue(cell));
                            if (dataFormatter.formatCellValue(cell).length() > 13){
                                pcbdto.setPCBCode(dataFormatter.formatCellValue(cell).substring(0,14));
                            }else {
                                pcbdto.setPCBCode(" ");
                            }
                        }
                        case 5 -> pcbdto.setSAPCode(dataFormatter.formatCellValue(cell));
                        //case 6 -> pcbdto.setModel(dataFormatter.formatCellValue(cell));
                        default -> {
                        }
                    }
                }
                list.add(pcbdto);
            }
            a++;
        }
        return list;
    }
    public PCBDTO getInfoExelByPCBBoxCode(String code){
        List<PCBDTO> list = getListExel();
        for (PCBDTO e : list){
            if (e.getPCBBoxCode().equals(code)){
                return e;
            }
        }
        return null;
    } public PCBDTO getInfoExelByPCBCode(String code){
        List<PCBDTO> list = getListExel();
        for (PCBDTO e : list){
            if (e.getPCBCode().equals(code)){
                return e;
            }
        }
        return null;
    }
    public PCBDTO getInfoExelBySAPCode(String code){
        List<PCBDTO> list = getListExel();
        for (PCBDTO e : list){
            if (e.getSAPCode().equals(code)){
                return e;
            }
        }
        return null;
    }
}
