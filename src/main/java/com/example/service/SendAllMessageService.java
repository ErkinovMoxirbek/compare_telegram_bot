package com.example.service;

import com.example.MyTelegramBot;
import com.example.dto.ProfileDTO;
import com.example.enums.ProfileStep;
import com.example.repository.ProfileRepository;
import com.example.repository.TokenRepository;
import com.example.util.ReplyKeyboardUtil;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@AllArgsConstructor
public class SendAllMessageService {
    private MyTelegramBot myTelegramBot;
    private ProfileRepository profileRepository;
    private TokenRepository tokenRepository;
    public void handle(String text, Message message) throws TelegramApiException {
        if (text != null && text.equals("✉️ Xabar jo'natish")){
            SendMessage sendMessage = sendMessageToUser("Jo'natmoqchi bo'lgan xabarni kiriting!",message);
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
            myTelegramBot.sendMsg(sendMessage);
            ProfileDTO dto = profileRepository.getProfile(message.getChatId());
            dto.setStep(ProfileStep.Send);
            profileRepository.update(dto);
        } else if (profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Send)) {
            if (message.hasText()){
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText(message.getText());
                if (profileRepository.getAll().size() > 0){
                    for (ProfileDTO e : profileRepository.getAll()){
                        if (e.getId().equals(message.getChatId()) ) continue;
                        sendMessage.setChatId(e.getId());
                        myTelegramBot.sendMsg(sendMessage);
                    }
                    sendMessage.setChatId(message.getChatId());
                    sendMessage.setText("Bajarildi! \nYana jonatishingiz mumkin!");
                    sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
                    myTelegramBot.sendMsg(sendMessage);
                }
            }if (message.hasVideo() || message.hasPhoto() || message.hasDocument() || message.hasSticker()) {
                ForwardMessage forwardMessage = new ForwardMessage();
                forwardMessage.setMessageId(message.getMessageId());
                for (ProfileDTO e : profileRepository.getAll()){
                    if (e.getId().equals(message.getChatId())) continue;
                    forwardMessage.setChatId(e.getId());
                    forwardMessage.setFromChatId(message.getChatId());
                    myTelegramBot.forwardMessage(forwardMessage);
                }
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(message.getChatId());
                sendMessage.setText("Bajarildi! \nYana jonatishingiz mumkin!");
                sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
                myTelegramBot.sendMsg(sendMessage);
            }

        }
    }
    private SendMessage sendMessageToUser(String text, Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(message.getChatId());
        return sendMessage;
    }
}
