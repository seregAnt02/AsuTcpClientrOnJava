package duma.asu.presents;

import duma.asu.models.interfaces.SendDataParameter;
import duma.asu.models.serializableModels.DataFile;

import java.io.*;
import java.net.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

public class SendVideoFiles extends Thread{


    private Client client;

    private int channel;

    static String pathFileName;
    private Logger log;

    public SendVideoFiles(Client client, DataFile dataFile) throws SocketException, UnknownHostException {
        this.client = client;
        this.channel = dataFile.getChannel();
        String packed_video_files = "/src/main/resources/video_content/";
        String userDirectory = System.getProperty("user.dir");
        pathFileName = String.valueOf(Path.of(userDirectory + packed_video_files));
        this.log = Logger.getLogger(StartNewProcess.class.getName());
    }

    @Override
    public void run() {

       try {
           File file_obj = new File(pathFileName);
           convert_file_in_object(file_obj);
           /*for(int i = 0; i < 10; i++){
               //convert_video_files_to_byte(files);
               client.sendVideoFilesToServer(file_obj);
               Thread.sleep(2000);
           }*/
           Thread.sleep(100);
           file_obj = null;

       } catch (Exception ex){
           log.info(ex.getMessage());
       }
    }


    public void start_send_video_thread_to_server() throws InterruptedException {
       this.start();
       this.join();
       this.interrupt();
    }


    private void convert_file_in_object(File file_obj){
        int header_length = 4;
        for (File file : file_obj.listFiles()) {
            try (FileInputStream inputStream = new FileInputStream(file)) {
                DataFile dataFile = new DataFile();
                dataFile.setChannel(2);
                dataFile.setData(new byte[(int) (file.length())]);
                dataFile.setNameFile(file.getName());
                inputStream.read(dataFile.getData(), 0, (int)file.length());

                client.sendVideoFilesToServer((SendDataParameter)dataFile);
                System.out.println("send file to server: " + file.getName() + "\r\n");
                file = null;
                dataFile = null;

            } catch (IOException ex) {
                log.info(ex.getMessage());
            }
        }
    }

    public Set<File> listFilesUsingDirectoryStream(String dir) throws IOException {
        Set<File> fileSet = new HashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    fileSet.add(path.getFileName().toFile());
                }
            }
        }
        return fileSet;
    }


    private void deleteFiles(File file){
        if(!file.getName().equals("dash.mpd"))
                file.delete();
        System.out.println("Удаление файла: " + file.getName());
    }


    /*private void close() {
        socket.close();
        System.out.println( "socket.isConnected(): " + socket.isConnected());
    }*/

}
