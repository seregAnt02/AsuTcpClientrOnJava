package duma.asu.models.serializableModels;

import duma.asu.models.interfaces.SendDataParameter;

import java.io.Serializable;
import java.sql.Date;

public class Parameter implements Serializable, SendDataParameter {

    private static final long serialVersionUID = 1L;


    public int id;
    public Date datetime;
    public String name;
    public String codParameter;
    public String lastUpdate;
    public int meaning;
    public int dumaId;// внешний ключь
    //public Duma duma  = new Duma();//навигационное свойство


    public Parameter(String name) {
        this.name = name;
    }


    public String getName(){ return this.name; }


    public int getMeaning(){ return this.meaning; }


    public int setMeaning(int meaning){ return this.meaning = meaning; }


    @Override
    public String toString() {
        return "Parameter{" +
                "id=" + id +
                ", datetime=" + datetime +
                ", name='" + name + '\'' +
                ", codParameter='" + codParameter + '\'' +
                ", lastUpdate='" + lastUpdate + '\'' +
                ", meaning=" + meaning +
                ", dumaId=" + dumaId +
                '}';
    }
}