package com.example.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.awt.*;
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
        KeyboardButton order = button("✅ Bloklarning bir biriga mosligini tekshirish");
        KeyboardButton order2 = button("\uD83D\uDC64 Adminlarim");
        KeyboardButton order3 = button("⚙️ Sozlamalar");
        KeyboardRow row = new KeyboardRow();
        row.add(order);
        KeyboardRow row1 = new KeyboardRow();
        row1.add(order2);
        row1.add(order3);
        List<KeyboardRow> rowList = new LinkedList<>();
        rowList.add(row);
        rowList.add(row1);
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
    public static ReplyKeyboardMarkup settingsMenu() {
        KeyboardButton order = button("\uD83D\uDDC2 Ma'lumotlar");
        KeyboardButton order2 = button("✏️ Loginni o'zgartirish");
        KeyboardButton order3 = button("✏️ Parolni o'zgartirish");
        KeyboardButton order4 = button("\uD83D\uDDD1 Bekor qilish");
        KeyboardRow row = new KeyboardRow();
        row.add(order);
        KeyboardRow row1 = new KeyboardRow();
        row1.add(order2);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(order3);
        KeyboardRow row4 = new KeyboardRow();
        row4.add(order4);
        List<KeyboardRow> rowList = new LinkedList<>();
        rowList.add(row);
        rowList.add(row1);
        rowList.add(row2);
        rowList.add(row4);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rowList);
        replyKeyboardMarkup.setResizeKeyboard(true);//buttonni razmerini to'g'irlaydi
        replyKeyboardMarkup.setSelective(true);// bottinga strelka qoshadi;
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }
}
