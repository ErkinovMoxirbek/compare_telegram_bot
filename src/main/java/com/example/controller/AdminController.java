package com.example.controller;

import com.example.MyTelegramBot;
import com.example.dto.AdminProfileDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.SuperAdminProfileDTO;
import com.example.enums.ProfileStep;
import com.example.repository.ConfidentialityRepository;
import com.example.repository.ProfileRepository;
import com.example.service.*;
import com.example.util.ReplyKeyboardUtil;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
@AllArgsConstructor
public class AdminController {
    private MyTelegramBot myTelegramBot;
    private ProfileRepository profileRepository;
    private ConfidentialityRepository confidentialityRepository;
    private FileHandlerService fileHandlerService;
    private ComparisonService comparisonService;
    private ProfileService profileService;
    private SuperAdminService superAdminService;
    public void handle(Message message){
        if (message.hasDocument() || message.hasPhoto() || message.hasVideo()){
            if (profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Save_file)){
                if (message.hasDocument()){
                    Document document = message.getDocument();
                    fileHandlerService.handleDocument(message,document);
                } else {
                    fileHandlerService.handleMediaFile(message);
                }
            }else {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(message.getChatId());
                sendMessage.setText("Buyrug'ga tushunmadim iltimos, tog'ri murojat qiling!");
                sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
                myTelegramBot.sendMsg(sendMessage);
            }
        } else if (message.getText().equals("\uD83D\uDDD1 Bekor qilish")){

            ProfileDTO profileDTO = profileRepository.getProfile(message.getChatId());
            profileDTO.setStep(ProfileStep.Done);
            if (!profileDTO.getLastMessageId().equals(0)){
                myTelegramBot.deleteMsg(new DeleteMessage(message.getChatId().toString(),profileDTO.getLastMessageId()));
                profileDTO.setLastMessageId(0);
            }if ( profileRepository.getSuperAdminProfile(message.getChatId()) != null && !profileRepository.getSuperAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Done) ){
                profileRepository.removeSuperAdmin(message.getChatId());
            }
            profileRepository.update(profileDTO);
            AdminProfileDTO dto = profileRepository.getAdminProfile(message.getChatId());
            dto.setStep(ProfileStep.Done);
            profileRepository.updateAdmin(dto);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Bo'limlarni birni tanlang!");
            sendMessage.setChatId(message.getChatId());
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuAdmin());
            myTelegramBot.sendMsg(sendMessage);
        } else if (message.getText().startsWith("/")) {
            if (message.getText().equals("/start")){
                command(message);
                return;
            }
            else if (profileRepository.getAdminProfile(message.getChatId()) != null && !profileRepository.getAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Done)||
                    profileRepository.getSuperAdminProfile(message.getChatId()) != null && !profileRepository.getSuperAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Done)||
                    profileRepository.getProfile(message.getChatId()) != null && !profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Done)){
                message.setText(message.getText().replace("/","+"));
                handle(message);
                return;
            }
            command(message);
            return;
        }if (profileRepository.getSuperAdminProfile(message.getChatId()) != null){
            if (profileRepository.getSuperAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Enter_login)) {
                superAdminService.password(message);
                return;
            }
            else if (profileRepository.getSuperAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Enter_password)) {
                superAdminService.checkLoginPassword(message);
                return;
            }
        } if(message.getText().startsWith("+")){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Tog'ri shaklda kiritilmadi! \nQayta urining!");
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
            sendMessage.setChatId(message.getChatId());
            myTelegramBot.sendMsg(sendMessage);
        }else if (message.getText().equals("✅ Bloklarning bir biriga mosligini tekshirish")){
            ProfileDTO profileDTO = profileRepository.getProfile(message.getChatId());
            profileDTO.setStep(ProfileStep.Done);
            profileRepository.update(profileDTO);
            comparisonService.internalBlockEnter(message);
        }
        //adminga kerak emasligi uchun ochirildi
        /*else if (message.getText().equals("\uD83D\uDCC1 Base")) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText("\uD83D\uDCC1 Ma'lumotlar ombori");
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuBaseKeyboard());
            myTelegramBot.sendMsg(sendMessage);
        }*/
        else if (message.getText().equals("\uD83D\uDCE4 Yuklab olish")) {
            fileHandlerService.getFile(message);
        }else if (message.getText().equals("\uD83D\uDCE5 Yuklab qo'yish")) {
            fileHandlerService.setFile(message);
        } else if (message.getText().equals("\uD83D\uDCD1 Yo'riqnoma")) {
            profileService.manual(message);
        } if (profileRepository.getProfile(message.getChatId()) != null&& !profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Done) && !profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Save_file) && message.getText() != null) {
            if (message.getText().matches("^[a-zA-Z0-9]+$") && !message.getText().startsWith("/") ){
                if (profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Enter_Internal_Block)||
                        profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Save_Internal_Block)||
                        profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Save_External_Block) ||
                        profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Enter_External_Block)) {
                    comparisonService.externalBlockEnter(message);
                }
                else {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
                    sendMessage.setText("Seriya harf va raqamda tashkil topgan,\nIltimos tog'ri shaklda kiring!");
                    sendMessage.setChatId(message.getChatId());
                    myTelegramBot.sendMsg(sendMessage);
                }
            }
        }
    }
    public void command(Message message){
        if (message.getText().equals("/start") ) {
            ProfileDTO profileDTO = profileRepository.getProfile(message.getChatId());
            if (profileDTO == null){
                profileDTO = new ProfileDTO();
                profileDTO.setId(message.getChatId());
                profileDTO.setStep(ProfileStep.Done);
                profileRepository.save(profileDTO);
            }
            AdminProfileDTO adminProfileDTO = profileRepository.getAdminProfile(message.getChatId());
            adminProfileDTO.setStep(ProfileStep.Done);
            profileRepository.updateAdmin(adminProfileDTO);
            profileDTO.setStep(ProfileStep.Done);
            profileRepository.update(profileDTO);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText("\uD83C\uDF89 MIX Helper botiga xush kelibsiz !!!");
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuAdmin());
            myTelegramBot.sendMsg(sendMessage);
        } else if (message.getText().equals("/admin") && profileRepository.getSuperAdminProfile(message.getChatId()) == null) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuAdmin());
            sendMessage.setText("Siz adminsiz!");
            sendMessage.setChatId(message.getChatId());
            myTelegramBot.sendMsg(sendMessage);
        } else if (message.getText().equals("/super_admin") && profileRepository.getAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Done)) {
            superAdminService.login(message);
        }
    }
}
