package duma.asu.presents;

import duma.asu.models.interfaces.SendDataParameter;
import duma.asu.models.serializableModels.Parameter;
import duma.asu.views.ViewDialogWithUser;

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

    private ReadWriteStreamAndReturnGenericObject readWriteStreamReturnGenericObject;

    private ViewDialogWithUser viewDialogWithUser;

    public Client(Socket socket,  String userName) throws IOException, ClassNotFoundException {
        this.socket = socket;
        this.name = userName;
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());

        readWriteStreamReturnGenericObject = new ReadWriteStreamAndReturnGenericObject(this.input, output);

        viewDialogWithUser = new ViewDialogWithUser();
    }

    void commandSwitch(){

    }


    public void SendDataToServer(){
        try {

            Parameter parameter = new Parameter(this.name, null);
            parameter.setMeaning(3);
            SendDataParameter sendDataParameter = parameter;
            readWriteStreamReturnGenericObject.modelSerializable(sendDataParameter);

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()){
                viewDialogWithUser.toWhomIsMessage();

                String toUser = scanner.nextLine();

                viewDialogWithUser.inputMessage();

                String messageOut = scanner.nextLine();

                parameter = new Parameter(this.name, null);
                parameter.setName(toUser);
                parameter.setMessage(messageOut);
                sendDataParameter = parameter;

                readWriteStreamReturnGenericObject.modelSerializable(sendDataParameter);

                viewDialogWithUser.sendToServer(sendDataParameter);
            }
        } catch (IOException e){
            closeEverything(socket);
        }
    }


    public void listenForModel(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()){
                    try {
                        SendDataParameter sendDataParameter =
                                (Parameter)readWriteStreamReturnGenericObject.modelDeserialization();
                        viewDialogWithUser.responseMessageServer(sendDataParameter);
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
