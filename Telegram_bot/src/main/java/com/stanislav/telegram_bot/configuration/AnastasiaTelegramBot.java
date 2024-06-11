package com.stanislav.telegram_bot.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stanislav.telegram_bot.domain.TradeBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

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
	public AnastasiaTelegramBot(TradeBot tradeBot) {
		try {
			TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
			this.botSession = api.registerBot(tradeBot);
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
