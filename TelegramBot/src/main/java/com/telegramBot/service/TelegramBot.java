package com.telegramBot.service;

import com.telegramBot.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;
    static final String BUTTON_ONE = "BUTTON_ONE";
    static final String BUTTON_TWO = "BUTTON_TWO";
    static final String BUTTON_NEXT = "BUTTON_NEXT";

    static final String BUTTON_ONE_SECOND_MENU = "BUTTON_ONE_SECOND_MENU";
    static final String BUTTON_TWO_SECOND_MENU = "BUTTON_TWO_SECOND_MENU";
    static final String BUTTON_PREVIOUS = "BUTTON_PREVIOUS";

    public TelegramBot(BotConfig config){
        this.config = config;
    }
    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText){
                case "/start":

                    startCommandReceived(chatId);
                    break;

                case "/next":

                    secondMenu(chatId);
                    break;

                default:
                    sendMessage(chatId, "Sorry, this command is not supported");
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callbackData.equals(BUTTON_ONE)) {
                String text = "Кнопка 1";
                executeEditMessageText(text, chatId, messageId);
                startCommandReceived(chatId);

            } else if (callbackData.equals(BUTTON_TWO)) {
                String text = "Кнопка 2";
                executeEditMessageText(text, chatId, messageId);
                startCommandReceived(chatId);

            } else if (callbackData.equals(BUTTON_NEXT)) {
                String text = "/next";
                executeEditMessageText(text, chatId, messageId);
                secondMenu(chatId);

            } else if (callbackData.equals(BUTTON_PREVIOUS)) {
                String text = "/start";
                executeEditMessageText(text, chatId, messageId);
                startCommandReceived(chatId);
            }

            else if (callbackData.equals(BUTTON_ONE_SECOND_MENU)) {
                String text = "Кнопка 1";
                executeEditMessageText(text, chatId, messageId);
                secondMenu(chatId);
            }

            else if (callbackData.equals(BUTTON_TWO_SECOND_MENU)) {
                String text = "Кнопка 2";
                executeEditMessageText(text, chatId, messageId);
                secondMenu(chatId);
            }

        }
    }

    private void startCommandReceived(long chatId) {

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("First menu");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var ButtonOne = new InlineKeyboardButton();

        ButtonOne.setText("Кнопка 1");
        ButtonOne.setCallbackData(BUTTON_ONE);

        var ButtonTwo = new InlineKeyboardButton();

        ButtonTwo.setText("Кнопка 2");
        ButtonTwo.setCallbackData(BUTTON_TWO);


        var ButtonNext = new InlineKeyboardButton();
        ButtonNext.setText("Далі");
        ButtonNext.setCallbackData(BUTTON_NEXT);

        rowInLine.add(ButtonOne);
        rowInLine.add(ButtonTwo);
        rowInLine.add(ButtonNext);

        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        executeMessage(message);

    }

    private void secondMenu(long chatId) {

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Second menu");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var ButtonOneSecondMenu = new InlineKeyboardButton();

        ButtonOneSecondMenu.setText("Кнопка 1");
        ButtonOneSecondMenu.setCallbackData(BUTTON_ONE_SECOND_MENU);

        var ButtonTwoSecondMenu = new InlineKeyboardButton();

        ButtonTwoSecondMenu.setText("Кнопка 2");
        ButtonTwoSecondMenu.setCallbackData(BUTTON_TWO_SECOND_MENU);


        var ButtonPrevious = new InlineKeyboardButton();
        ButtonPrevious.setText("Назад");
        ButtonPrevious.setCallbackData(BUTTON_PREVIOUS);

        rowInLine.add(ButtonOneSecondMenu);
        rowInLine.add(ButtonTwoSecondMenu);
        rowInLine.add(ButtonPrevious);

        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        executeMessage(message);
    }

    private void sendMessage (long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException exception){

        }
    }

    private void executeEditMessageText(String text, long chatId, long messageId){
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);

        try {
            execute(message);
        } catch (TelegramApiException e) {

        }
    }

    private void executeMessage(SendMessage message){
        try {
            execute(message);
        } catch (TelegramApiException e) {

        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    public String getBotToken(){
        return config.getToken();
    }
}
