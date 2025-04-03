package duma.asu.presents;

import duma.asu.models.AdressVideoChannel;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class CreateSendDeleteVideoFilesOnClient extends Thread {

    private StartNewProcess startNewProcess;

    private int channel;


    public static Map<Integer, Thread> array_processes = new HashMap<>();


    public CreateSendDeleteVideoFilesOnClient(int channel) {

        this.channel = channel;

        this.startNewProcess = new StartNewProcess();
    }


    @Override
    public void run() {
        try {
            this.startNewProcess.createProcess(channel);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }


    public void startNewProcess(){

        try{
            if(array_processes.entrySet()
                    .stream().anyMatch(n -> n.getKey() == this.channel)){

                for (Map.Entry<Integer, Thread> run: array_processes.entrySet()) {
                    if(run.getKey().equals(this.channel)){
                        Thread foo = run.getValue();
                        foo.interrupt();
                        array_processes.remove(run.getKey());
                        System.out.println("Процесс # " + foo + " удален -> " + foo.getState());
                    }
                }
            } else {

                Thread runnable = this;
                runnable.start();

                array_processes.put(this.channel, runnable);
            }

        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }


   /* public void createAnFiles(AdressVideoChannel adressVideo){

        Runnable task = () -> {
            this.startNewProcess.createProcess(adressVideo.getChannel());
        };
        Thread thread = new Thread(task);
        thread.start();
    }*/


    private void SendVideoDataOnVds(Object original_file, FileWriter stream_file, int index_file){}


    private void deleteFiles(){}
}
