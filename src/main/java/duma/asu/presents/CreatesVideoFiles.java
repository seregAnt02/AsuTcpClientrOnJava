package duma.asu.presents;

import java.util.HashMap;
import java.util.Map;

public class CreatesVideoFiles extends Thread {

    private int channel;
    private StartNewProcess startNewProcess;

    public CreatesVideoFiles(int channel) {
        this.channel = channel;
        startNewProcess = new StartNewProcess(this.channel);
    }


    @Override
    public void run() {
        try {
            startNewProcess.createProcess();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }


    public void startNewProcess() throws InterruptedException {
        startNewProcess.killProc();
        this.start();
    }
}
