package multithreadchatserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Mauricio Capua
 */
public class MultiThreadChatServer {

    static Socket clientSocket = null;
    static ServerSocket serverSocket = null;
    static clientThread clienteThread[] = new clientThread[10];

    public static void main(String args[]) {

        try {
            serverSocket = new ServerSocket(5555);
        } catch (IOException e) {
            System.out.println(e);
        }

        while (true) {
            try {
                clientSocket = serverSocket.accept();
                for (int id = 0; id <= 9; id++) {
                    if (clienteThread[id] == null) {
                        (clienteThread[id] = new clientThread(clientSocket, clienteThread, id)).start();
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
