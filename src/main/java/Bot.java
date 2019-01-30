import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.List;

public class Bot extends TelegramLongPollingBot
{



    public static void main(String[] args)
    {
        ApiContextInitializer.init();

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();


            try
                 {
                    telegramBotsApi.registerBot(new Bot());
                        }

            catch (TelegramApiRequestException e)
            {
                    e.printStackTrace();
            }


    }




    public void sendMsq(Message message, String text)
    {

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);

        sendMessage.setChatId(message.getChatId().toString());//определяем на какой конкретный чат определяем ответ
            sendMessage.setReplyToMessageId(message.getMessageId());
                sendMessage.setText(text);


try
{
execute(sendMessage);
}
catch(TelegramApiException e)
{
    e.printStackTrace();
}

    }


    public void onUpdateReceived(Update update)
    {

        // We check if the update has a message and the message has text
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            //String message_text = update.getMessage().getText();
            String message_text="чекого сучара";
            long chat_id = update.getMessage().getChatId();

            SendMessage message = new SendMessage() // Create a message object object
                    .setChatId(chat_id)
                    .setText(message_text);
            try {
                execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
//для приема сообщение и обновлений через лонг пул










    public String getBotUsername() {
        return "TestSTMBot";
        //вернуть имя бота
    }

    public String getBotToken() {
        return "703619267:AAHVoPUoSn4_DPm6VKUdjGCeccF6Hcsd_Qk";
        //вернуть токен
    }




}
