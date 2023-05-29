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
                        case 2 -> pcbdto.setSModel(dataFormatter.formatCellValue(cell));
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
                        case 6 -> pcbdto.setModel(dataFormatter.formatCellValue(cell));
                        default -> {
                        }
                    }
                }
                list.add(pcbdto);
            }if (a == 0){
                PCBDTO pcbdto = new PCBDTO();
                for (Cell cell : row) {
                    switch (cell.getColumnIndex()) {
                        case 0 -> pcbdto.setIdName(dataFormatter.formatCellValue(cell));
                        case 1 -> pcbdto.setGroupName(dataFormatter.formatCellValue(cell));
                        case 2 -> pcbdto.setSname(dataFormatter.formatCellValue(cell));
                        case 3 ->{
                            pcbdto.setPCBBoxCodeAssemblyName(dataFormatter.formatCellValue(cell));
                        }
                        case 4 ->{
                            pcbdto.setPCBCodeWholeName(dataFormatter.formatCellValue(cell));
                        }
                        case 5 -> pcbdto.setSAPCodeName(dataFormatter.formatCellValue(cell));
                        case 6 -> pcbdto.setModelName(dataFormatter.formatCellValue(cell));
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
            if ( e.getPCBBoxCode() != null && e.getPCBBoxCode().equalsIgnoreCase(code)){
                return e;
            }
        }
        return null;
    }
    public PCBDTO getInfoExelByPCBModel(String model){
        List<PCBDTO> list = getListExel();
        int a = 0;
        for (PCBDTO e : list){
            if (null == e.getModel() || a==0) {
                a++;
                continue;
            }
            if (e.getModel().toLowerCase().contains(model))return e;
        }
        return null;
    } public PCBDTO getInfoExelByPCBCode(String code){
        List<PCBDTO> list = getListExel();
        for (PCBDTO e : list){
            if (e.getPCBBoxCode() != null && e.getPCBCode().equalsIgnoreCase(code)){
                return e;
            }
        }
        return null;
    }
    public PCBDTO getInfoExelBySAPCode(String code){
        List<PCBDTO> list = getListExel();
        for (PCBDTO e : list){
            if (e.getPCBBoxCode() != null && e.getSAPCode().equalsIgnoreCase(code)){
                return e;
            }
        }
        return null;
    }
}
