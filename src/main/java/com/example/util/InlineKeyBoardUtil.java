package com.example.util;

import com.example.dto.FileDTO;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.LinkedList;
import java.util.List;

public class InlineKeyBoardUtil {
    public static InlineKeyboardButton button(String text, String callBack) {
        InlineKeyboardButton button = new InlineKeyboardButton(text);
        button.setCallbackData(callBack);
        return button;
    }

    public static InlineKeyboardMarkup getCheck(Long id,Integer messageId ) {

        List<InlineKeyboardButton> row = new LinkedList<>();
        InlineKeyboardButton button1 = InlineKeyBoardUtil.button("✅ Ha, qabul qilaman", "Check/" + id + "/" + messageId);
        List<InlineKeyboardButton> row2 = new LinkedList<>();
        InlineKeyboardButton button2 = InlineKeyBoardUtil.button("❌ Yo'q, qabul qilmayman", "NotCheck/" + id + "/" + messageId );

        row.add(button1);
        row2.add(button2);



        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new LinkedList<>();
        rowList.add(row);
        rowList.add(row2);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
    public static InlineKeyboardMarkup deleteAdmin(Long id,Integer messageId ) {
        List<InlineKeyboardButton> row = new LinkedList<>();
        InlineKeyboardButton button1 = InlineKeyBoardUtil.button("\uD83D\uDDD1 Olib tashlash", "delete/" + id + "/" + messageId);
        row.add(button1);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new LinkedList<>();
        rowList.add(row);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
    public static InlineKeyboardMarkup getFile(List<FileDTO> fileList,String path) {
        List<List<InlineKeyboardButton>> rowList = new LinkedList<>();
        List<InlineKeyboardButton> row ;
        for(FileDTO dto : fileList){
            row = new LinkedList<>();
            dto.setPath(dto.getPath().replace("\\","/"));
            InlineKeyboardButton button1 = InlineKeyBoardUtil.button(dto.getName() ,"File:" + dto.getPath());
            row.add(button1);
            rowList.add(row);
        }
        String [] arr = path.split("/");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        System.out.println(path);
        if (arr.length >= 2){
            System.out.println("button back added");
            row = new LinkedList<>();
            int index = path.lastIndexOf('/');
            String newPath = path.substring(0,index);
            InlineKeyboardButton button = InlineKeyBoardUtil.button("\uD83D\uDD19 Orqaga","Back:" + newPath);
            row.add(button);
            rowList.add(row);
        }
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

}
