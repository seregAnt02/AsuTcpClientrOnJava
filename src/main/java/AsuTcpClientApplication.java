import duma.asu.presents.SSLSocketClient;
import duma.asu.presents.modbus.RTUModbus;

import java.io.IOException;
import java.net.Socket;

import static java.lang.System.out;

public class AsuTcpClientApplication {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        SSLSocketClient sslSocketClient = new SSLSocketClient("192.168.0.10", 1300);
        sslSocketClient.runClient();

        //new RTUModbus(new Client()).start();

        out.print(System.getProperty("java.class.path"));
    }
}