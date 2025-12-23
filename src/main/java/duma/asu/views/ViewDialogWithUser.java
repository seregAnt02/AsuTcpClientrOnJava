package duma.asu.views;

import duma.asu.models.interfaces.SendDataParameter;
import duma.asu.models.serializableModels.DataFile;
import duma.asu.models.serializableModels.Parameter;

public class ViewDialogWithUser {

    public void responseMessageServer(SendDataParameter sendDataParameter){
        System.out.println("Ответ от сервера, в виде десериализаций объекта: " + sendDataParameter);
    }


    public void sendToServer(SendDataParameter sendDataParameter){
        if(sendDataParameter instanceof Parameter)
            System.out.println("Модель отправлена серверу, имя клиента: " + ((Parameter) sendDataParameter).getName());
        if(sendDataParameter instanceof DataFile)
            System.out.println("Модель отправлена серверу, имя файла: " + sendDataParameter.getNameFile());
    }


    public void toWhomIsMessage(){
        System.out.println("Кому сообщение(имя пользователя или all): ");
    }

    public void inputMessage(){
        System.out.println("Введите текст сообщения: ");
    }
}
