package duma.asu.models;

import java.io.Serializable;

public class Message implements Serializable {

    public Message(String name, String toName, String massage) {
        this.name = name;
        this.toName = toName;
        this.massage = massage;
    }

    private static final long serialVersionUID = 1L;
    private String name;
    private String toName;
    private String massage;

    public String getName() {
        return name;
    }

    public String getToName() {
        return toName;
    }

    public String getMassage() {
        return massage;
    }

    @Override
    public String toString() {
        return "Message{" +
                "name='" + name + '\'' +
                ", toName='" + toName + '\'' +
                ", massage='" + massage + '\'' +
                '}';
    }
}
