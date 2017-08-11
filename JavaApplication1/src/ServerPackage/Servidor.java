/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerPackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import ClientPackage.ClientTCP;
import java.util.Scanner;
import negocio.Cliente;

/**
 *
 * @author Mauricio Capua
 */
public class Servidor {
    private Arraylist<Cliente> clientes;
    /**
     * @param args the command line arguments
     */
    public StartServer(){
        
    }

        ServerSocket servidor = new ServerSocket(5555);

        System.out.println("Porta 5555 aberta!");

        Socket cliente = servidor.accept();

        System.out.println("Nova conexão com o cliente "
                + cliente.getInetAddress().getHostAddress()
        );

        Scanner s = new Scanner(cliente.getInputStream());

        while (s.hasNextLine()) {

            System.out.println(s.nextLine());

        }

        s.close();

        servidor.close();

        cliente.close();

    }

}
