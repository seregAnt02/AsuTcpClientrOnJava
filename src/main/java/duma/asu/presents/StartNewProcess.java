package duma.asu.presents;

import duma.asu.models.AdressVideoChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
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

    private String commandInput(AdressVideoChannel adressVideo){

        String userHome = System.getProperty("user.home");
        String input_adress = "rtsp://192.168.0.89:554/user=serega&password=sergy7&channel=" + adressVideo.getChannel() + "&stream=" + adressVideo.getProtocol();
        //String output_adress = "/var/www/video/window_0/dash.mpd";
        String output_adress = userHome + "/projectJava/asuTcpClientOnJava/src/main/resources/video_content/";
        //String commands = "ffmpeg -rtsp_transport tcp -hide_banner -i " +  input_adress +  " -r 25 -c:v copy -ss 00:01 -f dash -y "  + output_adress;
        //String commands = "ffmpeg -version\n";
        String commands = "gedit\n";

        this.log.info(commands);

        return commands;
    }


    protected void createProcess(){

        try {
            //Process p = Runtime.getRuntime().exec("cmd /B start \"\" java -jar someOtherProgram.jar");
            ProcessBuilder builder = new ProcessBuilder();
            builder.command("sh"); //, "-c", "ls"
            Process process = builder.start();

            String command = commandInput(new AdressVideoChannel(channel, "TCP"));

            process = Runtime.getRuntime()
                    .exec(command);

            array_processes.put(channel, process);

            try(BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;

                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
            }

            builder = null;
            process = null;

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
