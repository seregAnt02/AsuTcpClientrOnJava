package duma.asu.presents;

import duma.asu.models.interfaces.AsuAndVideoData;
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
    private SSLSocketClient client;
    private Logger log;
    private List<String> array_packed_files;

    public SendVideoFiles(SSLSocketClient client, List<String> packedFile) throws SocketException, UnknownHostException {
        this.client = client;
        this.log = Logger.getLogger(StartNewProcess.class.getName());
        this.array_packed_files = packedFile;
    }

    @Override
    public void run() {

       try {
           for(String packed : this.array_packed_files){
               String[] split_packed = packed.split("/");
               String path_file = "/" + split_packed[1] + "/" + split_packed[2] + "/" + split_packed[3] + "/" +
                                  split_packed[4] + "/" + split_packed[5] + "/" + split_packed[6] + "/" +
                                  split_packed[7] + "/" + split_packed[8] + "/";
               Thread.sleep(100);
               File directory = new File(path_file);
               String[] split_extension = split_packed[9].split("\\.");
               convert_file_in_object(directory, split_extension[0] + "." + split_extension[1]);
               directory = null;
               split_extension = null;
               path_file = null;
               split_packed = null;
               packed = null;
           }
       } catch (Exception ex){
           log.info(ex.getMessage());
       }
    }


    public void start_send_video_thread_to_server() throws InterruptedException {
       this.start();
       this.join();
       this.interrupt();
    }


    private void convert_file_in_object(File directory, String file_name) throws InterruptedException {
        int header_length = 4;
            File file = Arrays.stream(directory.listFiles()).filter(n -> n.getName().equals(file_name))
                    .findAny().orElse(null);
            if (file != null)
                try (FileInputStream inputStream = new FileInputStream(file)) {
                    DataFile dataFile = new DataFile();
                    dataFile.setChannel(2);
                    dataFile.setData(new byte[(int) (file.length())]);
                    dataFile.setNameFile(file.getName());
                    inputStream.read(dataFile.getData(), 0, (int) file.length());
                    synchronized (this) {
                        this.client.sendDataToServer((AsuAndVideoData) dataFile);
                    }
                    if(file.delete()) System.out.println("Удален файл: " + file.getName());
                    file = null;
                    dataFile = null;

                } catch (IOException ex) {
                    log.info(ex.getMessage());
                }
            Thread.sleep(100);
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


    /*private void close() {
        socket.close();
        System.out.println( "socket.isConnected(): " + socket.isConnected());
    }*/

}
