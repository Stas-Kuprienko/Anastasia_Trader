package com.stanislav.telegram_bot.domain.elements;

import com.stanislav.telegram_bot.entities.user.Account;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeyboardKit {

    private final ReplyKeyboardMarkup mainKeyboard;


    public KeyboardKit() {
        this.mainKeyboard = buildMainKeyboard();
    }


    public InlineKeyboardMarkup accounts(List<Account> accounts) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        for (Account a : accounts) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(a.getBroker().getTitle() + ':' + a.getClientId());
            button.setCallbackData(a.getClientId());
            buttons.add(List.of(button));
        }

        markup.setKeyboard(buttons);
        return markup;
    }

    public InlineKeyboardMarkup signUpLink(String buttonText, String link) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton(buttonText);
        button.setUrl(link);
        buttons.add(button);
        markup.setKeyboard(List.of(buttons));
        return markup;
    }

    public ReplyKeyboardMarkup getMainKeyboard() {
        return mainKeyboard;
    }

    private ReplyKeyboardMarkup buildMainKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setSelective(true);
        markup.setOneTimeKeyboard(true);

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();

        row1.add(new KeyboardButton("/order"));
        row1.add(new KeyboardButton("/securities"));
        row2.add(new KeyboardButton("/accounts"));
        row2.add(new KeyboardButton("/smart"));

        markup.setKeyboard(List.of(row1, row2));
        return markup;
    }

    private ReplyKeyboardMarkup buildAskLogin() {
        return null;
    }
}