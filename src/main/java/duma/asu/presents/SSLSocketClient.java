package duma.asu.presents;

import duma.asu.models.interfaces.AsuAndVideoData;
import duma.asu.models.serializableModels.DataFile;
import duma.asu.models.serializableModels.PR200;
import duma.asu.presents.modbus.RTUModbus;
import duma.asu.views.ViewDialogWithUser;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.logging.Logger;

public class SSLSocketClient {

    private String host;
    private int port;

    public SerializationAndDeserialization serializationAndDeserialization;
    //private String name;

    static String PACKED_VIDEO_FILES;
    static String pathFileName;


    private ViewDialogWithUser viewDialogWithUser;

    private Logger log;
    

    public SSLSocketClient(String host, int port) throws IOException {

        // Указываем параметры хранилища ключей
        System.setProperty("javax.net.ssl.keyStore", "/home/serega02/cert_key/clientkeystore.p12");
        System.setProperty("javax.net.ssl.keyStorePassword", "password");
        // Настраиваем хранилище доверенных сертификатов
        System.setProperty("javax.net.ssl.trustStore", "/home/serega02/cert_key/clienttruststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "password");

        this.host = host;
        this.port = port;


        PACKED_VIDEO_FILES = "/var/www/video/";
        pathFileName = String.valueOf(Path.of(SSLSocketClient.PACKED_VIDEO_FILES));

        DeleteVideoFiles deleteVideoFiles = new DeleteVideoFiles();
        deleteVideoFiles.start();

        this.viewDialogWithUser = new ViewDialogWithUser();
    }


    public void runClient() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SocketFactory factory = SSLSocketFactory.getDefault();
                    try (SSLSocket socket = (SSLSocket) factory.createSocket(host, port)) {

                        socket.setEnabledCipherSuites(new String[]{"TLS_AES_128_GCM_SHA256"});
                        socket.setEnabledProtocols(new String[]{"TLSv1.3"});

                        if (!socket.isClosed()) {
                            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                            serializationAndDeserialization = new SerializationAndDeserialization(socket, out);

                            AsuAndVideoData asuAndVideoData = new PR200("Hello client!!!", null);//dataModelParameter();

                            serializationAndDeserialization.outSerialization(asuAndVideoData);
                            viewDialogWithUser.sendToServer(asuAndVideoData);

                            listenForModel(socket);

                            asuAndVideoData = null;
                            out = null;
                            Scanner scanner = new Scanner(System.in);
                            System.out.print("Введите символ(ы), для выхода из программы: \r\n");
                            scanner.nextLine();
                        }

                    }
                } catch (Exception e) {
                    System.out.println( "\r\nException -> " + e.getMessage());
                }
            }
        }).start();
    }

    public void sendDataToServer(AsuAndVideoData asuAndVideoData){
        serializationAndDeserialization.outSerialization(asuAndVideoData);
        viewDialogWithUser.sendToServer(asuAndVideoData);
        asuAndVideoData = null;
    }


    
    private void listenForModel(Socket socket){
        new Thread(() -> {
            try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())){
            while (!socket.isClosed()){
                    AsuAndVideoData asuAndVideoData =
                            serializationAndDeserialization.inputDeserialization(in);
                    commandSwitch(asuAndVideoData);
                    viewDialogWithUser.responseMessageServer(asuAndVideoData);
                    asuAndVideoData = null;
                }
            } catch (IOException e){
                closeEverything(socket);
            }
        }).start();
    }

    private PR200 dataModelParameter() throws IOException {

        PR200 parameter = new PR200("asd", null);
        //parameter.setName("asd");
        parameter.setMeaning(3);
        return parameter;
    }
    private void commandSwitch(AsuAndVideoData asuAndVideoData){

        try {
            if (asuAndVideoData instanceof PR200) {
                RTUModbus rtu = new RTUModbus(this);
                rtu.start();
                rtu = null;
            }
            if (asuAndVideoData instanceof DataFile) {
                DataFile dataFile = (DataFile) asuAndVideoData;
                CreatesVideoFiles createsVideoFiles = new CreatesVideoFiles(dataFile.getChannel(), this);
                createsVideoFiles.startNewProcess();
                dataFile = null;
                createsVideoFiles = null;
            }
            asuAndVideoData = null;
        }catch (IOException | InterruptedException e){
            System.out.println(e.getMessage());
        }
    }

    private void closeEverything(Socket socket) {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
