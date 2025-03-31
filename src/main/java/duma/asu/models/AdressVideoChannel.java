package duma.asu.models;

public class AdressVideoChannel {

    private int channel;

    private String protocol;

    public AdressVideoChannel(int channel, String protocol) {
        this.channel = channel;
        this.protocol = protocol;
    }

    public int getChannel() {
        return channel;
    }


    public String getProtocol() {
        return protocol;
    }
}
