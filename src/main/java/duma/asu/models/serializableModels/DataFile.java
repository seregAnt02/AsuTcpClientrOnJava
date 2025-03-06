package duma.asu.models.serializableModels;

import duma.asu.models.interfaces.SendDataParameter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

public class DataFile implements Serializable, SendDataParameter {

    private static final long serialVersionUID = 2L;


    public String nameFile;
    public Date dateTime;
    public Integer filesize;
    public byte[] data;
    public String extension;
    public Integer indexFile;
    public Integer idNumberFolder;
    public Integer headerSize;


    public DataFile(String nameFile) {
        this.nameFile = nameFile;
    }


    public String getName(){ return this.nameFile; }


    public String setExtension(String extension){
        return this.extension = extension;
    }

    @Override
    public String toString() {
        return "DataFile{" +
                "nameFile='" + nameFile + '\'' +
                ", dateTime=" + dateTime +
                ", filesize=" + filesize +
                ", data=" + Arrays.toString(data) +
                ", extension='" + extension + '\'' +
                ", indexFile=" + indexFile +
                ", idNumberFolder=" + idNumberFolder +
                ", headerSize=" + headerSize +
                '}';
    }
}
