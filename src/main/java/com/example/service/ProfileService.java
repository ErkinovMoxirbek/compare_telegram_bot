package com.example.service;

import com.example.MyTelegramBot;
import com.example.dto.ProfileDTO;
import com.example.repository.ProfileRepository;
import com.example.util.ReplyKeyboardUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.File;

public class ProfileService {
    private ProfileRepository profileRepository;
    private MyTelegramBot myTelegramBot;
    public ProfileService(ProfileRepository profileRepository, MyTelegramBot myTelegramBot) {
        this.profileRepository = profileRepository;
        this.myTelegramBot = myTelegramBot;
    }
    public void create(ProfileDTO dto) {
        ProfileDTO entity = new ProfileDTO();
        entity.setId(dto.getId());
        entity.setStep(dto.getStep());
        profileRepository.save(entity);
    }
    public void manual (Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("✅ Bloklarning bir biriga mosligini tekshirish\n" +
                "menusini tanlang! Bot sizga -\n" +
                "Ichki blok seriya raqamining ilk 8 ta belgisini kiriting:\n" +
                "deb javob qaytaradi!");
        myTelegramBot.sendMsg(sendMessage);
        InputFile file = new InputFile(new File("seriya-photo.jpg"));
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(message.getChatId());
        sendPhoto.setPhoto(file);
        sendPhoto.setCaption("Ichki qism seriya raqamidan , ramka bilan ajratilgan birinchi 8 ta belgini kiriting (111ABCDE)");
        myTelegramBot.sendMsg(sendPhoto);
        sendMessage.setText("Bot sizga -\n" +
                "Tashqi blok seriya raqamining ilk 8 ta belgisini kiriting:\n" +
                "deb javob qaytaradi !");
        myTelegramBot.sendMsg(sendMessage);
        sendPhoto.setCaption("Tashqi qism seriya raqamidan , ramka bilan ajratilgan birinchi 8 ta belgini kiriting (113ABCDE)");
        myTelegramBot.sendMsg(sendPhoto);
        sendMessage.setText("Agar siz kiritgan seriya raqamidagi bloklar bir biriga mos kelsa bot sizga -\n" +
                "✅ Bu ichki va tashqi bloklar bir biriga mos keladi.\n" +
                "deb javob qaytaradi!\n" +
                "Agar siz kiritgan seriya raqamidagi bloklar bir biriga mos kelmasa bot sizga -\n" +
                "⛔️Bu ichki va tashqi bloklar bir biriga mos kelmaydi.\n" +
                "deb javob qaytaradi!\n" +
                "Ma'lumotni to'gri kiriting! (shrift kattaligi ahamiyatsiz)\n" +
                "Yoki botdan :\n" +
                "⚠️ Iltimos tashqi blok seriya raqamining ilk 8 ta belgisini to'g'ri kiriting:\n" +
                "degan javob olasiz\uD83D\uDE09");
        if (profileRepository.getAdminProfile(message.getChatId()) != null){
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuAdmin());
        }else {
            sendMessage.setReplyMarkup(ReplyKeyboardUtil.menuKeyboard2());
        }
        myTelegramBot.sendMsg(sendMessage);
    }
}
