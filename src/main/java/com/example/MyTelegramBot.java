package com.example;

import com.example.controller.MainController;
import com.example.repository.ExternalBlockRepository;
import com.example.repository.InternalBlockRepository;
import com.example.repository.ProfileRepository;
import com.example.service.ComparisonService;
import com.example.service.ProfileService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.MalformedURLException;

public class MyTelegramBot extends TelegramLongPollingBot {

    private ProfileRepository profileRepository = new ProfileRepository();
    private InternalBlockRepository internalBlockRepository = new InternalBlockRepository();
    private ExternalBlockRepository externalBlockRepository = new ExternalBlockRepository();
    private ProfileService profileService = new ProfileService(profileRepository,this);
    private ComparisonService comparisonService = new ComparisonService(profileRepository,internalBlockRepository,externalBlockRepository,this);
    private MainController mainController  = new MainController(profileRepository,profileService,comparisonService,this);


    @Override
    public String getBotUsername() {
        return "Suhbatchi_2005_bot";
    }

    @Override
    public String getBotToken() {
        return "5532793893:AAH87hRbjUmPmMjDIXJUp4ZMKks62zyL6Rw";
    }
    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage()) {

                System.out.println(update);
                Message message = update.getMessage();
                mainController.handle(message.getText(), message);
            } else {
                System.out.println("my telegram hatto");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }


    public Message sendMsg(SendMessage method) {
        try {
            Message execute = execute(method);
            return execute;
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMsg(EditMessageText method) {
        try {
            execute(method);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMsg(SendPhoto method) {
        try {
            execute(method);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}
