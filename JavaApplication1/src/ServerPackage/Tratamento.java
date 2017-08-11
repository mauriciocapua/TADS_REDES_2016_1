/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerPackage;

import java.io.PrintStream;
import java.util.Scanner;
import negocio.Cliente;

/**
 *
 * @author Mauricio Capua
 */
public class Tratamento {

    private Cliente cliente;
    private Cliente destino;
    private Servidor servidor;

    public Tratamento() {
    }

    public Tratamento(Cliente cliente, Cliente destino, Servidor servidor) {
        this.cliente = cliente;
        this.destino = destino;
        this.servidor = servidor;
    }

    public void envio() {
        // quando chegar uma msg, distribui pra todos

        Scanner teclado = new Scanner(System.in);

        PrintStream saida = new PrintStream(cliente.getOutputStream());

        while (teclado.hasNextLine()) {

            saida.println(teclado.nextLine());

        }

        saida.close();

    }
}
