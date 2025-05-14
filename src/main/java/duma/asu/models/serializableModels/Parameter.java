package duma.asu.models.serializableModels;


import duma.asu.models.interfaces.SendDataParameter;

import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

public class Parameter implements Serializable, SendDataParameter {

    private static final long serialVersionUID = 1L;


    private UUID id;
    private Date datetime;
    private String name;
    private String codParameter;
    private String lastUpdate;
    private int meaning;

    private String message;
    private int dumaId;// внешний ключь


    public Parameter(String name, String extension) {
        this.name = name;
    }

    public String getNameFile(){ return name; }


    public void setName(String name) {
        this.name = name;
    }

    public int getMeaning(){ return meaning; }


    public int setMeaning(int meaning){ return this.meaning = meaning; }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "id=" + id +
                ", datetime=" + datetime +
                ", name='" + name + '\'' +
                ", codParameter='" + codParameter + '\'' +
                ", lastUpdate='" + lastUpdate + '\'' +
                ", meaning=" + meaning +
                ", message='" + message + '\'' +
                ", dumaId=" + dumaId +
                '}';
    }
}
