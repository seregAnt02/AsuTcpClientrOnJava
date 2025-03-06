package duma.asu.presents;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ReadWriteStreamReturnGenericObject<T> {

    private final ObjectInputStream input;
    private final ObjectOutputStream output;


    public ReadWriteStreamReturnGenericObject(ObjectInputStream input, ObjectOutputStream output) {
        this.input = input;
        this.output = output;
    }


    public void modelSerializable(T sendDataParameter) throws IOException {
        this.output.writeObject(sendDataParameter);
        this.output.flush();
    }


    public T modelDeserialization() throws IOException, ClassNotFoundException {
        T parameter = (T) input.readObject();
        return parameter;
    }
}
