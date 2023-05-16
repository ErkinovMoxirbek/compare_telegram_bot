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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
@AllArgsConstructor
public class AdminService {
    private ProfileRepository profileRepository;
    private MyTelegramBot myTelegramBot;
    private ConfidentialityRepository confidentialityRepository;
    private SuperAdminControler superAdminControler;
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
        AdminProfileDTO dto = profileRepository.getAdminProfile(message.getChatId());
        dto.setName(message.getText());
        dto.setStep(ProfileStep.Enter_surname);
        profileRepository.updateAdmin(dto);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("Familyangizni kiritng!");
        sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
        myTelegramBot.sendMsg(sendMessage);
    }
    public void enterSurname(Message message){
        AdminProfileDTO dto = profileRepository.getAdminProfile(message.getChatId());
        dto.setSurname(message.getText());
        dto.setStep(ProfileStep.Enter_phone);
        profileRepository.updateAdmin(dto);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("Telefon raqamingizni jo'nating!");
        sendMessage.setReplyMarkup(ReplyKeyboardUtil.phoneKeyboard());
        myTelegramBot.sendMsg(sendMessage);
    }
    public void enterPhone(Message message){
        Contact contact = message.getContact();
        AdminProfileDTO dto = profileRepository.getAdminProfile(message.getChatId());
        dto.setPhone(contact.getPhoneNumber());
        dto.setStep(ProfileStep.Done);
        profileRepository.updateAdmin(dto);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("Sorovnoma tugadi!\nEndi siz super admin javobini kuting!\n\n /start bosing!");
        myTelegramBot.sendMsg(sendMessage);
        sendMessage.setReplyMarkup(InlineKeyBoardUtil.getCheck(message.getChatId(),message.getMessageId()));
        sendMessage.setText(dto.getName() + " " + dto.getSurname() + ";\n" + "Telefon nomeri: " + dto.getPhone() + ";\nFoydalanuvchi adminlikni talab qilmoqda!\nQabul qilasizmi?" );
        for(SuperAdminProfileDTO d : profileRepository.getSuperAdminAll()){
            sendMessage.setChatId(d.getId());
            myTelegramBot.sendMsg(sendMessage);
        }
    }
    public void checkAdmin(Long adminId,Message message ){
        SendMessage sendMessage = new SendMessage();
        AdminProfileDTO dto = profileRepository.getAdminProfile(adminId);
        if (profileRepository.getAdminProfile(adminId).getVisible()){
            sendMessage.setText("Bu faydalanuvchi boshqa super admin tomonidan tasdiqlanib bol'ingan!");
            sendMessage.setChatId(message.getChatId());
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuSuperAdmin());
            myTelegramBot.sendMsg(sendMessage);
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(adminId);
            deleteMessage.setMessageId(message.getMessageId());
            myTelegramBot.deleteMsg(deleteMessage);
        }
        dto.setVisible(Boolean.TRUE);
        profileRepository.updateAdmin(dto);
        sendMessage.setChatId(adminId);
        sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuAdmin());
        sendMessage.setText("\uD83E\uDD73 Siz admin sifatida qabul qildingiz!");
        myTelegramBot.sendMsg(sendMessage);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("Bajarildi ✅");
        sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuSuperAdmin());
        myTelegramBot.sendMsg(sendMessage);
    }
    public void notCheckAdmin(Long adminId ,Message message){
        profileRepository.removeAdmin(adminId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(adminId);
        sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuKeyboard());
        sendMessage.setText("\uD83D\uDE41 Sizni admin sifatida qabul qilmadi!");
        myTelegramBot.sendMsg(sendMessage);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("Bajarildi ✅");
        sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuSuperAdmin());
        myTelegramBot.sendMsg(sendMessage);
    }
    public void login(Message message){
        SendMessage sendMessage = new SendMessage();
        if (profileRepository.getSuperAdminProfile(message.getChatId()) != null && profileRepository.getSuperAdminProfile(message.getChatId()).getVisible()){
            message.setText("/start");
            superAdminControler.handle(message);
        }
        sendMessage.setText("Loginni kiriting!");
        sendMessage.setChatId(message.getChatId());
        sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
        myTelegramBot.sendMsg(sendMessage);
        if (profileRepository.getSuperAdminProfile(message.getChatId()) != null){
            profileRepository.removeSuperAdmin(message.getChatId());
        }
        SuperAdminProfileDTO dto = new SuperAdminProfileDTO();
        dto.setId(message.getChatId());
        dto.setStep(ProfileStep.Enter_login);
        profileRepository.saveSuperAdmin(dto);
    }
    public void password(Message message){
        SuperAdminProfileDTO dto = profileRepository.getSuperAdminProfile(message.getChatId());
        if (!confidentialityRepository.getConfidentiality("admin").getLogin().equals(message.getText())){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Xatto! Qayta urinig!");
            sendMessage.setChatId(message.getChatId());
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
            myTelegramBot.sendMsg(sendMessage);
        }else {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Parolni kiriting!");
            sendMessage.setChatId(message.getChatId());
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
            myTelegramBot.sendMsg(sendMessage);
            dto.setStep(ProfileStep.Enter_password);
            profileRepository.updateSuperAdmin(dto);
        }
    }
    public void checkLoginPassword(Message message){
        SuperAdminProfileDTO dto = profileRepository.getSuperAdminProfile(message.getChatId());
        if (!confidentialityRepository.getConfidentiality("admin").getPassword().equals(message.getText())){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Xatto! Qayta urinig!");
            sendMessage.setChatId(message.getChatId());
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
            myTelegramBot.sendMsg(sendMessage);
        }else {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Muvaffaqiyatli!");
            sendMessage.setChatId(message.getChatId());
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuSuperAdmin());
            myTelegramBot.sendMsg(sendMessage);
            dto.setStep(ProfileStep.Done);
            dto.setVisible(Boolean.TRUE);
            profileRepository.updateSuperAdmin(dto);
        }
    }
}
