package com.example.service;

import com.example.MyTelegramBot;
import com.example.controller.SuperAdminControler;
import com.example.dto.AdminProfileDTO;
import com.example.dto.SuperAdminProfileDTO;
import com.example.enums.ProfileStep;
import com.example.repository.ConfidentialityRepository;
import com.example.repository.ProfileRepository;
import com.example.util.InlineKeyBoardUtil;
import com.example.util.ReplyKeyboardUtil;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.File;

@AllArgsConstructor
public class AdminService {
    private ProfileRepository profileRepository;
    private MyTelegramBot myTelegramBot;
    private ConfidentialityRepository confidentialityRepository;
    private SuperAdminControler superAdminControler;
    private FileHandlerService fileHandlerService;
    public void create(Message message){
        if (profileRepository.getAdminProfile(message.getChatId()) != null){
            profileRepository.removeAdmin(message.getChatId());
        }
        AdminProfileDTO dto = new AdminProfileDTO();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("Siz admin emasiz!");
        sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
        myTelegramBot.sendMsg(sendMessage);
        sendMessage.setText("Admin bo'lishni istasangiz!");
        myTelegramBot.sendMsg(sendMessage);
        sendMessage.setText("So'rovnomani to'ldiring!");
        myTelegramBot.sendMsg(sendMessage);
        sendMessage.setText("Ismingizni kiritng :");
        dto.setId(message.getChatId());
        dto.setStep(ProfileStep.Enter_name);
        profileRepository.saveAdmin(dto);
        myTelegramBot.sendMsg(sendMessage);
    }
    public void enterName(Message message){
        SendMessage sendMessage = new SendMessage();
        AdminProfileDTO dto = profileRepository.getAdminProfile(message.getChatId());
        if (message.getText().matches("^[a-zA-Z]+$")){
            dto.setName(message.getText());
            dto.setStep(ProfileStep.Enter_surname);
            profileRepository.updateAdmin(dto);
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText("Familyangizni kiritng!");
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
            myTelegramBot.sendMsg(sendMessage);
        }else {
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText("Iltimos, ism to'g'ri shaklda kiriting!");
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
            myTelegramBot.sendMsg(sendMessage);
        }

    }
    public void enterSurname(Message message){
        AdminProfileDTO dto = profileRepository.getAdminProfile(message.getChatId());
        SendMessage sendMessage = new SendMessage();
        if (message.getText().matches("^[a-zA-Z]+$")){
            dto.setSurname(message.getText());
            dto.setStep(ProfileStep.Enter_phone);
            profileRepository.updateAdmin(dto);
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText("Telefon raqamingizni jo'nating yoki kiriting!");
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.phoneKeyboard());
            myTelegramBot.sendMsg(sendMessage);
        }else {
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText("Iltimos, familiyani to'g'ri shaklda kiriting!");
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
            myTelegramBot.sendMsg(sendMessage);
        }
    }
    public void enterPhone(Message message){
        Contact contact = message.getContact();
        AdminProfileDTO dto = profileRepository.getAdminProfile(message.getChatId());
        dto.setPhone(contact.getPhoneNumber());
        dto.setStep(ProfileStep.Done);
        profileRepository.updateAdmin(dto);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("So'rovnoma tugadi!\nEndi siz super admin javobini kuting!\n\n /start bosing!");
        myTelegramBot.sendMsg(sendMessage);
        sendMessage.setReplyMarkup(InlineKeyBoardUtil.getCheck(message.getChatId(),message.getMessageId()));
        sendMessage.setText(dto.getName() + " " + dto.getSurname() + ";\n" + "Telefon nomeri: " + dto.getPhone() + ";\nFoydalanuvchi adminlikni talab qilmoqda!\nQabul qilasizmi?" );
        for(SuperAdminProfileDTO d : profileRepository.getSuperAdminAll()){
            if (d.getVisible()){
                sendMessage.setChatId(d.getId());
                myTelegramBot.sendMsg(sendMessage);
            }
        }
    }
}
