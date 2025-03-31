package duma.asu.presents;

import duma.asu.models.AdressVideoChannel;

import java.io.FileWriter;

public class CreateSendDeleteVideoFilesOnClient{

    private StartNewProcess startNewProcess;

    public CreateSendDeleteVideoFilesOnClient() {
        this.startNewProcess = new StartNewProcess();
    }


    public void createAnFiles(AdressVideoChannel adressVideo){

        Runnable task = () -> {
            this.startNewProcess.createProcess(adressVideo.getChannel());
        };
        Thread thread = new Thread(task);
        thread.start();
    }


    private void SendVideoDataOnVds(Object original_file, FileWriter stream_file, int index_file){}


    private void deleteFiles(){}
}
