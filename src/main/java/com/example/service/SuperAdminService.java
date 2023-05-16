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
        if (list.size() == 0){
            sendMessage.setText("Sizda adminlar yo'q!");
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuSuperAdmin());
            sendMessage.setChatId(message.getChatId());
            myTelegramBot.sendMsg(sendMessage);
        }else {
            sendMessage.setChatId(message.getChatId());
            for (AdminProfileDTO a : list){
                sendMessage.setText("Ism: " + a.getName() + "\nFamiliya: " + a.getSurname() + "\nTelefon nomer: " + a.getPhone());
                sendMessage.setReplyMarkup(InlineKeyBoardUtil.deleteAdmin(a.getId(),message.getMessageId()));
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
    public void getFile(Message message){
        String directoryPath = "Base"; // Papka yo'lini o'zgartiring
        File directory = new File(directoryPath);
        List<FileDTO>list = new LinkedList<>();
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    FileDTO dto = new FileDTO();
                    dto.setName(file.getName());
                    dto.setPath(file.getPath());
                    list.add(dto);
                }
            }
        }if (list.size() != 0){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText("FAYLLAR");
            sendMessage.setReplyMarkup(InlineKeyBoardUtil.getFile(list,list.get(0).getPath()));
            myTelegramBot.sendMsg(sendMessage);
        }
    }
    public void getEditFile(Message message,String path){
        System.out.println(path);
        File directory = new File(path);
        List<FileDTO>list = new LinkedList<>();
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    FileDTO dto = new FileDTO();
                    dto.setName(file.getName());
                    dto.setPath(file.getPath());
                    list.add(dto);
                }
            }
        }if (list.size() != 0){
            EditMessageText send = new EditMessageText();
            send.setChatId(message.getChatId());
            send.setText("FAYLLAR");
            send.setMessageId(message.getMessageId());
            send.setReplyMarkup(InlineKeyBoardUtil.getFile(list,path));
            myTelegramBot.sendMsg(send);
        }
    }

    public void sendFile(String path,Message message) {
        String [] arr = path.split("/");
        System.out.println(arr[0]);
        File file = new File(path);
        if (!file.isDirectory() && profileRepository.getSuperAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Download_file)) {
            sendDoc(file,message);
        } else if (file.isDirectory() && profileRepository.getSuperAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Download_file)){
            getEditFile(message,path);
        } else if (file.isDirectory() && profileRepository.getSuperAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Save_file)){
            fileHandlerService.getEditFile(message,path);
        }else {
            System.out.println("Excel fayl topilmadi.");
        }
    }

    private void sendDoc(File file,Message message) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(message.getChatId()); // Foydalanuvchi chat ID sini o'zgartiring
        sendDocument.setDocument(new InputFile(file));
        myTelegramBot.sendDoc(sendDocument);
        System.out.println(" fayl foydalanuvchiga muvaffaqiyatli yuborildi.");
    }
}
