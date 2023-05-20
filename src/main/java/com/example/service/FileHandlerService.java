package com.example.service;

import com.example.MyTelegramBot;
import com.example.dto.FileDTO;
import com.example.dto.ProfileDTO;
import com.example.enums.ProfileStep;
import com.example.repository.ProfileRepository;
import com.example.util.InlineKeyBoardUtil;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;
@AllArgsConstructor
public class FileHandlerService {
    private MyTelegramBot telegramBot;
    private ProfileRepository profileRepository;
    public void handleDocument(Message message, Document document) {
            String fileId = document.getFileId();
            String filePath = getFilePath(fileId);
            if (filePath != null) {
                try {
                    java.io.File excelFile = downloadFile(filePath);
                    if (excelFile != null) {
                        saveFile(excelFile,message,profileRepository.getProfile(message.getChatId()).getNowPath(),document.getFileName());
                        sendMessageToUser("Fayl saqlandi!", message.getChatId());
                    } else {
                        sendMessageToUser("Fayl yuklanmadi.", message.getChatId());
                    }
                } catch (IOException e) {
                    sendMessageToUser("Xato yuz berdi: " + e.getMessage(), message.getChatId());
                }
            }  else {
                sendMessageToUser("Fayl haqida ma'lumot olishda xato yuz berdi. \n " + document.getFileSize(), message.getChatId());
            }
        }

