package com.example.controller;

import com.example.MyTelegramBot;
import com.example.service.AdminService;
import com.example.service.SuperAdminService;
import com.example.util.ReplyKeyboardUtil;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
@AllArgsConstructor
public class CallBackController {
    private SuperAdminService superAdminService;
    private AdminService adminService;
    private MyTelegramBot myTelegramBot;

    public void handle(String text,Message message){
        if (text.startsWith("Check/")){
            String [] arr = text.split("/");
            adminService.checkAdmin(Long.valueOf(arr[1]),message);
        } else if (text.startsWith("NotCheck/")) {
            String [] arr = text.split("/");
            adminService.notCheckAdmin(Long.valueOf(arr[1]),message);
        }else if (text.startsWith("delete/")){
            String [] arr = text.split("/");
            superAdminService.deleteAdmins(message,message.getText(),arr[1]);
        }
    }
}
