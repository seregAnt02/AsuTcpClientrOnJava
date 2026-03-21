package duma.asu.presents;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class CreatesVideoFiles extends Thread {
    private int channel;
    private StartNewProcess startNewProcess;

    public CreatesVideoFiles(int channel, SSLSocketClient client) throws SocketException, UnknownHostException {
        this.channel = channel;
        startNewProcess = new StartNewProcess(this.channel, client);
    }


    @Override
    public void run() {
        try {
            System.out.println("Start new thread name: " + this.getName());
            startNewProcess.createProcess();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }


    public void startNewProcess() throws InterruptedException, IOException {
        startNewProcess.killProc();
        //startNewProcess.closeStream();
        this.start();
    }
}
