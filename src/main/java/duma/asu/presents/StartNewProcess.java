package duma.asu.presents;

import duma.asu.models.AdressVideoChannel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.logging.Logger;

public class StartNewProcess {
    private Client client;
    private Logger log;
    private Integer channel;
    public static Map<Integer, Process> array_processes = new HashMap<>();


    public StartNewProcess(int channel, Client client) throws SocketException, UnknownHostException {
        this.client = client;
        this.channel = channel;
        this.log = Logger.getLogger(StartNewProcess.class.getName());
    }

    private List<String> commandInput(AdressVideoChannel adressVideo){
        List<String> commands = new ArrayList<>();
        commands.add("ffmpeg");
        commands.add("-rtsp_transport");
        commands.add("tcp");
        commands.add("-i");
        commands.add("rtsp://192.168.0.89:554/user=user&password=password&channel=" + adressVideo.getChannel() + "&stream=" + adressVideo.getProtocol());
        commands.add("-r");
        commands.add("25");
        commands.add("-c:v");
        commands.add("copy");
        commands.add("-ss");
        commands.add("00:01");
        commands.add("-f");
        commands.add("dash");
        commands.add("-y");
        //String path = System.getProperty("${custom.config.dir}");
        commands.add(Client.pathFileName + "/dash.mpd");
        this.log.info(commands.toString());
        return commands;
    }


    protected void createProcess(){
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.redirectErrorStream(true);
            List<String> command = commandInput(new AdressVideoChannel(channel, "TCP"));
            builder.command(command);
            Process process = builder.start();
            array_processes.put(channel, process);
            try(BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                List<String> array_packed_files = new ArrayList<>();
                while ((line = input.readLine()) != null) {
                    if(line.startsWith("dash", 1)){
                        String[] split_strip = line.split(" ");
                        if(split_strip[3].equals("Opening")){
                            String path_file = split_strip[4].substring(1, split_strip[4].length() - 1);
                            array_packed_files.add(path_file);
                            // строку сравнения переделать
                            if(path_file.equals("/home/serega02/projectJava/asuTcpClientOnJava/src/main/resources/video_content/dash.mpd.tmp")){
                                new SendVideoFiles(client, array_packed_files).start_send_video_thread_to_server();
                                /*System.out.println(array_packed_files);
                                array_packed_files.clear();*/
                                System.out.println(line);
                            }
                        }
                    }
                }
                line = null;
                input.close();
            }
            builder = null;
            process = null;
            command = null;

        } catch (Exception err) {
            err.printStackTrace();
        }
    }


    protected void killProc(){
        try {
            for (Map.Entry<Integer, Process> run: array_processes.entrySet()){
                if(run.getKey().equals(this.channel)){
                    Process process = run.getValue();

                    process.destroy();

                    array_processes.remove(run.getKey());

                    System.out.println("Процесс " + process.pid() + " удален.");

                    run = null;
                    process = null;
                }
            }
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
