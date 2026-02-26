package duma.asu.models.serializableModels;

import duma.asu.models.interfaces.AsuAndVideoData;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class PR200 implements Serializable, AsuAndVideoData {

    private static final long serialVersionUID = 1L;


    private UUID id;
    private Date datetime;
    private String name;
    private String codParameter;
    private String lastUpdate;
    private int meaning;

    private String message;
    private int dumaId;// внешний ключь


    public PR200(String name, String extension) {
        this.name = name;
    }

    public String getName(){ return name; }


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
