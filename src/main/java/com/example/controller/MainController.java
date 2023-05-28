package com.example.controller;

import com.example.MyTelegramBot;
import com.example.dto.AdminProfileDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.SuperAdminProfileDTO;
import com.example.enums.ProfileStep;
import com.example.repository.ProfileRepository;
import com.example.service.*;
import com.example.util.ReplyKeyboardUtil;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
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
    private SuperAdminService superAdminService;
    public void handle(String text, Message message) throws MalformedURLException {
        if (text != null){
            if (text.equals("\uD83D\uDDD1 Bekor qilish")){
                ProfileDTO profileDTO = profileRepository.getProfile(message.getChatId());
                profileDTO.setStep(ProfileStep.Done);
                profileRepository.update(profileDTO);
                if ( profileRepository.getAdminProfile(message.getChatId()) != null && !profileRepository.getAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Done) ){
                    profileRepository.removeAdmin(message.getChatId());
                }if ( profileRepository.getSuperAdminProfile(message.getChatId()) != null && !profileRepository.getSuperAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Done) ){
                    profileRepository.removeSuperAdmin(message.getChatId());
                }
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("Bo'limlarni birni tanlang!");
                sendMessage.setChatId(message.getChatId());
                sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuKeyboard());
                myTelegramBot.sendMsg(sendMessage);
                return;
            } else if (text.equals("\uD83D\uDCD1 Yo'riqnoma")){
                profileService.manual(message);
            }
            else if (text.equals("✅ Bloklarning bir biriga mosligini tekshirish")){
                if (profileRepository.getProfile(message.getChatId()) == null) {
                    categoryService.createProfile(message);
                }
                ProfileDTO profileDTO = profileRepository.getProfile(message.getChatId());
                profileDTO.setStep(ProfileStep.Done);
                profileRepository.update(profileDTO);
                comparisonService.internalBlockEnter(message);
            }else if (profileRepository.getAdminProfile(message.getChatId()) != null && profileRepository.getAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Enter_phone) || message.getText().startsWith("+998") ) {
                if ( message.getText().startsWith("+998")){
                    Contact contact = new Contact();
                    contact.setPhoneNumber(text);
                    message.setContact(contact);
                }
                adminService.enterPhone(message);
            }else if (profileRepository.getProfile(message.getChatId()) != null) {
                 if (profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Enter_Internal_Block)||
                        profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Save_Internal_Block)||
                        profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Save_External_Block)) {
                    comparisonService.externalBlockEnter(message);
                }
                else if (profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Enter_External_Block)){
                    comparisonService.externalBlockEnter(message);
                }
            }
            if (profileRepository.getAdminProfile(message.getChatId()) != null ){
                if ( profileRepository.getAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Enter_name) ){
                    adminService.enterName(message);
                }
                else if (profileRepository.getAdminProfile(message.getChatId()) != null && profileRepository.getAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Enter_surname) ) {
                    adminService.enterSurname(message);
                }
            }if (profileRepository.getSuperAdminProfile(message.getChatId()) != null && profileRepository.getAdminProfile(message.getChatId()) == null){
                if ( profileRepository.getSuperAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Enter_password)){
                    superAdminService.checkLoginPassword(message);
                }
                else if (profileRepository.getSuperAdminProfile(message.getChatId()) != null && profileRepository.getSuperAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Enter_login)) {
                    superAdminService.password(message);
                }
            }
        }
        else if (message.hasContact()){
            if (profileRepository.getAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Enter_phone) || message.getText().startsWith("+998") ) {
                adminService.enterPhone(message);
            }
        }else {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Bo'limni tanlang!");
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuKeyboard());
            sendMessage.setChatId(message.getChatId());
            myTelegramBot.sendMsg(sendMessage);
        }
    }
    public void command(Message message) throws MalformedURLException {
        if(message.getText().equals("/admin") ){
                if (profileRepository.getAdminProfile(message.getChatId()) == null ||
                        profileRepository.getAdminProfile(message.getChatId()).getName().equals("null") ||
                        profileRepository.getAdminProfile(message.getChatId()).getSurname().equals("null")  ||
                        profileRepository.getAdminProfile(message.getChatId()).getPhone().equals("null") ) {
                    adminService.create(message);
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
            else if ( profileRepository.getAdminProfile(message.getChatId()) != null && !profileRepository.getAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Done) ){
                AdminProfileDTO adminProfileDTO = profileRepository.getAdminProfile(message.getChatId());
                adminProfileDTO.setStep(ProfileStep.Done);
                profileRepository.updateAdmin(adminProfileDTO);
            }
            ProfileDTO profileDTO = profileRepository.getProfile(message.getChatId());
            profileDTO.setStep(ProfileStep.Done);
            profileRepository.update(profileDTO);
            if (profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Done)) {
                categoryService.helloMenu(message);
            }
        }
        else if (message.getText().equals("/super_admin")) {
            if (profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Done) && profileRepository.getAdminProfile(message.getChatId()) != null && profileRepository.getAdminProfile(message.getChatId()).getVisible() || profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Done) && profileRepository.getAdminProfile(message.getChatId()) == null){
                superAdminService.login(message);
            }
        }
        else {
            handle(message.getText(), message);
        }
    }
}
