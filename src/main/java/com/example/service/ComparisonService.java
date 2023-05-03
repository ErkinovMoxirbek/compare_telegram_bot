package com.example.service;

import com.example.MyTelegramBot;
import com.example.dto.ExternalBlockDTO;
import com.example.dto.InternalBlockDTO;
import com.example.dto.ProfileDTO;
import com.example.enums.ProfileStep;
import com.example.repository.ExternalBlockRepository;
import com.example.repository.InternalBlockRepository;
import com.example.repository.ProfileRepository;
import com.example.util.ReplyKeyboardUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class ComparisonService {
    private ProfileRepository profileRepository;
    private InternalBlockRepository internalBlockRepository;
    private ExternalBlockRepository externalBlockRepository;
    private MyTelegramBot myTelegramBot;
    private InternalBlockDTO internalBlock;
    private ExternalBlockDTO externalBlock;

    public ComparisonService(ProfileRepository profileRepository, InternalBlockRepository internalBlockRepository, ExternalBlockRepository externalBlockRepository, MyTelegramBot myTelegramBot) {
        this.profileRepository = profileRepository;
        this.internalBlockRepository = internalBlockRepository;
        this.externalBlockRepository = externalBlockRepository;
        this.myTelegramBot = myTelegramBot;
    }

    public void internalBlockEnter (Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        ProfileDTO entity = profileRepository.getProfile(message.getChatId());
        if (entity.getStep().equals(ProfileStep.Done)) {
            sendMessage.setText("Ichki blok seriya raqamining ilk 8 ta belgisini kiriting:");
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
            ProfileDTO profileEntity = profileRepository.getProfile(message.getChatId());
            profileEntity.setStep(ProfileStep.Save_Internal_Block);
            profileRepository.update(profileEntity);
            myTelegramBot.sendMsg(sendMessage);
        }
    }

    public void externalBlockEnter(Message message) {
        ProfileDTO entity = profileRepository.getProfile(message.getChatId());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        if (entity.getStep().equals(ProfileStep.Save_Internal_Block)){
            if (message.getText().length() == 8){
                internalBlock = internalBlockRepository.getInfoExel(message.getText().toLowerCase());
                if (internalBlock == null){
                    sendMessage.setText("⛔️ Ichki blok topilmadi! \n\uD83D\uDD04 Qayta urining!");
                    sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
                    entity.setStep(ProfileStep.Done);
                    profileRepository.update(entity);
                    myTelegramBot.sendMsg(sendMessage);
                    internalBlockEnter(message);
                    return;
                }
                entity.setStep(ProfileStep.Enter_External_Block);
                profileRepository.update(entity);
            }
        }
        if (entity.getStep().equals(ProfileStep.Enter_External_Block)){
            sendMessage.setText("Tashqi blok seriya raqamining ilk 8 ta belgisini kiriting:");
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
            ProfileDTO profileEntity = profileRepository.getProfile(message.getChatId());
            profileEntity.setStep(ProfileStep.Save_External_Block);
            profileRepository.update(profileEntity);
            myTelegramBot.sendMsg(sendMessage);

        }else if (entity.getStep().equals(ProfileStep.Save_External_Block)){
            if (message.getText().length() == 8){
                externalBlock = externalBlockRepository.getInfoExel(message.getText().toLowerCase());
                if (externalBlock == null){
                    sendMessage.setText("⛔️ Tashqi blok topilmadi! \n\uD83D\uDD04 Qayta urining!");
                    sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
                    myTelegramBot.sendMsg(sendMessage);
                    entity.setStep(ProfileStep.Enter_External_Block);
                    profileRepository.update(entity);
                    externalBlockEnter(message);
                    return;
                }
                comparison(internalBlock,externalBlock,message);
            }
        }
    }
    public void comparison(InternalBlockDTO internalBlock, ExternalBlockDTO externalBlock, Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        if (internalBlock!=null){
            System.out.println(internalBlock.toString());
        }
        if (externalBlock!= null){
            System.out.println(externalBlock.toString());
        }
        if (internalBlock.getNumber().equals(externalBlock.getNumber())){
            sendMessage.setText("✅ Bu ichki va tashqi bloklar bir biriga mos keladi.");
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuKeyboard2());
            myTelegramBot.sendMsg(sendMessage);
        }
        ProfileDTO entity = profileRepository.getProfile(message.getChatId());
        entity.setStep(ProfileStep.Done);
        profileRepository.update(entity);
    }

}
