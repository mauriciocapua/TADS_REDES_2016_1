/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithreadchatserver;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Mauricio Capua
 */
class clientThread extends Thread {

    DataInputStream is = null;
    PrintStream os = null;
    Socket clientSocket = null;
    clientThread clienteThread[];
    clientThread ct;
    int idClient;

    public clientThread(Socket clientSocket, clientThread[] clienteThread, int id) {
        this.clientSocket = clientSocket;
        this.clienteThread = clienteThread;
        idClient = id;
    }

    public void run() {
        String line;
        String name;

        try {
            is = new DataInputStream(clientSocket.getInputStream());
            os = new PrintStream(clientSocket.getOutputStream());
            idClient++;

            os.println("Enter your name.");
            name = is.readLine();
            os.println("Hello " + name + "your user id is : " + idClient);
            System.out.println("Hello " + name + "\n" + "your user id is : " + idClient);
            for (int idClient = 0; idClient <= 9; idClient++) {
                if (clienteThread[idClient] != null && clienteThread[idClient] != this) {
                    clienteThread[idClient].os.println("*** A new user " + name + " entered the chat room !!! ***");
                }
            }

            while (true) {
                line = is.readLine();

                if (line.startsWith("/quit")) {
                    break;
                }

                if (line.startsWith("/getid")) {
                    os.println("<" + name + " > Your id is :" + idClient);
                }

                if (line.startsWith("/menu")) {
                    for (int idClient = 0; idClient <= 9; idClient++) {
                        if (clienteThread[idClient] != null && clienteThread[idClient] == this) {
                            clienteThread[idClient].os.println("1.Show all Name of the clients\n2. Show your name\n3.Send a file to the server\n4.Receive a file to the server\n5.Quit");
                        }
                    }
                }
                
                if (line.startsWith("/menu1")) {
                    for (int idClient = 0; idClient <= 9; idClient++) {
                        if (clienteThread[idClient] != null && clienteThread[idClient] == this) {
                            for(int i=0; i< clienteThread.length;i++){
                                System.out.println(clienteThread[i].clientSocket);
                            }
                            clienteThread[idClient].os.println("1.Show all Name of the clients\n2. Show your name\n3.Send a file to the server\n4.Receive a file to the server\n5.Quit");
                        }
                    }
                }
                if (line.startsWith("/priv")) {
                    Scanner sr = new Scanner(System.in);
                    System.out.println("priv message to ?");
                    int test = sr.nextInt();
                    ct = clienteThread[test];
                    String message = sr.next();
                    ct.os.println("<" + name + "> " + message);
                }

                /*
             * 
		 Scanner sr = new Scanner(System.in);
		 
         System.out.println("Which number do you wanna see ? \n");
         
         int number = sr.nextInt();
         int aInt = Integer.parseInt(aString);
	     System.out.println(ia[number]);
             * */
 /*for	(int idClient =0; idClient <=9; idClient ++)	
            {
               if (t[idClient]!=null)  
               {
            	   System.out.println("Private Message for who ? ");
            	   int number = is.readInt();
            	   ct = t[number];
            	 
               }
            }*/
 /*System.out.println("Private Message for who ? ");
            String number = is.readLine();
            int aInt = Integer.parseInt(number);
            ct = t[aInt];
            ct.os.println("<"+name+"> "+line);*/
                for (int idClient = 0; idClient <= 9; idClient++) {
                    if (clienteThread[idClient] != null && clienteThread[idClient] != this) {
                        clienteThread[idClient].os.println("<" + name + "> " + line);
                    }
                }
            }

            for (int idClient = 0; idClient <= 9; idClient++) {
                if (clienteThread[idClient] != null && clienteThread[idClient] != this) {
                    clienteThread[idClient].os.println("*** The user " + name + " is leaving the chat room !!! ***");
                }
            }

            os.println("*** Bye " + name + " ***");

            // Clean up:
            // Set to null the current thread variable such that other client could
            // be accepted by the server
            for (int idClient = 0; idClient <= 9; idClient++) {
                if (clienteThread[idClient] == this) {
                    clienteThread[idClient] = null;
                }
            }

            // close the output stream
            // close the input stream
            // close the socket
            try {
                sleep(50);
            } catch (Exception e) {
            };
            is.close();
            os.close();
            clientSocket.close();
        } catch (IOException e) {
        };

    }
}
