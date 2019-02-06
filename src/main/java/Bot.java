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
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Bot extends TelegramLongPollingBot
{


/*
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
*/



    public void sendMsq(Message message, String text)
    {

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);

        sendMessage.setChatId(message.getChatId().toString());//определяем на какой конкретный чат определяем ответ
            sendMessage.setReplyToMessageId(message.getMessageId());
                sendMessage.setText(text);


                    try { execute(sendMessage); }

                    catch(TelegramApiException e) { e.printStackTrace(); }



    }


    public void onUpdateReceived(Update update)
    {

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

        //если в сообщении от пользователя содержится фото
        else if (update.hasMessage()&& update.getMessage().hasPhoto()) {


            PhotoSize photo = getPhoto(update);
            System.out.println("прошло фото ");


            String photoFilePath = null;
            try {
                photoFilePath = getFilePath(photo);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("прошел path");
                                                                        java.io.File file = DownloadPhotoInDirectory(photoFilePath);

            try {
                saveImageinDIrectory(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
//для приема сообщение и обновлений через лонг пул





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

                    uploadFile("waxwax",photo.getFileId());

                    try {
                        File file = execute(getFileMethod);
                        return file.getFilePath();
                         }
                          catch (TelegramApiException e){ e.printStackTrace();}
                    System.out.println("path");

                }


     return null;

    }

private java.io.File DownloadPhotoInDirectory(String filePath)
            {
                try
                {

                    System.out.println("download");




                    return downloadFile(filePath);


                }
                catch(TelegramApiException e){e.printStackTrace();}


            return null;

            }


public void saveImageinDIrectory(java.io.File file) throws IOException
{

    ImageIO.write(ImageIO.read(file),"png",file);


}


    public void uploadFile(String file_name, String file_id) throws IOException
    {


        URL url = new URL("https://api.telegram.org/bot"+getBotToken()+"/getFile?file_id="+file_id);

        BufferedReader in = new BufferedReader(new InputStreamReader( url.openStream()));
        String res = in.readLine();

        JSONObject jresult = new JSONObject(res);
        JSONObject path = jresult.getJSONObject("result");


        String file_path = path.getString("file_path");



        URL download = new URL("https://api.telegram.org/file/bot" + getBotToken() + "/" + file_path);

        FileOutputStream fos = new FileOutputStream(file_id + file_name);


        System.out.println("Start upload");
        ReadableByteChannel rbc = Channels.newChannel(download.openStream());

        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();

     //   uploadFlag = 0;

        System.out.println("Uploaded!");
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
