package duma.asu.presents;

import duma.asu.models.AdressVideoChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class StartNewProcess {

    private Logger log;


    public StartNewProcess() {

        this.log = Logger.getLogger(StartNewProcess.class.getName());
    }

    private String commandInput(AdressVideoChannel adressVideo){

        String userHome = System.getProperty("user.home");
        String input_adress = "rtsp://192.168.0.89:554/user=serega&password=sergy7&channel=" + adressVideo.getChannel() + "&stream=" + adressVideo.getProtocol();
        //String output_adress = "/var/www/video/window_0/dash.mpd";
        String output_adress = userHome + "/projectJava/asuTcpClientOnJava/src/main/resources/video_content";
        //String commands = "ffmpeg -rtsp_transport tcp -hide_banner -i " +  input_adress +  " -r 25 -c:v copy -ss 00:01 -f dash -y "  + output_adress;
        String commands = "ffmpeg -version\n";

        this.log.info(commands);

        return commands;
    }


    public void createProcess(int channel){

        ProcessBuilder builder = null; Process process = null;
        try {
            //Process p = Runtime.getRuntime().exec("cmd /B start \"\" java -jar someOtherProgram.jar");
            builder = new ProcessBuilder();
            builder.command("sh"); //, "-c", "ls"
            process = builder.start();

            String command = commandInput(new AdressVideoChannel(channel, "TCP"));

            process = Runtime.getRuntime()
                    .exec(command);

            try(BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;

                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
            }
            //System.exit(0);

        } catch (Exception err) {
            err.printStackTrace();
        }
        finally {

            builder = null;
            process.destroy();
        }
    }


    private void killProc(){

    }
}
