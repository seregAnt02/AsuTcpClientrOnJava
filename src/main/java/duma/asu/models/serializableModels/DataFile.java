package duma.asu.models.serializableModels;

import duma.asu.models.interfaces.SendDataParameter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class DataFile implements Serializable, SendDataParameter {

    private static final long serialVersionUID = 2L;


    private UUID id;
    private String nameFile;
    private Date dateTime;
    private Integer filesize;
    private byte[] data;
    private String extension;
    private Integer channel;
    private Integer numberFolder;
    private Integer headerSize;


    public DataFile(String nameFile) {
        this.nameFile = nameFile;
    }


    public String getName(){ return this.nameFile; }


    public String setExtension(String extension){
        return this.extension = extension;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "DataFile{" +
                "id=" + id +
                ", nameFile='" + nameFile + '\'' +
                ", dateTime=" + dateTime +
                ", filesize=" + filesize +
                ", data=" + Arrays.toString(data) +
                ", extension='" + extension + '\'' +
                ", channel=" + channel +
                ", numberFolder=" + numberFolder +
                ", headerSize=" + headerSize +
                '}';
    }
}
