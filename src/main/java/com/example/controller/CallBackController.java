package com.example.controller;

import com.example.service.AdminService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;
@AllArgsConstructor
public class CallBackController {
    private AdminService adminService;
    public void handle(Message message){
        if (message.getText().startsWith("Check/")){
            String [] arr = message.getText().split("/");
            adminService.checkAdmin(Long.valueOf(arr[1]),message);
        } else if (message.getText().startsWith("NotCheck/")) {
            String [] arr = message.getText().split("/");
            adminService.notCheckAdmin(Long.valueOf(arr[1]),message);
        }
    }
}
