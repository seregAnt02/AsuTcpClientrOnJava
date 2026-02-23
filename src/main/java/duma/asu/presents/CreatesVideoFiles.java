package duma.asu.presents;

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
