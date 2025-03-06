package duma.asu.models.serializableModels;

import java.io.Serializable;
import java.util.Date;

public class SendDataObject implements Serializable {

    public String name;
    public Date dateTime;
    public Integer filesize;
    public String filename;
    public byte[] data;
    public String extension;
    public Integer indexFile;
    public Integer idNumberFolder;
    public Integer headerSize;

    public SendDataObject(String name) {
        this.name = name;
    }

    public String setExtension(String extension){
        return this.extension = extension;
    }
}
