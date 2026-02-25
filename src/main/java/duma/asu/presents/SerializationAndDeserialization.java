package duma.asu.presents;

import duma.asu.models.interfaces.SendDataParameter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SerializationAndDeserialization {

    private final Socket socket;
    private ObjectOutputStream out;

    public SerializationAndDeserialization(Socket socket, ObjectOutputStream out) {
        this.socket = socket;
        this.out = out;
    }


    protected SendDataParameter InputDeserialization(ObjectInputStream in){
        try {
            SendDataParameter parameter = (SendDataParameter) in.readUnshared();
            return parameter;
        }catch (IOException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        return null;
    }



    protected void outSerialization(SendDataParameter sendDataParameter) {
        try {
            out.writeObject(sendDataParameter);
            out.flush();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

}
