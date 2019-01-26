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

    public static void main(String[] args) {
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


    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if(message!=null && message.hasText()){
            switch (message.getText())
            {

                case "/help":
sendMsq(message, "Djjn");
break;
default:sendMsq(message, "Djjdsn");
            }

        }
//для приема сообщение и обновлений через лонг пул
    }



    public void onUpdatesReceived(List<Update> updates) {

    }

    public String getBotUsername() {
        return "TestSTMBot";
        //вернуть имя бота
    }

    public String getBotToken() {
        return "703619267:AAHVoPUoSn4_DPm6VKUdjGCeccF6Hcsd_Qk";
        //вернуть токен
    }
}
