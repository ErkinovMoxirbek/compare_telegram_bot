package com.example.controller;

import com.example.MyTelegramBot;
import com.example.dto.AdminProfileDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.SuperAdminProfileDTO;
import com.example.enums.ProfileStep;
import com.example.repository.ProfileRepository;
import com.example.service.ComparisonService;
import com.example.service.SuperAdminService;
import com.example.util.ReplyKeyboardUtil;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
@AllArgsConstructor
public class SuperAdminControler {
    private MyTelegramBot myTelegramBot;
    private ProfileRepository profileRepository;
    private ComparisonService comparisonService;
    private SuperAdminService superAdminService ;
    public void handle(Message message){
        if (message.getText().equals("\uD83D\uDDD1 Bekor qilish")){
            ProfileDTO profileDTO = profileRepository.getProfile(message.getChatId());
            profileDTO.setStep(ProfileStep.Done);
            profileRepository.update(profileDTO);
            if ( profileRepository.getAdminProfile(message.getChatId()) != null && !profileRepository.getAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Done) ){
                AdminProfileDTO adminProfileDTO = profileRepository.getAdminProfile(message.getChatId());
                adminProfileDTO.setStep(ProfileStep.Done);
                profileRepository.updateAdmin(adminProfileDTO);
            }
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Bo'limlarni birni tanlang!");
            sendMessage.setChatId(message.getChatId());
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuAdmin());
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
        } else if (message.getText().equals("\uD83D\uDDC2 Ma'lumotlar")) {
            superAdminService.infoSuperAdmin(message);
        } else if (message.getText().equals("✏️ Loginni o'zgartirish")) {
            superAdminService.editLogin(message);
        } else if (message.getText().equals("✏️ Parolni o'zgartirish")) {
            superAdminService.editPassword(message);
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
            SuperAdminProfileDTO dto = profileRepository.getSuperAdminProfile(message.getChatId());
            dto.setStep(ProfileStep.Done);
            profileRepository.updateSuperAdmin(dto);
            ProfileDTO profileDTO = profileRepository.getProfile(message.getChatId());
            profileDTO.setStep(ProfileStep.Done);
            profileRepository.update(profileDTO);
            profileRepository.removeAdmin(message.getChatId());
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText("\uD83C\uDF89 MIX Helper botiga xush kelibsiz !!!");
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuAdmin());
            myTelegramBot.sendMsg(sendMessage);
        }
    }
}
