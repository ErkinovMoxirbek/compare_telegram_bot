package com.example.controller;

import com.example.MyTelegramBot;
import com.example.dto.AdminProfileDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.SuperAdminProfileDTO;
import com.example.enums.ProfileStep;
import com.example.repository.ProfileRepository;
import com.example.service.ComparisonService;
import com.example.util.ReplyKeyboardUtil;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
@AllArgsConstructor
public class SuperAdminControler {
    private MyTelegramBot myTelegramBot;
    private ProfileRepository profileRepository;
    private ComparisonService comparisonService;
    public void handle(Message message){
        if (message.getText().equals("\uD83D\uDDD1 Bekor qilish")){
            ProfileDTO profileDTO = profileRepository.getProfile(message.getChatId());
            profileDTO.setStep(ProfileStep.Done);
            profileRepository.update(profileDTO);
            if ( profileRepository.getAdminProfile(message.getChatId()) != null && !profileRepository.getAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Done) ){
                AdminProfileDTO adminProfileDTO = profileRepository.getAdminProfile(message.getChatId());
                adminProfileDTO.setStep(ProfileStep.Done);
                profileRepository.updateAdmin(adminProfileDTO);
            } if (profileRepository.getSuperAdminProfile(message.getChatId()) != null &&
                    !profileRepository.getSuperAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Done)){
                SuperAdminProfileDTO dto = profileRepository.getSuperAdminProfile(message.getChatId());
                dto.setId(1L);
                dto.setStep(ProfileStep.Done);
                profileRepository.updateSuperAdmin(dto);
            }
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Bo'limlarni birni tanlang!");
            sendMessage.setChatId(message.getChatId());
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuAdmin());
            myTelegramBot.sendMsg(sendMessage);
            return;
        }
        else if (message.getText().equals("✅ Bloklarning bir biriga mosligini tekshirish")){
            ProfileDTO profileDTO = profileRepository.getProfile(message.getChatId());
            profileDTO.setStep(ProfileStep.Done);
            profileRepository.update(profileDTO);
            comparisonService.internalBlockEnter(message);
        }else {
            SendMessage send = new SendMessage();
            send.setText("Super admin paneli sozlanmoqda");
            send.setReplyMarkup(ReplyKeyboardUtil.menuAdmin());
            send.setChatId(message.getChatId());
            myTelegramBot.sendMsg(send);
        }
    }
}
