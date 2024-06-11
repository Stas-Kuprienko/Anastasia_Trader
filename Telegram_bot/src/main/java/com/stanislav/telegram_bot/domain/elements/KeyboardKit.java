package com.stanislav.telegram_bot.domain.elements;

import com.stanislav.telegram_bot.entities.user.Account;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeyboardKit {

    public InlineKeyboardMarkup accounts(List<Account> accounts) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        for (Account a : accounts) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(a.getBroker() + '\n' + a.getClientId());
            button.setCallbackData(a.getClientId());
            buttons.add(List.of(button));
        }

        markup.setKeyboard(buttons);
        return markup;
    }
}