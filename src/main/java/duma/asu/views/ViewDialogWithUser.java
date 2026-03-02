package duma.asu.views;

import duma.asu.models.interfaces.AsuAndVideoData;
import duma.asu.models.serializableModels.DataFile;
import duma.asu.models.serializableModels.PR200;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ViewDialogWithUser {

    public void responseMessageServer(AsuAndVideoData sendDataParameter){
        Calendar calendar = new GregorianCalendar();
        if(sendDataParameter instanceof PR200){
            System.out.println(calendar.getTime() +
                    " Ответ от сервера, в виде десериализаций объекта: " + ((PR200)sendDataParameter).getName());
        }
        if(sendDataParameter instanceof  DataFile){
            System.out.println(calendar.getTime() +
                    " Ответ от сервера, в виде десериализаций объекта: " + sendDataParameter.getNameFile());
        }
    }


    public void sendToServer(AsuAndVideoData asuAndVideoData){
        if(asuAndVideoData instanceof PR200)
            System.out.println("\r\nМодель отправлена серверу, имя клиента: " + ((PR200) asuAndVideoData).getName());
        if(asuAndVideoData instanceof DataFile)
            System.out.println("Модель отправлена серверу, имя файла: " + ((DataFile)asuAndVideoData).getNameFile());
    }


    public void toWhomIsMessage(){
        System.out.println("Кому сообщение(имя пользователя или all): ");
    }

    public void inputMessage(){
        System.out.println("Введите текст сообщения: ");
    }
}
