package com.example;

import com.example.controller.CallBackController;
import com.example.controller.MainController;
import com.example.controller.SuperAdminControler;
import com.example.dto.SuperAdminProfileDTO;
import com.example.repository.ConfidentialityRepository;
import com.example.repository.ExternalBlockRepository;
import com.example.repository.InternalBlockRepository;
import com.example.repository.ProfileRepository;
import com.example.service.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.MalformedURLException;

public class MyTelegramBot extends TelegramLongPollingBot {

    private ProfileRepository profileRepository = new ProfileRepository();
    private InternalBlockRepository internalBlockRepository = new InternalBlockRepository();
    private ExternalBlockRepository externalBlockRepository = new ExternalBlockRepository();
    private ConfidentialityRepository confidentialityRepository = new ConfidentialityRepository();
    private FileHandlerService fileHandlerService = new FileHandlerService(this,profileRepository);
    private ProfileService profileService = new ProfileService(profileRepository,this);
    private SuperAdminService superAdminService = new SuperAdminService(this,profileRepository,confidentialityRepository,fileHandlerService);
    private ComparisonService comparisonService = new ComparisonService(profileRepository,internalBlockRepository,externalBlockRepository,this);
    private SuperAdminControler superAdminControler = new SuperAdminControler(this,profileRepository,comparisonService,superAdminService,fileHandlerService);
    private AdminService adminService = new AdminService(profileRepository,this,confidentialityRepository,superAdminControler);
    private CallBackController callBackController = new CallBackController(superAdminService,adminService,profileRepository,fileHandlerService);
    private CategoryService categoryService = new CategoryService(profileService,profileRepository,this);
    private MainController mainController  = new MainController(profileRepository,profileService,comparisonService,this,adminService,categoryService);


    @Override
    public String getBotUsername() {
        return "MIXDhelper_bot";
    }

    @Override
    public String getBotToken() {
        return "1793820753:AAGTT-mFXQR_3XhL8857g5NMK0BsCiznIqQ";
    }
    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage()){
                SuperAdminProfileDTO dto = profileRepository.getSuperAdminProfile(update.getMessage().getChatId());
                if (dto != null && dto.getVisible()){
                    if (update.getMessage().getChatId().equals(dto.getId())){
                        Message message = update.getMessage();
                        superAdminControler.handle(message);
                    }
                }else {
                    System.out.println(update);
                    Message message = update.getMessage();
                    mainController.handle(message.getText(), message);
                }
            }
            else if (update.hasCallbackQuery()) {
                System.out.println(update);
                CallbackQuery callbackQuery = update.getCallbackQuery();
                String data = callbackQuery.getData();
                callBackController.handle(data, callbackQuery.getMessage());
            }else {
                System.out.println("my telegram hatto");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }


    public Message sendDoc(SendDocument method) {
        try {
            return execute(method);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    public Message sendMsg(SendMessage method) {
        try {
            return execute(method);
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
    }public void deleteMsg(DeleteMessage method) {
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

    public File getFile(GetFile getFileRequest) throws TelegramApiException {
        return execute(getFileRequest);

    }
}
