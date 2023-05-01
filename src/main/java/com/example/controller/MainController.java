package com.example.controller;

import com.example.MyTelegramBot;
import com.example.dto.ProfileDTO;
import com.example.entity.InternalBlockEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileStep;
import com.example.repository.ProfileRepository;
import com.example.service.ComparisonService;
import com.example.service.ProfileService;
import com.example.util.InlineKeyBoardUtil;
import com.example.util.ReplyKeyboardUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.print.DocFlavor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainController {
    private ProfileRepository profileRepository;
    private ProfileService profileService;
    private ComparisonService comparisonService;
    private MyTelegramBot myTelegramBot;

    public MainController(ProfileRepository profileRepository, ProfileService profileService, ComparisonService comparisonService, MyTelegramBot myTelegramBot) {
        this.profileRepository = profileRepository;
        this.profileService = profileService;
        this.comparisonService = comparisonService;
        this.myTelegramBot = myTelegramBot;
    }

    public void handle(String text, Message message) throws MalformedURLException {
        ProfileEntity entity = profileRepository.getProfile(message.getChatId());
        if (text.equals("/start") || text.equals("\uD83D\uDDD1 Bekor qilish")) {
            if (entity == null){
                ProfileDTO dto = new ProfileDTO();
                dto.setId(message.getChatId());
                dto.setStep(ProfileStep.Done);
                profileService.create(dto);
                entity = profileRepository.getProfile(message.getChatId());
            }
            if (entity.getStep().equals(ProfileStep.Enter_Internal_Block) || entity.getStep().equals(ProfileStep.Enter_External_Block) || entity.getStep().equals(ProfileStep.Save_External_Block) || entity.getStep().equals(ProfileStep.Save_Internal_Block)){
                entity.setStep(ProfileStep.Done);
                profileRepository.update(entity);
            }
            if (entity.getStep().equals(ProfileStep.Done)) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(message.getChatId());
                sendMessage.setText("\uD83C\uDF89 MIX Helper botiga xush kelibsiz !!!");
                sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuKeyboard());
                myTelegramBot.sendMsg(sendMessage);
            }
        }else if (text.equals("✅ Bloklarning bir biriga mosligini tekshirish")){
            entity.setStep(ProfileStep.Done);
            profileRepository.update(entity);
            comparisonService.internalBlockEnter(message);
        }else if (entity.getStep().equals(ProfileStep.Enter_Internal_Block)||entity.getStep().equals(ProfileStep.Save_Internal_Block)||entity.getStep().equals(ProfileStep.Save_External_Block)) {
            comparisonService.externalBlockEnter(message);
        }else if (entity.getStep().equals(ProfileStep.Enter_External_Block)){
            comparisonService.externalBlockEnter(message);
        }else if (text.equals("\uD83D\uDCD1 Yo'riqnoma")){
            profileService.manual(message);
        }
    }
}
