package duma.asu.views;

import duma.asu.models.interfaces.SendDataParameter;

public class ViewDialogWithUser {

    public void responseMessageServer(SendDataParameter sendDataParameter){
        System.out.println("Ответ от сервера, в виде десериализаций объекта: " + sendDataParameter);
    }


    public void sendToServer(SendDataParameter sendDataParameter){
        System.out.println("Модель отправлена через сервер, к клиенту: " + sendDataParameter.getNameFile());
    }


    public void toWhomIsMessage(){
        System.out.println("Кому сообщение(имя пользователя или all): ");
    }

    public void inputMessage(){
        System.out.println("Введите текст сообщения: ");
    }
}
