package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

public class Bot extends TelegramLongPollingBot {
    private ThreadLocal<Update> updateEvent = new ThreadLocal<>();
    private boolean screaming = false;
    private final String URL = "https://api.nasa.gov/planetary/apod?" +
            "api_key=tS9CFvpcIFY5yE9K9E2cp907zzv1mLLnocOAvoUW";

    @Override
    public String getBotUsername() {
        return "";
    }

    @Override
    public String getBotToken() {
        return "";
    }

    @Override
    public void onUpdateReceived(Update update) {
        String userFirstName = update.getMessage().getFrom().getFirstName();
        String textFromUser = update.getMessage().getText();
        long idUser = update.getMessage().getFrom().getId();
        long messageId = update.getMessage().getChatId();

        if(update.getMessage().isCommand()){
            switch (textFromUser) {
                case "/start" -> {
                    sendText(idUser, "Я бот, которого создал Алексей, для изучения BOTs");
                    sendPhoto(idUser, "images/Pacman_Montilla_1080.jpg");
                }
                case "/scream" ->          //If the command was /scream, we switch gears
                        screaming = true;
                case "/whisper" ->    //Otherwise, we return to normal
                        screaming = false;
                case "/image" -> {
                    String image = Utils.getImageUrl(URL);
                    sendText(idUser, image);
                }
                case "/help" -> {
                    sendText(idUser, "Описание в команды уже прописаны.\n" +
                            "Читай внимательнее!");
                }
                default -> {
                    return;                                       //We don't want to echo commands, so we exit
                }
            }
        }
        if (screaming) {
            sendText(idUser, textFromUser.toUpperCase());
        } else {
            sendText(idUser, textFromUser);
        }

    }

    /*
    Метод для пользователя, который только что зарегистрировался
     */
    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm); //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e); //Any error will be printed here
        }
    }

    /*
    Метод для отправки пользователю who определенное сообщение msgId
     */
    public void copyMessage(Long who, Integer msgId){
        CopyMessage cm = CopyMessage.builder()
                .fromChatId(who.toString())  //We copy from the user
                .chatId(who.toString())      //And send it back to him
                .messageId(msgId)            //Specifying what message
                .build();
        try {
            execute(cm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    Отправить сохраненное фото пользователю
     */
    public void sendPhoto(long chatId, String photoPath) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(new InputFile(new File(photoPath)));

        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
