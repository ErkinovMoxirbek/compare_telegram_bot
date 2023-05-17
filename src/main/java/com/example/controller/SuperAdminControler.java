package com.example.controller;

import com.example.MyTelegramBot;
import com.example.dto.AdminProfileDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.SuperAdminProfileDTO;
import com.example.enums.ProfileStep;
import com.example.repository.ProfileRepository;
import com.example.service.ComparisonService;
import com.example.service.FileHandlerService;
import com.example.service.SuperAdminService;
import com.example.util.ReplyKeyboardUtil;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@AllArgsConstructor
public class SuperAdminControler {
    private MyTelegramBot myTelegramBot;
    private ProfileRepository profileRepository;
    private ComparisonService comparisonService;
    private SuperAdminService superAdminService ;
    private FileHandlerService fileHandlerService;
    public void handle(Message message){
        if (message.hasDocument()){
            if (profileRepository.getSuperAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Save_file)){
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
            profileRepository.update(profileDTO);
            SuperAdminProfileDTO dto = profileRepository.getSuperAdminProfile(message.getChatId());
            dto.setStep(ProfileStep.Done);
            profileRepository.saveSuperAdmin(dto);
            if ( profileRepository.getAdminProfile(message.getChatId()) != null && !profileRepository.getAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Done) ){
                AdminProfileDTO adminProfileDTO = profileRepository.getAdminProfile(message.getChatId());
                adminProfileDTO.setStep(ProfileStep.Done);
                profileRepository.updateAdmin(adminProfileDTO);
            }
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Bo'limlarni birni tanlang!");
            sendMessage.setChatId(message.getChatId());
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuSuperAdmin());
            myTelegramBot.sendMsg(sendMessage);
        } else if (message.getText().startsWith("/")) {
            command(message);
        } else if (profileRepository.getSuperAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Edit_Login)) {
            superAdminService.saveLogin(message);
        } else if (profileRepository.getSuperAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Edit_Password)) {
            superAdminService.savePassword(message);
        } else if (message.getText().equals("✅ Bloklarning bir biriga mosligini tekshirish")){
            ProfileDTO profileDTO = profileRepository.getProfile(message.getChatId());
            profileDTO.setStep(ProfileStep.Done);
            profileRepository.update(profileDTO);
            comparisonService.internalBlockEnter(message);
        } else if (message.getText().equals("\uD83D\uDC64 Adminlarim")) {
            superAdminService.myAdmins(message,message.getText());
        } else if (message.getText().equals("⚙️ Sozlamalar")) {
            superAdminService.settingsMenu(message);
        } else if (message.getText().equals("\uD83D\uDCC1 Base")) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText("\uD83D\uDCC1 Ma'lumotlar ombori");
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuBaseKeyboard());
            myTelegramBot.sendMsg(sendMessage);
        } else if (message.getText().equals("\uD83D\uDCE4 Yuklab olish")) {
            SuperAdminProfileDTO dto = profileRepository.getSuperAdminProfile(message.getChatId());
            dto.setStep(ProfileStep.Download_file);
            profileRepository.updateSuperAdmin(dto);
            superAdminService.getFile(message);
        }else if (message.getText().equals("\uD83D\uDCE5 Yuklab qo'yish")) {
            SuperAdminProfileDTO dto = profileRepository.getSuperAdminProfile(message.getChatId());
            dto.setStep(ProfileStep.Save_file);
            profileRepository.updateSuperAdmin(dto);
            fileHandlerService.setFile(message);
        } else if (message.getText().equals("\uD83D\uDDC2 Ma'lumotlar")) {
            superAdminService.infoSuperAdmin(message);
        } else if (message.getText().equals("✏️ Loginni o'zgartirish")) {
            superAdminService.editLogin(message);
        } else if (message.getText().equals("✏️ Parolni o'zgartirish")) {
            superAdminService.editPassword(message);
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
            send.setReplyMarkup(ReplyKeyboardUtil.menuSuperAdmin());
            send.setChatId(message.getChatId());
            myTelegramBot.sendMsg(send);
        }
    }
    public void command(Message message){
        if (message.getText().equals("/start") ) {
            SuperAdminProfileDTO dto = profileRepository.getSuperAdminProfile(message.getChatId());
            dto.setStep(ProfileStep.Done);
            profileRepository.updateSuperAdmin(dto);
            ProfileDTO profileDTO = profileRepository.getProfile(message.getChatId());
            if (profileDTO == null){
                profileDTO = new ProfileDTO();
                profileDTO.setId(message.getChatId());
                profileDTO.setStep(ProfileStep.Done);
                profileRepository.save(profileDTO);
            }
            profileDTO.setStep(ProfileStep.Done);
            profileRepository.update(profileDTO);
            profileRepository.removeAdmin(message.getChatId());
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText("\uD83C\uDF89 MIX Helper botiga xush kelibsiz !!!");
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuSuperAdmin());
            myTelegramBot.sendMsg(sendMessage);
        } else if (message.getText().equals("/admin")) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuSuperAdmin());
            sendMessage.setText("Siz super adminsiz!");
            sendMessage.setChatId(message.getChatId());
            myTelegramBot.sendMsg(sendMessage);
        }
    }
}
