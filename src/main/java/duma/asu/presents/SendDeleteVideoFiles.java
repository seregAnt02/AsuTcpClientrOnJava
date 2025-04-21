package duma.asu.presents;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class SendDeleteVideoFiles extends Thread{


    private DatagramSocket socket;
    private InetAddress address;


    private static Map<Integer, Thread> array_threads = new HashMap<>();

    private int channel;

    private Logger log;

    public SendDeleteVideoFiles(int channel) throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");

        this.channel = channel;

        this.log = Logger.getLogger(StartNewProcess.class.getName());
    }

    @Override
    public void run() {

       try {

           convert_video_files_to_byte();

       } catch (Exception ex){
           log.info(ex.getMessage());
       }
    }


    public void start_send_video_thread_to_server(){
        try{
            if(array_threads.entrySet()
                    .stream().anyMatch(n -> n.getKey() == this.channel)){
                for (Map.Entry<Integer, Thread> run: array_threads.entrySet()) {
                    if(run.getKey().equals(this.channel)){
                        Thread thread = run.getValue();
                        thread.interrupt();

                        array_threads.remove(run.getKey());
                        System.out.println("Поток # " + thread + " удален -> " + thread.getState());
                        run = null;
                        thread = null;
                    }
                }
            }
            array_threads.put(this.channel, this);

        }catch (Exception ex){
            log.info(ex.getMessage());
        }
    }


    private void convert_video_files_to_byte(){
        String packed_video_files = "/src/main/resources/video_content/";
        String userDirectory = System.getProperty("user.dir");
        File files = new File(userDirectory + packed_video_files);
        File[] array_files = files.listFiles();
        int header_length = 4;
        for (int i = 0; i < array_files.length; i++){
            try (FileInputStream inputStream = new FileInputStream(array_files[i])) {
                ByteBuffer length_file = ByteBuffer.allocate(header_length).putInt((int)array_files[i].length());
                byte[] file_name = array_files[i].getName().getBytes();
                byte[] array_byte_in_file = new byte[(int) (header_length +
                        file_name.length + array_files[i].length())];
                IntStream.range(0, header_length)
                        .forEach(x -> array_byte_in_file[x] = length_file.array()[x]);
                IntStream.range(0, file_name.length)
                        .forEach(x -> array_byte_in_file[x + header_length] = file_name[x]);
                inputStream.read(array_byte_in_file, header_length + file_name.length,
                        (int) array_files[i].length());

                sendVideoDataOnServer(array_byte_in_file);

            } catch (IOException ex) {
                log.info(ex.getMessage());
            }
        }
        packed_video_files = null;
        userDirectory = null;
        files = null;
        array_files = null;
        close();
    }


    private void sendVideoDataOnServer(byte[] buf){

        try{
            DatagramPacket packet
                    = new DatagramPacket(buf, buf.length, address, 4445);
            socket.send(packet);

            Thread.sleep(100);

            packet = null;

        }catch (Exception ex){
            close();
            System.out.println(ex.getMessage());
        }
    }


    private void deleteFiles(){}


    private void close() {
        socket.close();
    }

}
