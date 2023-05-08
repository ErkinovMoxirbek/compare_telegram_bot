package com.example.controller;

import com.example.MyTelegramBot;
import com.example.service.AdminService;
import com.example.util.ReplyKeyboardUtil;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
@AllArgsConstructor
public class CallBackController {
    private AdminService adminService;
    private MyTelegramBot myTelegramBot;
    public void handle(String text,Message message){
        if (text.startsWith("Check/")){
            String [] arr = text.split("/");
            adminService.checkAdmin(Long.valueOf(arr[1]),message);
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setText("Bajarildi ✅");
            editMessageText.setChatId(message.getChatId());
            editMessageText.setMessageId(message.getMessageId());
            myTelegramBot.sendMsg(editMessageText);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("\uD83C\uDFE0 Bosh menyu");
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuAdmin());
            sendMessage.setChatId(message.getChatId());
            myTelegramBot.sendMsg(sendMessage);
        } else if (text.startsWith("NotCheck/")) {
            String [] arr = text.split("/");
            adminService.notCheckAdmin(Long.valueOf(arr[1]),message);
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setText("Bajarildi ✅");
            editMessageText.setChatId(message.getChatId());
            editMessageText.setMessageId(message.getMessageId());
            myTelegramBot.sendMsg(editMessageText);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuAdmin());
            sendMessage.setChatId(message.getChatId());
            myTelegramBot.sendMsg(sendMessage);
        }
    }
}
