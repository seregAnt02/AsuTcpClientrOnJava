package duma.asu.models.interfaces;

public interface SendDataParameter {

    String name = null;


    default String getNameFile(){ return name;}
}
