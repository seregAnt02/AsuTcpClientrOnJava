package duma.asu.presents;

import duma.asu.models.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String name;

    public Client(Socket socket,  String userName) throws IOException, ClassNotFoundException {
        this.socket = socket;
        this.name = userName;
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());
    }

    public void sendMessage(){
        try {

            Message modelMessage = new Message(name,null, null );
            output.writeObject(modelMessage);
            output.flush();
            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()){
                System.out.println("Кому сообщение(имя пользователя или all): ");
                String toUser = scanner.nextLine();
                System.out.println("Введите текст сообщения: ");
                String messageOut = scanner.nextLine();
                modelMessage = new Message(name, toUser, messageOut);
                modelSerializable(modelMessage);
                System.out.println("модель отправлена через сервер, к клиенту: " + modelMessage);
            }
        } catch (IOException e){
            closeEverything(socket);
        }
    }

    private void modelSerializable(Message modelMessage) throws IOException {
        this.output.writeObject(modelMessage);
        this.output.flush();
    }

    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message modelMessage;
                while (socket.isConnected()){
                    try {
                        modelMessage = (Message) input.readObject();
                        System.out.println("ответ от сервера, в виде десериализаций объекта: " + modelMessage);
                    } catch (IOException | ClassNotFoundException e){
                        closeEverything(socket);
                    }
                }
            }
        }).start();
    }


    private void closeEverything(Socket socket) {
        try {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
