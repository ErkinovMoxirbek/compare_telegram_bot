package com.example.service;

import com.example.MyTelegramBot;
import com.example.dto.PCBDTO;
import com.example.enums.ProfileStep;
import com.example.repository.PCBRepository;
import com.example.repository.ProfileRepository;
import com.example.util.ReplyKeyboardUtil;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
@AllArgsConstructor
public class PCBService {
    private MyTelegramBot myTelegramBot;
    private PCBRepository pcbRepository;
    private ProfileRepository profileRepository;
    public void searchPCB(String text, Message message) {
        PCBDTO pcbdto = null;
        if (profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_PCB_Box_Code)){
            pcbdto = pcbRepository.getInfoExelByPCBBoxCode(text);
        }else if (profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_PCB_Code)){
            pcbdto = pcbRepository.getInfoExelByPCBCode(text);
        }else if (profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_SAP_Code)){
            pcbdto = pcbRepository.getInfoExelBySAPCode(text);
        }
        if (pcbdto != null){
            for (PCBDTO p : pcbRepository.getListExel()){
                if (p.getGroup().equals(pcbdto.getGroup())){
                    myTelegramBot.sendMsg(sendMessageToUser("ID: " + p.getId() + ";\nGROUP: " + p.getGroup() + ";\nMODEL NAME SUPPLIER: " + p.getName() + ";\nPCB Box Code Assembly: " + p.getPCBBoxCodeAssembly() + ";\nPCB Code: " + p.getPCBCodeWhole() + ";\nSAP CODE: " + p.getSAPCode() /*+ ";\nKONDITSIONER MODELI: " + p.getModel() + ";"*/,message));
                }
            }
        }else {
            SendMessage sendMessage = sendMessageToUser("PCB mavjud emas, qayta urining!",message);
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
            myTelegramBot.sendMsg(sendMessage);
        }
    }
    private SendMessage sendMessageToUser(String text,Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(message.getChatId());
        return sendMessage;
    }
}
