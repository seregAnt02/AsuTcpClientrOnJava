package duma.asu.models.interfaces;

public interface AsuAndVideoData {

    String name = null;

    default String getNameFile(){ return name;}

}
