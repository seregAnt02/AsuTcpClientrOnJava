package duma.asu.models.serializableModels;

import java.io.Serializable;
import java.sql.Date;

public class Parameter implements Serializable {

    public Parameter(String name) {
        this.name = name;
    }

    private static final long serialVersionUID = 1L;


    public int id;
    public Date datetime;
    public String name;
    public String codParameter;
    public String lastUpdate;
    public int meaning;
    public int dumaId;// внешний ключь
    //public Duma duma  = new Duma();//навигационное свойство

    public String getName(){ return name; }
}