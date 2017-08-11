package multithreadchatserver;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Mauricio Capua
 */
public class MultiThreadChatClient implements Runnable {

    static Socket clientSocket = null;
    static PrintStream os = null;
    static DataInputStream is = null;
    static BufferedReader inputLine = null;
    static boolean closed = false;

    public static void main(String[] args) {

        try {
            clientSocket = new Socket("127.0.0.1", 5555);
            inputLine = new BufferedReader(new InputStreamReader(System.in));
            os = new PrintStream(clientSocket.getOutputStream());
            is = new DataInputStream(clientSocket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + 5555);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to the host " + 5555);
        }

        // If everything has been initialized then we want to write some data
        // to the socket we have opened a connection to on port port_number 
        if (clientSocket != null && os != null && is != null) {
            try {

                // Create a thread to read from the server
                new Thread(new MultiThreadChatClient()).start();

                while (!closed) {
                    os.println(inputLine.readLine());
                }
                os.close();
                is.close();
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        }
    }

    public void run() {
        String responseLine;

        // Keep on reading from the socket till we receive the "Bye" from the server,
        // once we received that then we want to break.
        try {
            while ((responseLine = is.readLine()) != null) {
                System.out.println(responseLine);
                if (responseLine.indexOf("*** Bye") != -1) {
                    break;
                }
            }
            closed = true;
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }
}
