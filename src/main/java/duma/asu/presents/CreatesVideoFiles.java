package duma.asu.presents;

import java.util.HashMap;
import java.util.Map;

public class CreatesVideoFiles extends Thread {

    private int channel;

    public CreatesVideoFiles(int channel) {
        this.channel = channel;
    }


    @Override
    public void run() {
        try {
            new StartNewProcess(this.channel).createProcess();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }


    public void startNewProcess() throws InterruptedException {
        this.start();
        this.join();
        this.interrupt();
    }
}
