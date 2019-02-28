
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;


import javax.imageio.ImageIO;
import javax.validation.constraints.Null;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.*;

import java.io.FileWriter;


public class Bot extends TelegramLongPollingBot
{

    private  List<Integer> processingUsers = new ArrayList<>();

    private Task Task=new Task();


    public void onUpdateReceived(Update update)
    {

        


        if(update.hasMessage() && update.getMessage().hasText()  )
        {


            switch(update.getMessage().getText())
            {
                case "/newtask":
                    {



                    sendTextMessageToUser("Введите название задачи", update);

                    handleNewTaskCommand(update);


                }
                    break;




                case "/send":
                {

                        if(Task.getTaskName()!=null&&Task.getTaskNote()!=null&&Task.getTaskPicture()!=null) {
                            try {
                                SendToTodoist(Task);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else sendTextMessageToUser("Заполни все поля",update);


                }
                    break;

                 default:{
                     handleSimpleMessage(update);

                     //sendTextMessageToUser("Нет такой комманды",update.getMessage().getChatId());
                     }
            }
        }


if(update.getMessage().hasPhoto()){handleSimpleMessage(update);}






    }
//для приема сообщение и обновлений через лонг пул

    private void handleNewTaskCommand(Update update)
    {
        processingUsers.add(update.getMessage().getFrom().getId());
    }




    private void handleSimpleMessage(Update update)
    {

        Integer userId = update.getMessage().getFrom().getId();






        if(processingUsers.contains(userId)  && Task.getTaskName() == null && update.getMessage().hasText())
        {

            Task.setTaskName(update.getMessage().getText());
            System.out.println(Task.getTaskName());
            sendTextMessageToUser("Введите дополнительные указания к задаче", update);


        }

        else if(processingUsers.contains(userId) && update.getMessage().hasText() && Task.getTaskNote()==null)  {  Task.setTaskNote(update.getMessage().getText());   sendTextMessageToUser("Добавьте фото к задаче",update);         }


        if(processingUsers.contains(userId) && update.getMessage().hasPhoto()&& Task.getTaskPicture()==null)
        {

            System.out.println("Я вошел в тело отправки фото");
            PhotoSize photo = getPhoto(update);

            try
            {
                //System.out.println(uploadPhoto(photo.getFileId()));
                Task.setTaskPicture(uploadPhoto(photo.getFileId()));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }



            testNewTask(update);
            processingUsers.remove(userId);
        }




    }





    private void testNewTask(Update update)
    {
        System.out.println("Название задачи:  " + Task.getTaskName());
        System.out.println("Комментарий к задаче: " + Task.getTaskNote());
        System.out.println("Фото называется: " + Task.getTaskPicture());


    }







public void sendTextMessageToUser(String message_text,Update update)
{

long chat_id = update.getMessage().getChatId();

    SendMessage messageForUser = new SendMessage()
            .setChatId(chat_id)
            .setText(message_text);

    try {
        execute(messageForUser);
    } catch (TelegramApiException e) {
        e.printStackTrace();
    }

}




public PhotoSize getPhoto(Update update)
{// выбрали самую большую фотографию из списка

    if (update.hasMessage()&& update.getMessage().hasPhoto()) {

        List<PhotoSize> photos = update.getMessage().getPhoto();

        PhotoSize photoSize = photos.stream()
                .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                .findFirst()
                .orElse(null);


        return photoSize;

    }

    return null;


}







// поиск или формирование file_path
public String getFilePath(PhotoSize photo) throws IOException {
       // Objects.requireNonNull(null); выдает ошибку - непонятно почему поэтому скрыто за комментариями

        if(photo.hasFilePath())
        {
            return photo.getFilePath();

        }
            else
                {

                    GetFile getFileMethod = new GetFile();

                    getFileMethod.setFileId(photo.getFileId());

                    uploadPhoto(photo.getFileId());

                    try {
                        File file = execute(getFileMethod);
                        return file.getFilePath();
                         }
                          catch (TelegramApiException e){ e.printStackTrace();}
                    System.out.println("path");

                }


     return null;

    }


     private String uploadPhoto( String file_id) throws IOException
    {


        URL url = new URL("https://api.telegram.org/bot"+getBotToken()+"/getFile?file_id="+file_id);

        BufferedReader in = new BufferedReader(new InputStreamReader( url.openStream()));
        String res = in.readLine();

        JSONObject jresult = new JSONObject(res);
        JSONObject path = jresult.getJSONObject("result");


        String file_path = path.getString("file_path");



        URL download = new URL("https://api.telegram.org/file/bot" + getBotToken() + "/" + file_path);



        String nameOfFile = (String.valueOf(1 + (int) (Math.random() * 1000)) )+file_id  ;




        FileOutputStream fos = new FileOutputStream("/home/kiryushin/projects/BotSTMApi/src/main/resources/pictures/" +nameOfFile + ".jpeg" );


        System.out.println("Start upload");
        ReadableByteChannel rbc = Channels.newChannel(download.openStream());



        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();

     //   uploadFlag = 0;
        System.out.println("Uploaded!");
        return  nameOfFile;

    }




private void SendToTodoist(Task task) throws IOException
{

        String headline = task.getTaskName();
        String note = task.getTaskNote();
        String image = task.getTaskPicture();

        FileWriter writer = new FileWriter("/home/kiryushin/projects/BotSTMApi/src/main/resources/text/test",false);
        String text=headline + "|"+ note + "|" + image+"|";
        writer.write(text);
        writer.flush();
startPythonSync();


    }


    private void startPythonSync() throws IOException
    {
        try {
            String cmd = "python3.6 /home/kiryushin/projects/python/stm2/stm.py";
            Process p = Runtime.getRuntime().exec(cmd);
        }
        catch (IOException e){e.printStackTrace();}

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
