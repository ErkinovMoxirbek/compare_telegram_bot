package com.example.service;

import com.example.MyTelegramBot;
import com.example.dto.ProfileDTO;
import com.example.enums.ProfileStep;
import com.example.repository.ProfileRepository;
import com.example.util.ReplyKeyboardUtil;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
@AllArgsConstructor
public class CategoryService {
    private ProfileService profileService;
    private ProfileRepository profileRepository;
    private MyTelegramBot myTelegramBot;

    public void createProfile(Message message){
        ProfileDTO dto = new ProfileDTO();
        dto.setId(message.getChatId());
        dto.setStep(ProfileStep.Done);
        profileService.create(dto);
        profileRepository.getProfile(message.getChatId());
    }
    public void helloMenu(Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("\uD83C\uDF89 MIX Helper botiga xush kelibsiz !!!");
        sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuKeyboard());
        myTelegramBot.sendMsg(sendMessage);
    }
}
