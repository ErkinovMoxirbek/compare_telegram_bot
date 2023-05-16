package com.example.controller;

import com.example.dto.ProfileDTO;
import com.example.enums.ProfileStep;
import com.example.repository.ProfileRepository;
import com.example.service.AdminService;
import com.example.service.FileHandlerService;
import com.example.service.SuperAdminService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;
@AllArgsConstructor
public class CallBackController {
    private SuperAdminService superAdminService;
    private AdminService adminService;
    private ProfileRepository profileRepository;
    private FileHandlerService fileHandlerService;

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
        } else if (text.startsWith("File:")) {
            String [] arr = text.split(":");
            ProfileDTO profile = profileRepository.getProfile(message.getChatId());
            profile.setNowPath(arr[1]);
            profileRepository.update(profile);
            superAdminService.sendFile(arr[1],message);
        } else if (text.startsWith("Back:")) {
            String [] arr = text.split(":");
            if (profileRepository.getSuperAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Download_file)){
                superAdminService.getEditFile(message,arr[1]);
            } else if (profileRepository.getSuperAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Save_file)) {
                fileHandlerService.getEditFile(message,arr[1]);
            }
        }
    }
}
