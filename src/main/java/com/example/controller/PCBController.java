package com.example.controller;

import com.example.MyTelegramBot;
import com.example.dto.ProfileDTO;
import com.example.enums.ProfileStep;
import com.example.repository.ProfileRepository;
import com.example.service.PCBService;
import com.example.util.ReplyKeyboardUtil;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@AllArgsConstructor
public class PCBController {
    private MyTelegramBot myTelegramBot;
    private ProfileRepository profileRepository;
    private PCBService pcbService;
    public void handle(String text ,Message message){
        if (text.equals("\uD83D\uDDD1 Bekor qilish")){
            ProfileDTO dto = profileRepository.getProfile(message.getChatId());
            dto.setStep(ProfileStep.Done);
            profileRepository.update(dto);
            message.setText("\uD83D\uDDD1 Bekor qilish");
            Update update = new Update();
            update.setMessage(message);
            myTelegramBot.onUpdateReceived(update);
        }else if (text.equals("\uD83D\uDD0D PCB qidirish")){
            SendMessage sendMessage = sendMessageToUsers("Bo'limlardan birini tanlang!",message);
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuPCB());
            myTelegramBot.sendMsg(sendMessage);
            ProfileDTO dto = profileRepository.getProfile(message.getChatId());
            dto.setStep(ProfileStep.Search_PCB);
            profileRepository.update(dto);
        } else if (text.equals("\uD83D\uDD0E PCB BOX CODE bo'yicha qidirish")) {
            SendMessage sendMessage = sendMessageToUsers("PCB Boxning 14-ta raqamli kodini kiriting!",message);
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
            myTelegramBot.sendMsg(sendMessage);
            ProfileDTO dto = profileRepository.getProfile(message.getChatId());
            dto.setStep(ProfileStep.Search_PCB_Box_Code);
            profileRepository.update(dto);
        } else if (text.equals("\uD83D\uDD0E PCB CODE bo'yicha qidirish")) {
            SendMessage sendMessage = sendMessageToUsers("PCBning 14-ta raqamli kodini kiriting!",message);
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
            myTelegramBot.sendMsg(sendMessage);
            ProfileDTO dto = profileRepository.getProfile(message.getChatId());
            dto.setStep(ProfileStep.Search_PCB_Code);
            profileRepository.update(dto);
        }else if (text.equals("\uD83D\uDD0E SAP CODE bo'yicha qidirish")){
            SendMessage sendMessage = sendMessageToUsers("SAP Codini kiriting!",message);
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
            myTelegramBot.sendMsg(sendMessage);
            ProfileDTO dto = profileRepository.getProfile(message.getChatId());
            dto.setStep(ProfileStep.Search_SAP_Code);
            profileRepository.update(dto);
        } else if (text.equals("\uD83D\uDD0E MODEL bo'yicha qidirish")) {
            SendMessage sendMessage = sendMessageToUsers("Modelni kiriting! ",message);
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
            myTelegramBot.sendMsg(sendMessage);
            ProfileDTO dto = profileRepository.getProfile(message.getChatId());
            dto.setStep(ProfileStep.Search_MODEL_Code);
            profileRepository.update(dto);
        } else if (profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_PCB_Box_Code)||
                profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_PCB_Code)||
                profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_SAP_Code)) {
            pcbService.searchPCBByGroup(text,message);
        } else if (profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_MODEL_Code)) {
            pcbService.searchPCBByModel(text,message);
        }
    }
    private SendMessage sendMessageToUsers(String text,Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(message.getChatId());
        return sendMessage;
    }
}
