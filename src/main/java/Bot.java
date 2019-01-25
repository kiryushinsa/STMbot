import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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

catch (TelegramApiException e)
{
    e.printStackTrace();
}


    }

    public void onUpdateReceived(Update update) {
//для приема сообщение и обновлений через лонг пул
    }

    public void onUpdatesReceived(List<Update> updates) {

    }

    public String getBotUsername() {
        return "TestWebBot";
        //вернуть имя бота
    }

    public String getBotToken() {
        return "703619267:AAHVoPUoSn4_DPm6VKUdjGCeccF6Hcsd_Qk";
        //вернуть токен
    }
}