    private String getFilePath(String fileId) {
        GetFile getFileRequest = new GetFile();
        getFileRequest.setFileId(fileId);
        try {
            File file = telegramBot.getFile(getFileRequest);
            String filePath = file.getFilePath();
            if (filePath != null) {
                return "https://api.telegram.org/file/bot" + telegramBot.getBotToken() + "/" + filePath;
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    private java.io.File downloadFile(String filePath) throws IOException {
        java.io.File downloadedFile;
        try (InputStream inputStream = new URL(filePath).openStream()) {
            downloadedFile = java.io.File.createTempFile("excel_", ".xlsx");
            FileOutputStream outputStream = new FileOutputStream(downloadedFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return downloadedFile;
    }

    private void saveFile(java.io.File excelFile, Message message, String path, String fileName) throws IOException {
        if (!excelFile.exists() || !excelFile.isFile()) {
            sendMessageToUser(" fayli topilmadi yoki fayl emas.", message.getChatId());
            return;
        }

        java.io.File targetFolder = new java.io.File(path);

        if (!targetFolder.exists() || !targetFolder.isDirectory()) {
            sendMessageToUser("Hedef papka mavjud emas yoki papka emas.", message.getChatId());
            return;
        }

        Path sourcePath = excelFile.toPath();
        Path targetPath = Path.of(path, fileName);
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

        sendMessageToUser("Fayl muvaffaqiyatli saqlandi!", message.getChatId());
    }

    private void sendMessageToUser(String text, long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        telegramBot.sendMsg(sendMessage);
    }
    public void setFile(Message message) {
        String directoryPath = "Base"; // Papka yo'lini o'zgartiring
        java.io.File directory = new java.io.File(directoryPath);
        List<FileDTO> list = new LinkedList<>();
        if (directory.isDirectory()) {
            java.io.File[] files = directory.listFiles();
            if (files != null) {
                for (java.io.File file : files) {
                    if (file.isDirectory()){
                        FileDTO dto = new FileDTO();
                        dto.setName(file.getName());
                        dto.setPath(file.getPath());
                        list.add(dto);
                    }
                }
            }
            ProfileDTO dto = profileRepository.getProfile(message.getChatId());
            dto.setNowPath(directoryPath);
            profileRepository.update(dto);
        }if (list.size() != 0){
            if (profileRepository.getProfile(message.getChatId()) != null && !profileRepository.getProfile(message.getChatId()).getLastMessageId().equals(0)) {
                telegramBot.deleteMsg(new DeleteMessage(message.getChatId().toString(),profileRepository.getProfile(message.getChatId()).getLastMessageId()));
            }
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(message.getChatId());
                sendMessage.setText("Siz " + directoryPath + " papkasidasiz faylni yuborishingiz mumkin!");
                sendMessage.setReplyMarkup(InlineKeyBoardUtil.getFile(list,list.get(0).getPath()));
                Message message2 = telegramBot.sendMsg(sendMessage);
                ProfileDTO dto = profileRepository.getProfile(message.getChatId());
                dto.setStep(ProfileStep.Save_file);
                dto.setLastMessageId(message2.getMessageId());
                profileRepository.update(dto);

        }
    }
    public void setEditFile(Message message,String path){
        java.io.File directory = new java.io.File(path);
        List<FileDTO>list = new LinkedList<>();
        int countDirectory = 0;
        if (directory.isDirectory()) {
            java.io.File[] files = directory.listFiles();
            if (files != null) {
                for (java.io.File file : files) {
                    if (file.isDirectory()){
                        countDirectory++;
                        FileDTO dto = new FileDTO();
                        dto.setName(file.getName());
                        dto.setPath(file.getPath());
                        list.add(dto);
                    }
                }
                if (countDirectory == 0){
                    FileDTO file = new FileDTO();
                    file.setName("Bu yerda papka mavjud emas");
                    file.setPath("null");
                    list.add(file);
                }
            }
        }if (list.size() != 0){
            EditMessageText send = new EditMessageText();
            send.setChatId(message.getChatId());
            send.setText("Siz " + path + " papkasidasiz faylni yuborishingiz mumkin!");
            send.setMessageId(message.getMessageId());
            send.setReplyMarkup(InlineKeyBoardUtil.getFile(list,path));
            telegramBot.sendMsg(send);
        }
    }
    public void getFile(Message message){
        String directoryPath = "Base"; // Papka yo'lini o'zgartiring
        java.io.File directory = new java.io.File(directoryPath);
        List<FileDTO>list = new LinkedList<>();
        if (directory.isDirectory()) {
            java.io.File[] files = directory.listFiles();
            if (files != null) {
                for (java.io.File file : files) {
                    FileDTO dto = new FileDTO();
                    dto.setName(file.getName());
                    dto.setPath(file.getPath());
                    list.add(dto);
                }
            }
        }if (list.size() != 0){
            if (profileRepository.getProfile(message.getChatId()) != null && !profileRepository.getProfile(message.getChatId()).getLastMessageId().equals(0)){
                telegramBot.deleteMsg(new DeleteMessage(message.getChatId().toString(),profileRepository.getProfile(message.getChatId()).getLastMessageId()));
            }
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(message.getChatId());
                sendMessage.setText("FAYLLAR");
                sendMessage.setReplyMarkup(InlineKeyBoardUtil.getFile(list,list.get(0).getPath()));
                Message message2 = telegramBot.sendMsg(sendMessage);
                ProfileDTO dto = profileRepository.getProfile(message.getChatId());
                dto.setStep(ProfileStep.Download_file);
                dto.setLastMessageId(message2.getMessageId());
                profileRepository.update(dto);
        }
    }
    public void getEditFile(Message message,String path){
        System.out.println(path);
        java.io.File directory = new java.io.File(path);
        List<FileDTO>list = new LinkedList<>();
        int countDirectory = 0;
        if (directory.isDirectory()) {
            java.io.File[] files = directory.listFiles();
            if (files != null) {
                for (java.io.File file : files) {
                    FileDTO dto = new FileDTO();
                    dto.setName(file.getName());
                    dto.setPath(file.getPath());
                    list.add(dto);
                    countDirectory++;
                } if (countDirectory == 0){
                    FileDTO file = new FileDTO();
                    file.setName("Bu yerda papka mavjud emas");
                    file.setPath("null");
                    list.add(file);
                }
            }
        }if (list.size() != 0){
            EditMessageText send = new EditMessageText();
            send.setChatId(message.getChatId());
            send.setText("FAYLLAR");
            send.setMessageId(message.getMessageId());
            send.setReplyMarkup(InlineKeyBoardUtil.getFile(list,path));
            telegramBot.sendMsg(send);
        }
    }
    public void sendFile(String path,Message message) {
        String [] arr = path.split("/");
        System.out.println(arr[0]);
        java.io.File file = new java.io.File(path);
        if (!file.isDirectory() && profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Download_file)) {
            sendDoc(file,message);
        } else if (file.isDirectory() && profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Download_file)){
            getEditFile(message,path);
        } else if (file.isDirectory() && profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Save_file)){
            setEditFile(message,path);
        }else {
            System.out.println("fayl topilmadi.");
        }
    }

    private void sendDoc(java.io.File file, Message message) {
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(),"Jonatilish uchun yuklanmoqda...");
        Message message2 = telegramBot.sendMsg(sendMessage);
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(message.getChatId()); // Foydalanuvchi chat ID sini o'zgartiring
        sendDocument.setDocument(new InputFile(file));
        telegramBot.sendDoc(sendDocument);
        System.out.println(" fayl foydalanuvchiga muvaffaqiyatli yuborildi.");
        telegramBot.deleteMsg(new DeleteMessage(message.getChatId().toString(),message2.getMessageId()));
    }
}