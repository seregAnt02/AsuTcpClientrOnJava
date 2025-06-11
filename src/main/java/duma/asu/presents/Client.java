package duma.asu.presents;

import duma.asu.models.interfaces.SendDataParameter;
import duma.asu.models.serializableModels.DataFile;
import duma.asu.models.serializableModels.Parameter;
import duma.asu.views.ViewDialogWithUser;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Path;
import java.util.logging.Logger;

public class Client {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String name;

    static String PACKED_VIDEO_FILES;
    static String pathFileName;
    private ReadWriteStreamAndReturnGenericObject<SendDataParameter> readWriteStreamReturnGenericObject;

    private ViewDialogWithUser viewDialogWithUser;



    private Logger log;

    public Client(Socket socket,  String userName) throws IOException, ClassNotFoundException {
        this.socket = socket;
        this.name = userName;
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());

        PACKED_VIDEO_FILES = "/src/main/resources/video_content/";
        String userDirectory = System.getProperty("user.dir");
        pathFileName = String.valueOf(Path.of(userDirectory + Client.PACKED_VIDEO_FILES));
        this.readWriteStreamReturnGenericObject = new ReadWriteStreamAndReturnGenericObject(this.input, output);

        DeleteVideoFiles deleteVideoFiles = new DeleteVideoFiles();
        deleteVideoFiles.start();

        this.viewDialogWithUser = new ViewDialogWithUser();


        this.log = Logger.getLogger(Client.class.getName());
    }


    private void commandSwitch(SendDataParameter sendDataParameter) throws IOException, InterruptedException {

        if(sendDataParameter instanceof Parameter){
            log.info(Parameter.class.getName());
        }
        if(sendDataParameter instanceof DataFile) {
            DataFile dataFile = (DataFile) sendDataParameter;
            CreatesVideoFiles createsVideoFiles = new CreatesVideoFiles(dataFile.getChannel());
            createsVideoFiles.startNewProcess();
            new SendVideoFiles(this, dataFile).start_send_video_thread_to_server();
            log.info(DataFile.class.getName());
        }

    }

    public void sendVideoFilesToServer(SendDataParameter sendDataParameter) throws IOException {
        readWriteStreamReturnGenericObject.modelSerializable(sendDataParameter);
        viewDialogWithUser.sendToServer(sendDataParameter);
    }

    public void SendDataToServer(){
        try {
            Parameter parameter = new Parameter();
            parameter.setName("asd");
            parameter.setMeaning(3);
            SendDataParameter sendDataParameter = parameter;
            readWriteStreamReturnGenericObject.modelSerializable(sendDataParameter);
            //viewDialogWithUser.sendToServer(sendDataParameter);
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
                    } catch (IOException | ClassNotFoundException | InterruptedException e){
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
