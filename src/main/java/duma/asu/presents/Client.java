package duma.asu.presents;

import duma.asu.models.interfaces.SendDataParameter;
import duma.asu.models.serializableModels.DataFile;
import duma.asu.models.serializableModels.Parameter;
import duma.asu.views.ViewDialogWithUser;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Logger;

public class Client {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String name;

    private ReadWriteStreamAndReturnGenericObject<SendDataParameter> readWriteStreamReturnGenericObject;

    private ViewDialogWithUser viewDialogWithUser;



    private Logger log;

    public Client(Socket socket,  String userName) throws IOException, ClassNotFoundException {
        this.socket = socket;
        this.name = userName;
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());

        this.readWriteStreamReturnGenericObject = new ReadWriteStreamAndReturnGenericObject(this.input, output);


        this.viewDialogWithUser = new ViewDialogWithUser();


        this.log = Logger.getLogger(Client.class.getName());
    }


    private void commandSwitch(SendDataParameter sendDataParameter){

        try{
            if(sendDataParameter instanceof Parameter){
                log.info(Parameter.class.getName());
            }

            if(sendDataParameter instanceof DataFile) {

                DataFile dataFile = (DataFile) sendDataParameter;

                new CreatesVideoFiles(dataFile.getChannel()).startNewProcess();

                new SendDeleteVideoFiles(dataFile.getChannel()).start_send_video_thread_to_server();

                log.info(DataFile.class.getName());
                log.info(dataFile.toString());
            }

        }catch (IOException ex){
            log.info(ex.getMessage());
        }
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
                                (SendDataParameter) readWriteStreamReturnGenericObject.modelDeserialization();

                        commandSwitch(sendDataParameter);

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
