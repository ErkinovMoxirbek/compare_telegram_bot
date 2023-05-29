package com.example.service;

import com.example.MyTelegramBot;
import com.example.dto.ErrorDTO;
import com.example.dto.PCBDTO;
import com.example.enums.ProfileStep;
import com.example.repository.ErrorRepository;
import com.example.repository.ProfileRepository;
import com.example.util.ReplyKeyboardUtil;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
@AllArgsConstructor
public class ErrorService {
    private MyTelegramBot myTelegramBot;
    private ProfileRepository profileRepository;
    private ErrorRepository errorRepository;
    public void searchError(String text, Message message) {
        ErrorDTO dto = null;
        if (profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_Error)){
            dto = errorRepository.getInfoExelByCode(text);
        }
        if (dto != null && dto.getErrorCode().equalsIgnoreCase(text)){
            myTelegramBot.sendMsg(sendMessageToUser(dto.getKondisanerModel() + "\n\n" +dto.getErrorInfo() + "\n\n" + dto.getCorrectionSequence(),message));
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
            sendMessage.setText("Qidirishni davom ettirishingiz mumkin!");
            myTelegramBot.sendMsg(sendMessage);
        }else {
            SendMessage sendMessage = sendMessageToUser("No'togri kiritildi yoki \nXatolik mavjud emas, qayta urining!",message);
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
            myTelegramBot.sendMsg(sendMessage);
        }
    }
    private SendMessage sendMessageToUser(String text, Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(message.getChatId());
        return sendMessage;
    }
}
