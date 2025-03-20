package duma.asu.models.serializableModels;

import duma.asu.models.interfaces.SendDataParameter;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Duma implements Serializable, SendDataParameter {

    private static final long serialVersionUID = 3L;

    public UUID id;
    public String guid;
    public Date datetime;
    public String macAdress;
    public String ipAdress;
    public int port;
    public String status;
    public int number;
    public String migration;
    public int age;
}
