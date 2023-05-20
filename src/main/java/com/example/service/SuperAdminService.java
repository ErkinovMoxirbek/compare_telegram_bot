package com.example.service;

import com.example.MyTelegramBot;
import com.example.dto.AdminProfileDTO;
import com.example.dto.ConfidentialityDTO;
import com.example.dto.FileDTO;
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
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
@AllArgsConstructor
public class SuperAdminService {
    private MyTelegramBot myTelegramBot;
    private ProfileRepository profileRepository;
    private ConfidentialityRepository confidentialityRepository;
    private FileHandlerService fileHandlerService;
    public void myAdmins(Message message, String text){
        List<AdminProfileDTO>list = profileRepository.getAdminAll();
        SendMessage sendMessage = new SendMessage();
        int count = 0;
        if (list.size() == 0){
            sendMessage.setText("Sizda adminlar yo'q!");
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuSuperAdmin());
            sendMessage.setChatId(message.getChatId());
            myTelegramBot.sendMsg(sendMessage);
        }else {
            sendMessage.setChatId(message.getChatId());
            for (AdminProfileDTO a : list){
                if (a.getVisible()){
                    count++;
                    sendMessage.setText("Ism: " + a.getName() + "\nFamiliya: " + a.getSurname() + "\nTelefon nomer: " + a.getPhone());
                    sendMessage.setReplyMarkup(InlineKeyBoardUtil.deleteAdmin(a.getId(),message.getMessageId()));
                    myTelegramBot.sendMsg(sendMessage);
                }
            }
            if (count == 0){
                sendMessage.setText("Sizda adminlar yo'q!");
                sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuSuperAdmin());
                myTelegramBot.sendMsg(sendMessage);
            }
        }
    }

    public void deleteAdmins(Message message, String text, String adminId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setMessageId(message.getMessageId());
        deleteMessage.setChatId(message.getChatId());
        myTelegramBot.deleteMsg(deleteMessage);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Bajarildi ✅");
        sendMessage.setChatId(message.getChatId());
        sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuSuperAdmin());
        myTelegramBot.sendMsg(sendMessage);
        sendMessage.setText("Super admin sizni endi admin sifatida endi ko'rmaydi!");
        sendMessage.setChatId(adminId);
        sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuKeyboard());
        myTelegramBot.sendMsg(sendMessage);
        profileRepository.removeAdmin(Long.valueOf(adminId));
    }
    public void settingsMenu(Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setReplyMarkup(ReplyKeyboardUtil.settingsMenu());
        sendMessage.setText("⚙️ Sozlamalar");
        sendMessage.setChatId(message.getChatId());
        myTelegramBot.sendMsg(sendMessage);

    }
    public void infoSuperAdmin(Message message){
        ConfidentialityDTO dto = confidentialityRepository.getConfidentiality("admin");
        SendMessage sendMessage = new SendMessage();
        sendMessage.setReplyMarkup(ReplyKeyboardUtil.settingsMenu());
        sendMessage.setText("Login: " + dto.getLogin() + "\nParol: " + dto.getPassword());
        sendMessage.setChatId(message.getChatId());
        myTelegramBot.sendMsg(sendMessage);
    }
    public void editLogin(Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("Yangi loginni kiriting!");
        myTelegramBot.sendMsg(sendMessage);
        SuperAdminProfileDTO dto = profileRepository.getSuperAdminProfile(message.getChatId());
        dto.setStep(ProfileStep.Edit_Login);
        profileRepository.updateSuperAdmin(dto);
    }
    public void saveLogin(Message message){
        SuperAdminProfileDTO dto = profileRepository.getSuperAdminProfile(message.getChatId());
        ConfidentialityDTO confidentialityDTO = confidentialityRepository.getConfidentiality("admin");
        dto.setStep(ProfileStep.Done);
        confidentialityDTO.setLogin(message.getText());
        confidentialityRepository.update(confidentialityDTO);
        profileRepository.updateSuperAdmin(dto);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("O'zgartirildi ✅");
        sendMessage.setReplyMarkup(ReplyKeyboardUtil.settingsMenu());
        myTelegramBot.sendMsg(sendMessage);
    }
    public void editPassword(Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("Yangi parolni kiriting!");
        myTelegramBot.sendMsg(sendMessage);
        SuperAdminProfileDTO dto = profileRepository.getSuperAdminProfile(message.getChatId());
        dto.setStep(ProfileStep.Edit_Password);
        profileRepository.updateSuperAdmin(dto);
    }
    public void savePassword(Message message){
        SuperAdminProfileDTO dto = profileRepository.getSuperAdminProfile(message.getChatId());
        ConfidentialityDTO confidentialityDTO = confidentialityRepository.getConfidentiality("admin");
        dto.setStep(ProfileStep.Done);
        confidentialityDTO.setPassword(message.getText());
        confidentialityRepository.update(confidentialityDTO);
        profileRepository.updateSuperAdmin(dto);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("O'zgartirildi ✅");
        sendMessage.setReplyMarkup(ReplyKeyboardUtil.settingsMenu());
        myTelegramBot.sendMsg(sendMessage);
    }
    public void checkAdmin(Long adminId,Message message ){
        SendMessage sendMessage = new SendMessage();
        AdminProfileDTO dto = profileRepository.getAdminProfile(adminId);
        if (profileRepository.getAdminProfile(adminId).getVisible()){
            sendMessage.setText("Bu faydalanuvchi boshqa super admin tomonidan tasdiqlanib bol'ingan!");
            sendMessage.setChatId(message.getChatId());
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuSuperAdmin());
            myTelegramBot.sendMsg(sendMessage);
            myTelegramBot.deleteMsg(new DeleteMessage(message.getChatId().toString(),message.getMessageId()));
        }else {
            myTelegramBot.deleteMsg(new DeleteMessage(message.getChatId().toString(),message.getMessageId()));
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
    }
    public void notCheckAdmin(Long adminId ,Message message){
        SendMessage sendMessage = new SendMessage();
        if (profileRepository.getAdminProfile(adminId) == null){
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText("Bu faydalanuvchi boshqa super admin tomonidan bajarilib bol'ingan!");
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuSuperAdmin());
            myTelegramBot.sendMsg(sendMessage);
            myTelegramBot.deleteMsg(new DeleteMessage(message.getChatId().toString(),message.getMessageId()));
        }else {
            myTelegramBot.deleteMsg(new DeleteMessage(message.getChatId().toString(),message.getMessageId()));
            profileRepository.removeAdmin(adminId);
            sendMessage.setChatId(adminId);
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuKeyboard());
            sendMessage.setText("\uD83D\uDE41 Sizni admin sifatida qabul qilmadi!");
            myTelegramBot.sendMsg(sendMessage);
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText("Bajarildi ✅");
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuSuperAdmin());
            myTelegramBot.sendMsg(sendMessage);
        }
    }
}
