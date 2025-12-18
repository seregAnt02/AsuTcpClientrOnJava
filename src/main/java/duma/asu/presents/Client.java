package duma.asu.presents;

import duma.asu.models.interfaces.SendDataParameter;
import duma.asu.models.serializableModels.DataFile;
import duma.asu.models.serializableModels.Parameter;
import duma.asu.presents.modbus.RTUModbus;
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

    protected ReadWriteStreamAndReturnGenericObject<SendDataParameter> readWriteStreamReturnGenericObject;

    private ViewDialogWithUser viewDialogWithUser;



    private Logger log;


    public Client(){}

    public Client(Socket socket,  String userName) throws IOException, ClassNotFoundException {
        this.socket = socket;
        this.name = userName;
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());
        this.readWriteStreamReturnGenericObject = new ReadWriteStreamAndReturnGenericObject(this.input, output);

        PACKED_VIDEO_FILES = "/src/main/resources/video_content/";
        String userDirectory = System.getProperty("user.dir");
        pathFileName = String.valueOf(Path.of(userDirectory + Client.PACKED_VIDEO_FILES));
        /*DeleteVideoFiles deleteVideoFiles = new DeleteVideoFiles();
        deleteVideoFiles.start();*/
        this.viewDialogWithUser = new ViewDialogWithUser();
        this.log = Logger.getLogger(Client.class.getName());
    }


    public void sendDataToServer(SendDataParameter sendDataParameter) throws IOException {
        readWriteStreamReturnGenericObject.modelSerializable(sendDataParameter);
        viewDialogWithUser.sendToServer(sendDataParameter);
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
                        sendDataParameter = null;
                    } catch (IOException | ClassNotFoundException |InterruptedException e){
                        closeEverything(socket);
                    }
                }
            }
        }).start();
    }

    private void commandSwitch(SendDataParameter sendDataParameter) throws IOException, InterruptedException {

        if(sendDataParameter instanceof Parameter){
            RTUModbus rtu = new RTUModbus(this);
            rtu.start();
        }
        if(sendDataParameter instanceof DataFile) {
            DataFile dataFile = (DataFile) sendDataParameter;
            CreatesVideoFiles createsVideoFiles = new CreatesVideoFiles(dataFile.getChannel(), this);
            createsVideoFiles.startNewProcess();
            //new SendVideoFiles(this, dataFile).start_send_video_thread_to_server();
            //log.info(DataFile.class.getName());
        }
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
