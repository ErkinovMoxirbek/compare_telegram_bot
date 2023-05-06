package com.example;

import com.example.controller.CallBackController;
import com.example.controller.MainController;
import com.example.repository.ExternalBlockRepository;
import com.example.repository.InternalBlockRepository;
import com.example.repository.ProfileRepository;
import com.example.service.AdminService;
import com.example.service.CategoryService;
import com.example.service.ComparisonService;
import com.example.service.ProfileService;
import org.checkerframework.checker.units.qual.C;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.MalformedURLException;

public class MyTelegramBot extends TelegramLongPollingBot {

    private ProfileRepository profileRepository = new ProfileRepository();
    private InternalBlockRepository internalBlockRepository = new InternalBlockRepository();
    private ExternalBlockRepository externalBlockRepository = new ExternalBlockRepository();
    private ProfileService profileService = new ProfileService(profileRepository,this);
    private AdminService adminService = new AdminService(profileRepository,this);
    private CallBackController callBackController = new CallBackController(adminService);
    private CategoryService categoryService = new CategoryService(profileService,profileRepository,this);
    private ComparisonService comparisonService = new ComparisonService(profileRepository,internalBlockRepository,externalBlockRepository,this);
    private MainController mainController  = new MainController(profileRepository,profileService,comparisonService,this,adminService,categoryService);


    @Override
    public String getBotUsername() {
        return "MIXDhelper_bot";
    }

    @Override
    public String getBotToken() {
        return "1793820753:AAEepXiMoLpjHAYenUNtVba_exQxGbplZ_o";
    }
    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage()) {
                System.out.println(update);
                Message message = update.getMessage();
                mainController.handle(message.getText(), message);
            } else if (update.hasCallbackQuery()) {
                System.out.println(update);
                CallbackQuery callbackQuery = update.getCallbackQuery();
                callBackController.handle(callbackQuery.getMessage());
                System.out.println("sdcsdsd");
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
