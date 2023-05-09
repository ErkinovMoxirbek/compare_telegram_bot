package com.example.util;

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

}
