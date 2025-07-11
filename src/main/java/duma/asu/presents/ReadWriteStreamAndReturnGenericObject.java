package duma.asu.presents;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ReadWriteStreamAndReturnGenericObject<T> {

    private final ObjectInputStream input;
    private final ObjectOutputStream output;


    public ReadWriteStreamAndReturnGenericObject(ObjectInputStream input, ObjectOutputStream output) {
        this.input = input;
        this.output = output;
    }


    public void modelSerializable(T sendDataParameter) throws IOException {
        this.output.writeUnshared(sendDataParameter);
        this.output.flush();
        this.output.reset();
    }


    public T modelDeserialization() throws IOException, ClassNotFoundException {
        T parameter = (T) input.readObject();
        return parameter;
    }
}
