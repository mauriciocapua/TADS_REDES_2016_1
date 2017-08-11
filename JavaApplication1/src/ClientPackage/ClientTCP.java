/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientPackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author Mauricio Capua
 */
public class ClientTCP {

    public void Access() {
        try {

            while (true) {
                String retorno;
                Socket socket = new Socket("127.0.0.1", 5555);
                System.out.println("O cliente se conectou ao servidor!");
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();
                do {
                    byte[] line = new byte[100];
                    System.in.read(line);
//                    JOptionPane.showInputDialog("Informe a inicial do nome do produto que deseja pesquisar: ", "Letra");
                    input.read(line);
                    output.write(line);
                    retorno = new String(line);
                    System.out.println(retorno.trim());
                } while (!retorno.trim().equals("bye"));
                socket.close();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
