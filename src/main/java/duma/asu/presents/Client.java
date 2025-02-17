package duma.asu.presents;

import duma.asu.models.serializableModels.Message;
import duma.asu.models.serializableModels.Parameter;

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

    public void sendModel(){
        try {

            Parameter parameter = new Parameter(this.name);
            output.writeObject(parameter);
            output.flush();
            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()){
                System.out.println("Кому сообщение(имя пользователя или all): ");
                String toUser = scanner.nextLine();
                System.out.println("Введите текст сообщения: ");
                String messageOut = scanner.nextLine();
                parameter = new Parameter(name);
                modelSerializable(parameter);
                System.out.println("модель отправлена через сервер, к клиенту: " + parameter);
            }
        } catch (IOException e){
            closeEverything(socket);
        }
    }

    private void modelSerializable(Parameter parameter) throws IOException {
        this.output.writeObject(parameter);
        this.output.flush();
    }

    public void listenForModel(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()){
                    try {
                        Parameter parameter = (Parameter) input.readObject();
                        System.out.println("ответ от сервера, в виде десериализаций объекта: " + parameter);
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
