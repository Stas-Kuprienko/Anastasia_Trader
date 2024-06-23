package com.stanislav.telegram_bot.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stanislav.telegram_bot.domain.TelegramBotController;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.crypto.SecretKey;
import java.util.List;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan("com.stanislav.telegram_bot")
@SpringBootApplication
public class AnastasiaTelegramBot {

	public static void main(String[] args) {
		SpringApplication.run(AnastasiaTelegramBot.class, args);
	}


	private final BotSession botSession;


	@Autowired
	public AnastasiaTelegramBot(TelegramBotController telegramBotController) {
		try {
			TelegramBotsApi telegram = new TelegramBotsApi(DefaultBotSession.class);
			this.botSession = telegram.registerBot(telegramBotController);
		} catch (TelegramApiException e) {
			//TODO logs
			throw new RuntimeException(e);
		}
	}


	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}
