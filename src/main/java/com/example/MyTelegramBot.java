package com.example;

import com.example.controller.*;
import com.example.dto.AdminProfileDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.SuperAdminProfileDTO;
import com.example.enums.ProfileStep;
import com.example.repository.*;
import com.example.service.*;
import com.example.util.ReplyKeyboardUtil;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.MalformedURLException;

public class MyTelegramBot extends TelegramLongPollingBot {

    private ProfileRepository profileRepository = new ProfileRepository();
    private InternalBlockRepository internalBlockRepository = new InternalBlockRepository();
    private ExternalBlockRepository externalBlockRepository = new ExternalBlockRepository();
    private ConfidentialityRepository confidentialityRepository = new ConfidentialityRepository();
    private PCBRepository pcbRepository = new PCBRepository();
    private FileHandlerService fileHandlerService = new FileHandlerService(this,profileRepository);
    private ProfileService profileService = new ProfileService(profileRepository,this);
    private SuperAdminService superAdminService = new SuperAdminService(this,profileRepository,confidentialityRepository,fileHandlerService);
    private ComparisonService comparisonService = new ComparisonService(profileRepository,internalBlockRepository,externalBlockRepository,this);
    private PCBService pcbService = new PCBService(this,pcbRepository,profileRepository);
    private ErrorRepository errorRepository = new ErrorRepository();
    private ErrorService errorService = new ErrorService(this,profileRepository,errorRepository);
    private ErrorController errorController = new ErrorController(this,errorService,profileRepository);
    private PCBController pcbController = new PCBController(this,profileRepository,pcbService);
    private SuperAdminControler superAdminControler = new SuperAdminControler(this,profileRepository,comparisonService,superAdminService,fileHandlerService);
    private AdminService adminService = new AdminService(profileRepository,this,confidentialityRepository,superAdminControler,fileHandlerService);
    private CallBackController callBackController = new CallBackController(superAdminService,adminService,profileRepository,fileHandlerService);
    private CategoryService categoryService = new CategoryService(profileService,profileRepository,this);
    private MainController mainController  = new MainController(profileRepository,profileService,comparisonService,this,adminService,categoryService,superAdminService);
    private AdminController adminController = new AdminController(this,profileRepository,confidentialityRepository,fileHandlerService,comparisonService,profileService,superAdminService);
    private TokenRepository tokenRepository = new TokenRepository();
    private SendAllMessageService sendAllMessageService = new SendAllMessageService(this,profileRepository,tokenRepository);

    @Override
    public String getBotUsername() {
        return tokenRepository.get("token").getUser();
    }

