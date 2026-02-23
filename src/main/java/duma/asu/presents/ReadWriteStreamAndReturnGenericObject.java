package duma.asu.presents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ReadWriteStreamAndReturnGenericObject<T> {

    private final Socket socket;
    private ObjectOutputStream out;

    public ReadWriteStreamAndReturnGenericObject(Socket socket, ObjectOutputStream out) {
        this.socket = socket;
        this.out = out;
    }

    protected T InputDeserialization(ObjectInputStream in) throws IOException, ClassNotFoundException {
        T parameter = (T) in.readUnshared();
        return parameter;
    }


    protected void outSerialization(T sendDataParameter) throws IOException, ClassNotFoundException {
        out.writeObject(sendDataParameter);
        out.flush();
    }

}
