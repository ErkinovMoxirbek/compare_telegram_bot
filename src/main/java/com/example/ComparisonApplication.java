package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class ComparisonApplication {

	public static void main(String[] args) throws TelegramApiException {
		try {

			File file = new File("Profile.txt");
			if (!file.exists()) {
				file.createNewFile();
			}File file1 = new File("AdminProfile.txt");
			if (!file1.exists()) {
				file1.createNewFile();
			}File file2 = new File("SuperAdminProfile.txt");
			if (!file2.exists()) {
				file2.createNewFile();
			}File file3 = new File("ConfidentialityDTO.txt");
			if (!file2.exists()) {
				file2.createNewFile();
			}

			TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
			telegramBotsApi.registerBot(new MyTelegramBot());
		} catch (TelegramApiException e) {
			e.printStackTrace();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		SpringApplication.run(ComparisonApplication.class, args);
	}
}
