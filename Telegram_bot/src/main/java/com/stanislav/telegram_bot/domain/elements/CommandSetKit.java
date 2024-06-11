package com.stanislav.telegram_bot.domain.elements;

import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public class CommandSetKit {

    @Bean
    public SetMyCommands onStart() {
        SetMyCommands commands = new SetMyCommands();
        var commandList = List.of(new BotCommand("/help", "Информация по работе с ботом"),
                new BotCommand("/accounts", "Ваши торговые счета"),
                new BotCommand("/securities", "Открыть список бумаг"),
                new BotCommand("/order", "Сделать заявку"),
                new BotCommand("/smart", "Авто-трейдинг"));
        commands.setCommands(commandList);
        return commands;
    }
}
