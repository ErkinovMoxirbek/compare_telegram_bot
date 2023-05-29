package com.example.controller;

import com.example.MyTelegramBot;
import com.example.dto.ProfileDTO;
import com.example.enums.ProfileStep;
import com.example.repository.PCBRepository;
import com.example.repository.ProfileRepository;
import com.example.service.ErrorService;
import com.example.util.ReplyKeyboardUtil;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@AllArgsConstructor
public class ErrorController {
    private MyTelegramBot myTelegramBot;
    private ErrorService errorService;
    private ProfileRepository profileRepository;
    public void handle(String text, Message message){
        if (text.equals("❗️ Xatolik kodlari") ){
            myTelegramBot.sendMsg(sendMessageToUser("Xatolik kodini kiriting!",message));
            ProfileDTO dto = profileRepository.getProfile(message.getChatId());
            dto.setStep(ProfileStep.Search_Error);
            profileRepository.update(dto);
        } else if (profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_Error)) {
            errorService.searchError(text,message);
        }
    }
    private SendMessage sendMessageToUser(String text, Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
        return sendMessage;
    }
}
