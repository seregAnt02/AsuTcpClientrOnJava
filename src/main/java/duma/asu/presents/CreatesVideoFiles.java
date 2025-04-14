package duma.asu.presents;

import java.util.HashMap;
import java.util.Map;

public class CreatesVideoFiles extends Thread {

    private StartNewProcess startNewProcess;

    private int channel;


    private static Map<Integer, Thread> array_threads = new HashMap<>();


    public CreatesVideoFiles(int channel) {

        this.channel = channel;

        this.startNewProcess = new StartNewProcess(channel);
    }


    @Override
    public void run() {
        try {
            this.startNewProcess.createProcess();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }


    public void startNewProcess(){

        try{
            if(array_threads.entrySet()
                    .stream().anyMatch(n -> n.getKey() == this.channel)){

                for (Map.Entry<Integer, Thread> run: array_threads.entrySet()) {
                    if(run.getKey().equals(this.channel)){

                        this.startNewProcess.killProc();

                        Thread thread = run.getValue();
                        thread.interrupt();
                        array_threads.remove(run.getKey());
                        System.out.println("Поток # " + thread + " удален -> " + thread.getState());

                        run = null;
                        thread = null;
                    }
                }
            }

            Thread runnable = this;
            runnable.start();

            array_threads.put(this.channel, runnable);

            runnable = null;

        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
