package duma.asu.presents;

import duma.asu.models.interfaces.AsuAndVideoData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class SerializationAndDeserialization implements Serializable {

    private final Socket socket;
    private ObjectOutputStream out;

    public SerializationAndDeserialization(Socket socket, ObjectOutputStream out) {
        this.socket = socket;
        this.out = out;
    }


    public AsuAndVideoData inputDeserialization(ObjectInputStream in){
        try {
            AsuAndVideoData parameter = (AsuAndVideoData) in.readUnshared();
            return parameter;
        }catch (IOException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        return null;
    }



    public void outSerialization(AsuAndVideoData asuAndVideoData) {
        try {
            out.writeObject(asuAndVideoData);
            out.flush();
            out.reset();
            asuAndVideoData = null;
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

}