    @Override
    public String getBotToken() {
        return tokenRepository.get("token").getToken();
    }
    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage()){
                SuperAdminProfileDTO dto = profileRepository.getSuperAdminProfile(update.getMessage().getChatId());
                AdminProfileDTO adminProfileDTO = profileRepository.getAdminProfile(update.getMessage().getChatId());
                if (dto != null && dto.getVisible()){
                    if (profileRepository.getProfile(update.getMessage().getChatId()) == null){
                        categoryService.createProfile(update.getMessage());
                    }
                    if (update.getMessage().getChatId().equals(dto.getId())){
                        Message message = update.getMessage();
                        if (message.hasVideo() && profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Send) || message.hasPhoto()&& profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Send) ){
                            sendAllMessageService.handle(message.getText(),message);
                            return;
                        }
                        if (message.getText() != null && !message.getText().equals("\uD83D\uDDD1 Bekor qilish") && message.getText().equals("✉️ Xabar jo'natish") || message.getText() != null && !message.getText().equals("\uD83D\uDDD1 Bekor qilish") && profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Send)  ){
                            sendAllMessageService.handle(message.getText(),message);
                            return;
                        }
                        if ( message.getText() != null && !message.getText().equals("\uD83D\uDDD1 Bekor qilish") && !message.getText().startsWith("/")){
                            if (message.getText() != null && message.getText().equals("\uD83D\uDD0D PCB qidirish") ||
                                    profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_PCB) && !message.getText().startsWith("/") ||
                                    profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_PCB_Code) && !message.getText().startsWith("/") ||
                                    profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_PCB_Box_Code) && !message.getText().startsWith("/") ||
                                    profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_SAP_Code) && !message.getText().startsWith("/")||
                                    profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_MODEL_Code) && !message.getText().startsWith("/") ){
                                pcbController.handle(message.getText(),message);
                                return;
                            }
                            if (message.getText().equals("❗️ Xatolik kodlari") || profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_Error) && !message.getText().startsWith("/")){
                                errorController.handle(message.getText(),message);
                                return;
                            }
                        }
                        superAdminControler.handle(message);
                    }
                }else if (adminProfileDTO != null && adminProfileDTO.getVisible()){
                    if (update.getMessage().getChatId().equals(adminProfileDTO.getId())){
                        Message message = update.getMessage();
                        if (!message.getText().equals("\uD83D\uDDD1 Bekor qilish") && !message.getText().startsWith("/")){
                            if (message.getText() != null && message.getText().equals("\uD83D\uDD0D PCB qidirish") ||
                                    profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_PCB) && !message.getText().startsWith("/") ||
                                    profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_PCB_Code) && !message.getText().startsWith("/") ||
                                    profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_PCB_Box_Code) && !message.getText().startsWith("/") ||
                                    profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_SAP_Code) && !message.getText().startsWith("/")||
                                    profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_MODEL_Code) && !message.getText().startsWith("/") ){
                                pcbController.handle(message.getText(),message);
                                return;
                            }
                            if (message.getText().equals("❗️ Xatolik kodlari") || profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_Error) && !message.getText().startsWith("/")){
                                errorController.handle(message.getText(),message);
                                return;
                            }
                        }
                        adminController.handle(message);

                    }
                }else {
                    if (profileRepository.getProfile(update.getMessage().getChatId()) == null){
                        categoryService.createProfile(update.getMessage());
                    }if (update.getMessage().hasContact()){
                        mainController.handle(update.getMessage().getText(),update.getMessage());
                        return;
                    }
                    System.out.println(update);
                    Message message = update.getMessage();
                    if (profileRepository.getProfile(message.getChatId()) == null){
                        mainController.command(message);
                    }
                    if (message.getText() != null){
                        if (!message.getText().equals("\uD83D\uDDD1 Bekor qilish") && !message.getText().startsWith("/")){
                            if (message.getText() != null && message.getText().equals("\uD83D\uDD0D PCB qidirish") ||
                                    profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_PCB) && !message.getText().startsWith("/") ||
                                    profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_PCB_Code) && !message.getText().startsWith("/") ||
                                    profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_PCB_Box_Code) && !message.getText().startsWith("/") ||
                                    profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_SAP_Code) && !message.getText().startsWith("/")||
                                    profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_MODEL_Code) && !message.getText().startsWith("/") ){
                                pcbController.handle(message.getText(),message);
                            }
                            if (message.getText().equals("❗️ Xatolik kodlari") || profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Search_Error) && !message.getText().startsWith("/")){
                                errorController.handle(message.getText(),message);
                            }
                        }
                        if (message.getText() != null && message.getText().startsWith("/")) {
                            if (message.getText().equals("/start")){
                                mainController.command(message);
                                return;
                            }
                            if (profileRepository.getAdminProfile(message.getChatId()) != null && !profileRepository.getAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Done)||
                                    profileRepository.getSuperAdminProfile(message.getChatId()) != null && !profileRepository.getSuperAdminProfile(message.getChatId()).getStep().equals(ProfileStep.Done)||
                                    profileRepository.getProfile(message.getChatId()) != null && !profileRepository.getProfile(message.getChatId()).getStep().equals(ProfileStep.Done)){
                                message.setText(message.getText().replace("/","+"));
                                mainController.handle(message.getText(),message);
                                return;
                            }
                            mainController.command(message);
                        }else {
                            mainController.handle(message.getText(), message);
                        }
                    }
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
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    //Embedding information in sending a message
    public SendMessage embeddingInformationWithReplyKeyBoard(Message message, String sendText, ReplyKeyboardUtil replyKeyboardUtil){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(sendText);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setReplyMarkup((ReplyKeyboard) replyKeyboardUtil);
        return sendMessage;
    }

    public Message sendDoc(SendDocument method) {
        try {
            return execute(method);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    public void forwardMessage(ForwardMessage method) throws TelegramApiException {
         execute(method);
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
