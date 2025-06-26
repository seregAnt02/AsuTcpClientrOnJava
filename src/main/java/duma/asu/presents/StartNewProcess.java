package duma.asu.presents;

import duma.asu.models.AdressVideoChannel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class StartNewProcess {

    private Logger log;


    private Integer channel;

    public static Map<Integer, Process> array_processes = new HashMap<>();


    public StartNewProcess(int channel) {

        this.channel = channel;

        this.log = Logger.getLogger(StartNewProcess.class.getName());
    }

    private List<String> commandInput(AdressVideoChannel adressVideo){
        List<String> commands = new ArrayList<>();
        commands.add("ffmpeg");
        commands.add("-rtsp_transport");
        commands.add("tcp");
        commands.add("-i");
        commands.add("rtsp://192.168.0.89:554/user=serega&password=sergy7&channel=" + adressVideo.getChannel() + "&stream=" + adressVideo.getProtocol());
        commands.add("-r");
        commands.add("25");
        commands.add("-c:v");
        commands.add("copy");
        commands.add("-ss");
        commands.add("00:01");
        commands.add("-f");
        commands.add("dash");
        commands.add("-y");
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
                while ((line = input.readLine()) != null) {
                    if(line.startsWith("dash", 1)){
                        String[] splitted = line.split(" ");
                        if(splitted[3].equals("Opening"))
                            System.out.println(line);
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
