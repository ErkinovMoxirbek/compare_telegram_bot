package com.example.service;

import com.example.MyTelegramBot;
import com.example.dto.FileDTO;
import com.example.repository.ProfileRepository;
import com.example.util.InlineKeyBoardUtil;
import com.example.util.ReplyKeyboardUtil;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.File;
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
        if (document.getMimeType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            String fileId = document.getFileId();
            String filePath = getFilePath(fileId);

            if (filePath != null) {
                try {
                    java.io.File excelFile = downloadFile(filePath);
                    if (excelFile != null) {
                        saveExcelFile(excelFile,message,profileRepository.getProfile(message.getChatId()).getNowPath(),document.getFileName());
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

    private void saveExcelFile(java.io.File excelFile, Message message, String path,String fileName) throws IOException {
        if (!excelFile.exists() || !excelFile.isFile()) {
            sendMessageToUser("Excel fayli topilmadi yoki fayl emas.", message.getChatId());
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

        sendMessageToUser("Excel fayl muvaffaqiyatli saqlandi!", message.getChatId());
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
        }if (list.size() != 0){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText("FAYLLAR");
            sendMessage.setReplyMarkup(InlineKeyBoardUtil.getFile(list,list.get(0).getPath()));
            telegramBot.sendMsg(sendMessage);
            sendMessage.setText("Siz " + directoryPath + " papkasidasiz faylni yuborishingiz mumkin!" );
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
            telegramBot.sendMsg(sendMessage);
        }
    }
    public void getEditFile(Message message,String path){
        System.out.println(path);
        java.io.File directory = new java.io.File(path);
        List<FileDTO>list = new LinkedList<>();
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
        }if (list.size() != 0){
            EditMessageText send = new EditMessageText();
            send.setChatId(message.getChatId());
            send.setText("FAYLLAR");
            send.setMessageId(message.getMessageId());
            send.setReplyMarkup(InlineKeyBoardUtil.getFile(list,path));
            telegramBot.sendMsg(send);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Siz " + path + " papkasidasiz faylni yuborishingiz mumkin!" );
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
            sendMessage.setChatId(message.getChatId());
            telegramBot.sendMsg(sendMessage);
        }
    }
}