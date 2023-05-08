package com.example.controller;

import com.example.MyTelegramBot;
import com.example.dto.AdminProfileDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.SuperAdminProfileDTO;
import com.example.enums.ProfileStep;
import com.example.repository.ProfileRepository;
import com.example.service.CategoryService;
import com.example.service.ComparisonService;
import com.example.service.ProfileService;
import com.example.service.AdminService;
import com.example.util.ReplyKeyboardUtil;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.net.MalformedURLException;
@AllArgsConstructor
public class MainController {
    private ProfileRepository profileRepository;
    private ProfileService profileService;
    private ComparisonService comparisonService;
    private MyTelegramBot myTelegramBot;
    private AdminService adminService;
    private CategoryService categoryService;
    public void handle(String text, Message message) throws MalformedURLException {
        if (text != null){
            if (text.equals("\uD83D\uDDD1 Bekor qilish")){
                ProfileDTO profileDTO = profileRepository.getProfile(message.getChatId());
                profileDTO.setStep(ProfileStep.Done);
                profileRepository.update(profileDTO);
                if ( profileRepository.getAdminProfile(message.getChatId()) != null && !profileRepository.getAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Done) ){
                    AdminProfileDTO adminProfileDTO = profileRepository.getAdminProfile(message.getChatId());
                    adminProfileDTO.setStep(ProfileStep.Done);
                    profileRepository.updateAdmin(adminProfileDTO);
                } if (profileRepository.getSuperAdminProfile(message.getChatId()) != null &&
                        !profileRepository.getSuperAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Done)){
                    SuperAdminProfileDTO dto = profileRepository.getSuperAdminProfile(message.getChatId());
                    dto.setId(1L);
                    dto.setStep(ProfileStep.Done);
                    profileRepository.updateSuperAdmin(dto);
                }
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("Bo'limlarni birni tanlang!");
                sendMessage.setChatId(message.getChatId());
                sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuKeyboard());
                myTelegramBot.sendMsg(sendMessage);
                return;
            } else if (text.startsWith("/")) {
                command(message);
                return;
            } else if (text.equals("\uD83D\uDCD1 Yo'riqnoma")){
                profileService.manual(message);
            }
            else if (text.equals("✅ Bloklarning bir biriga mosligini tekshirish")){
                ProfileDTO profileDTO = profileRepository.getProfile(message.getChatId());
                profileDTO.setStep(ProfileStep.Done);
                profileRepository.update(profileDTO);
                comparisonService.internalBlockEnter(message);
            } else if (profileRepository.getProfile(message.getChatId()) != null) {
                 if (profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Enter_Internal_Block)||
                        profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Save_Internal_Block)||
                        profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Save_External_Block)) {
                    comparisonService.externalBlockEnter(message);
                }
                else if (profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Enter_External_Block)){
                    comparisonService.externalBlockEnter(message);
                }
            }

            if ( profileRepository.getAdminProfile(message.getChatId()) != null){
                if ( profileRepository.getAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Enter_name) ){
                    adminService.enterName(message);
                }
                else if (profileRepository.getAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Enter_surname) ) {
                    adminService.enterSurname(message);
                }
            }if (profileRepository.getSuperAdminProfile(message.getChatId()) != null){
                if (profileRepository.getSuperAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Enter_login)) {
                    adminService.password(message);
                }
                else if (profileRepository.getSuperAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Enter_password)){
                    adminService.checkLoginPassword(message);
                }
            }
        }
        else if (message.hasContact()){
            if (profileRepository.getAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Enter_phone) ) {
                adminService.enterPhone(message);
            }
        }
         else {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Bo'limni tanlang!");
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuKeyboard());
            sendMessage.setChatId(message.getChatId());
            myTelegramBot.sendMsg(sendMessage);
        }
    }
    public void command(Message message){
        if(message.getText().equals("/admin")){
            if (profileRepository.getAdminProfile(message.getChatId()) == null ||
                    profileRepository.getAdminProfile(message.getChatId()).getName().equals("null") ||
                    profileRepository.getAdminProfile(message.getChatId()).getSurname().equals("null")  ||
                    profileRepository.getAdminProfile(message.getChatId()).getPhone().equals("null") ){
                ProfileDTO dto = profileRepository.getProfile(message.getChatId());
                dto.setStep(ProfileStep.Done);
                profileRepository.update(dto);
                adminService.create(message);
            }else if (profileRepository.getAdminProfile(message.getChatId()) != null && profileRepository.getAdminProfile(message.getChatId()).getVisible()){

            }else{
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("So'rovnoma super adminga yuborilgan iltimos kuting!");
                sendMessage.setChatId(message.getChatId());
                sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuKeyboard());
                myTelegramBot.sendMsg(sendMessage);
            }
        }
        else if (message.getText().equals("/start") ) {
            if (profileRepository.getProfile(message.getChatId()) == null) {
                categoryService.createProfile(message);
            }
            if (!profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Done)  ) {
                if ( profileRepository.getAdminProfile(message.getChatId()) != null && !profileRepository.getAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Done) ){
                    AdminProfileDTO adminProfileDTO = profileRepository.getAdminProfile(message.getChatId());
                    adminProfileDTO.setStep(ProfileStep.Done);
                    profileRepository.updateAdmin(adminProfileDTO);
                }
                ProfileDTO profileDTO = profileRepository.getProfile(message.getChatId());
                profileDTO.setStep(ProfileStep.Done);
                profileRepository.update(profileDTO);

            }
            if (profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Done)) {
                categoryService.helloMenu(message);
            }
        }
        else if (message.getText().equals("/super_admin")) {
            adminService.login(message);
        }
    }
}
