package com.example.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.LinkedList;
import java.util.List;

public class ReplyKeyboardUtil {
    public static KeyboardButton button(String text) {
        KeyboardButton button = new KeyboardButton();
        button.setText(text);
        return button;
    }
    public static ReplyKeyboardMarkup menuKeyboard() {
        KeyboardButton order = button("✅ Bloklarning bir biriga mosligini tekshirish");
        KeyboardButton Suggestions = button("\uD83D\uDCD1 Yo'riqnoma");
        KeyboardRow row1 = new KeyboardRow();
        row1.add(order);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(Suggestions);
        List<KeyboardRow> rowList = new LinkedList<>();
        rowList.add(row1);
        rowList.add(row2);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rowList);
        replyKeyboardMarkup.setResizeKeyboard(true);//buttonni razmerini to'g'irlaydi
        replyKeyboardMarkup.setSelective(true);// bottinga strelka qoshadi;
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }
    public static ReplyKeyboardMarkup menuAdmin() {
        KeyboardButton order = button("\uD83D\uDC64 Adminlarim");
        KeyboardButton Suggestions = button("yana nimadir");
        KeyboardRow row1 = new KeyboardRow();
        row1.add(order);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(Suggestions);
        List<KeyboardRow> rowList = new LinkedList<>();
        rowList.add(row1);
        rowList.add(row2);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rowList);
        replyKeyboardMarkup.setResizeKeyboard(true);//buttonni razmerini to'g'irlaydi
        replyKeyboardMarkup.setSelective(true);// bottinga strelka qoshadi;
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }
    public static ReplyKeyboardMarkup menuKeyboard2() {
        KeyboardButton order = button("✅ Bloklarning bir biriga mosligini tekshirish");
        KeyboardRow row1 = new KeyboardRow();
        row1.add(order);
        List<KeyboardRow> rowList = new LinkedList<>();
        rowList.add(row1);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rowList);
        replyKeyboardMarkup.setResizeKeyboard(true);//buttonni razmerini to'g'irlaydi
        replyKeyboardMarkup.setSelective(true);// bottinga strelka qoshadi;
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboard cancellation() {
        KeyboardButton order = button("\uD83D\uDDD1 Bekor qilish");
        KeyboardRow row1 = new KeyboardRow();
        row1.add(order);
        List<KeyboardRow> rowList = new LinkedList<>();
        rowList.add(row1);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rowList);
        replyKeyboardMarkup.setResizeKeyboard(true);//buttonni razmerini to'g'irlaydi
        replyKeyboardMarkup.setSelective(true);// bottinga strelka qoshadi;
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }
    public static ReplyKeyboardMarkup phoneKeyboard() {
        KeyboardButton button = new KeyboardButton();
        button.setText(" ☎️ Kontact jo'natish");
        button.setRequestContact(true);

        KeyboardRow row = new KeyboardRow();
        row.add(button);

        List<KeyboardRow> rowList = new LinkedList<>();
        rowList.add(row);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rowList);

        replyKeyboardMarkup.setResizeKeyboard(true);//buttonni razmerini to'g'irlaydi
        replyKeyboardMarkup.setSelective(true);// bottinga strelka qoshadi;
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        return replyKeyboardMarkup;
    }
}
