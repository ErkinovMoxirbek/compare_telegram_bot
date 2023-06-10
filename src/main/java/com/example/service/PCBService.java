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

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
public class PCBService {
    private MyTelegramBot myTelegramBot;
    private PCBRepository pcbRepository;
    private ProfileRepository profileRepository;
    public void searchPCBByGroup(String text, Message message) {
        List<PCBDTO> list = pcbRepository.getListExel();
        int i = 0;
        int j = 0;
        if (text != null){
            for (PCBDTO p : list){
                if ( i != 0 && p.getSAPCode().toLowerCase().contains(text.toLowerCase())){
                    j++;
                    placeSendMessage(message,p);
                }
                i++;
            }
        }if (j != 0){
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
    }
    public void searchPCBByModel(String model, Message message) {
        if (pcbRepository.getListExel().size() > 0 && model != null ){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (PCBDTO p : pcbRepository.getListExel()){
                        if ( p.getModel() != null && p.getModel().contains(model)){
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
                }
            }).start();
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
    public void placeSendMessage(Message message,PCBDTO p){
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
