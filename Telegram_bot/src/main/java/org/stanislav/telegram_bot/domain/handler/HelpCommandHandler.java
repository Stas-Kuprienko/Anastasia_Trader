package org.stanislav.telegram_bot.domain.handler;

public class HelpCommandHandler implements CommandHandler {

    private static final String manual = """
            Для работы с ботом вы должны быть зарегистрированы на сайте Anastasia-Trade.com.
            
            """;

    @Override
    public String getResponse() {
        return manual;
    }
}
