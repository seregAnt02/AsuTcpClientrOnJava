import duma.asu.presents.Client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.System.out;

public class AsuTcpClientApplication {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ввыдите своё имя: ");
        String name = scanner.nextLine();
        Socket socket = new Socket("localhost", 1300);
        Client client = new Client(socket, name);
        client.listenForMessage();
        client.sendMessage();

        out.print(System.getProperty("java.class.path"));
    }
}