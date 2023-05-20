package com.example.controller;

import com.example.MyTelegramBot;
import com.example.dto.AdminProfileDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.SuperAdminProfileDTO;
import com.example.enums.ProfileStep;
import com.example.repository.ConfidentialityRepository;
import com.example.repository.ProfileRepository;
import com.example.service.AdminService;
import com.example.service.ComparisonService;
import com.example.service.FileHandlerService;
import com.example.service.ProfileService;
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
    private AdminService adminService;
    private ComparisonService comparisonService;
    private ProfileService profileService;
    public void handle(Message message){
        if (message.hasDocument()){
            if (profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Save_file)){
                System.out.println(message.getDocument().getFileSize());
                Document document = message.getDocument();
                fileHandlerService.handleDocument(message,document);
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
            command(message);
        } else if (message.getText().equals("✅ Bloklarning bir biriga mosligini tekshirish")){
            ProfileDTO profileDTO = profileRepository.getProfile(message.getChatId());
            profileDTO.setStep(ProfileStep.Done);
            profileRepository.update(profileDTO);
            comparisonService.internalBlockEnter(message);
        } else if (message.getText().equals("\uD83D\uDCC1 Base")) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText("\uD83D\uDCC1 Ma'lumotlar ombori");
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuBaseKeyboard());
            myTelegramBot.sendMsg(sendMessage);
        } else if (message.getText().equals("\uD83D\uDCE4 Yuklab olish")) {
            fileHandlerService.getFile(message);
        }else if (message.getText().equals("\uD83D\uDCE5 Yuklab qo'yish")) {
            fileHandlerService.setFile(message);
        } else if (message.getText().equals("\uD83D\uDCD1 Yo'riqnoma")) {
            profileService.manual(message);
        }else if (profileRepository.getSuperAdminProfile(message.getChatId()) != null){
            if (profileRepository.getSuperAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Enter_login)) {
                adminService.password(message);
            }
            else if (profileRepository.getSuperAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Enter_password)){
                adminService.checkLoginPassword(message);
            }
        }else if (profileRepository.getProfile(message.getChatId()) != null) {
            if (profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Enter_Internal_Block)||
                    profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Save_Internal_Block)||
                    profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Save_External_Block)) {
                comparisonService.externalBlockEnter(message);
            }
            else if (profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Enter_External_Block)){
                comparisonService.externalBlockEnter(message);
            }
        } else {
            SendMessage send = new SendMessage();
            send.setText("Super admin paneli sozlanmoqda");
            send.setReplyMarkup(ReplyKeyboardUtil.menuAdmin());
            send.setChatId(message.getChatId());
            myTelegramBot.sendMsg(send);
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
            profileDTO.setStep(ProfileStep.Done);
            profileRepository.update(profileDTO);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText("\uD83C\uDF89 MIX Helper botiga xush kelibsiz !!!");
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuAdmin());
            myTelegramBot.sendMsg(sendMessage);
        } else if (message.getText().equals("/admin")) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuAdmin());
            sendMessage.setText("Siz adminsiz!");
            sendMessage.setChatId(message.getChatId());
            myTelegramBot.sendMsg(sendMessage);
        } else if (message.getText().equals("/super-admin")) {
            adminService.login(message);
        }
    }
}
