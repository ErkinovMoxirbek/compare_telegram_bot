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
    public void searchPCBByGroup(String text, Message message) {
        PCBDTO pcbdto = null;
        if (profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_PCB_Box_Code)){
            pcbdto = pcbRepository.getInfoExelByPCBBoxCode(text);
        }else if (profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_PCB_Code)){
            pcbdto = pcbRepository.getInfoExelByPCBCode(text);
        }else if (profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_SAP_Code)){
            pcbdto = pcbRepository.getInfoExelBySAPCode(text);
        }else if (profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_MODEL_Code)){
            pcbdto = pcbRepository.getInfoExelBySAPCode(text);
        }
        if (pcbdto != null){
            for (PCBDTO p : pcbRepository.getListExel()){
                if (p.getGroup().equals(pcbdto.getGroup())){
                    myTelegramBot.sendMsg(sendMessageToUser(pcbRepository.getListExel().get(0).getIdName()
                            + ": " + p.getId() + ";\n\n" + pcbRepository.getListExel().get(0).getGroupName()
                            + ": " + p.getGroup() + ";\n\n"+ pcbRepository.getListExel().get(0).getSname()
                            + ": " + p.getSModel() + ";\n\n"+ pcbRepository.getListExel().get(0).getPCBBoxCodeAssemblyName()
                            +": " + p.getPCBBoxCodeAssembly() + ";\n\n" + pcbRepository.getListExel().get(0).getPCBCodeWholeName() + ": "
                            + p.getPCBCodeWhole() + ";\n\n" +  pcbRepository.getListExel().get(0).getSAPCodeName()+": "
                            + p.getSAPCode() + ";\n\n"+pcbRepository.getListExel().get(0).getModelName()
                            +": " + p.getModel() + ";",message));
                }
            }
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
            sendMessage.setText("Qidirishni davom ettirishingiz mumkin!!");
            myTelegramBot.sendMsg(sendMessage);
        }else {
            SendMessage sendMessage = sendMessageToUser("Xato kiritildi yoki \nPCB mavjud emas, qayta urining!",message);
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
            myTelegramBot.sendMsg(sendMessage);
        }
    } public void searchPCBByModel(String model, Message message) {
        PCBDTO pcbdto = null;
        if (profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_MODEL_Code)){
            pcbdto = pcbRepository.getInfoExelByPCBModel(model.toLowerCase());
        }
        if (pcbdto != null){
            for (PCBDTO p : pcbRepository.getListExel()){
                if (p.getModel() != null && p.getModel().contains(pcbdto.getModel())){
                    myTelegramBot.sendMsg(sendMessageToUser(pcbRepository.getListExel().get(0).getIdName()
                            + ": " + p.getId() + ";\n\n" + pcbRepository.getListExel().get(0).getGroupName()
                            + ": " + p.getGroup() + ";\n\n"+ pcbRepository.getListExel().get(0).getSname()
                            + ": " + p.getSModel() + ";\n\n"+ pcbRepository.getListExel().get(0).getPCBBoxCodeAssemblyName()
                            +": " + p.getPCBBoxCodeAssembly() + ";\n\n" + pcbRepository.getListExel().get(0).getPCBCodeWholeName() + ": "
                            + p.getPCBCodeWhole() + ";\n\n" +  pcbRepository.getListExel().get(0).getSAPCodeName()+": "
                            + p.getSAPCode() + ";\n\n"+pcbRepository.getListExel().get(0).getModelName()
                            +": " + p.getModel() + ";",message));
                }
            }
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.cancellation());
            sendMessage.setText("Qidirishni davom ettirishingiz mumkin!");
            myTelegramBot.sendMsg(sendMessage);
        }else {
            SendMessage sendMessage = sendMessageToUser("Kod no'togri kiritildi yoki \nXatolik mavjud emas, qayta urining!",message);
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
