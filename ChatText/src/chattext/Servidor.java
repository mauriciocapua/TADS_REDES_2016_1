/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chattext;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Enki
 */
public class Servidor extends Thread {

    private static ArrayList<Cliente> clientes;
    private static ServerSocket server;
    private String nome;
    private Socket con;
    private InputStream in;
    private InputStreamReader inr;
    private BufferedReader bfr;

    public Servidor(Socket con) {
        this.con = con;
        try {
            in = con.getInputStream();
            inr = new InputStreamReader(in);
            bfr = new BufferedReader(inr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        try {

            String msg;
            OutputStream ou = this.con.getOutputStream();
            Writer ouw = new OutputStreamWriter(ou);
            BufferedWriter bfw = new BufferedWriter(ouw);
            nome = msg = bfr.readLine();
            clientes.add(new Cliente(nome, bfw, con.getPort(), con.getInetAddress().getHostAddress()));

            System.out.println(nome);
            while (!"Bye".equalsIgnoreCase(msg) && msg != null) {
                msg = bfr.readLine();
                if (msg.startsWith("/")) {
                    msg = msg.substring(1);
                    if (msg.startsWith("list")) {
                        listar(bfw);
                    } else if (msg.startsWith("w")) {
                        if (msg.contains(" ")) {
                            msg = msg.substring(msg.indexOf(" ") + 1);
                            if (!msg.isEmpty()) {
                                String cliente = msg.substring(0, msg.indexOf(" "));
                                if (msg.contains(" ")) {
                                    msg = msg.substring(msg.indexOf(" ") + 1);
                                    privado(cliente, msg);
                                }
                            }
                        }
                    }

                } else {
                    sendToAll(bfw, msg);
                    System.out.println(msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void sendToAll(BufferedWriter bwSaida, String msg) throws IOException {
        BufferedWriter bwS;

        for (Cliente cliente : clientes) {
            BufferedWriter bw = cliente.getBw();
            bwS = (BufferedWriter) bw;
            if (!(bwSaida == bwS)) {
                bw.write(nome + " -> " + msg + "\r\n");
                bw.flush();
            }
        }
    }

    public void listar(BufferedWriter bwSaida) throws IOException {
        BufferedWriter bwS;
        BufferedWriter bwCliente = null;
        BufferedWriter bw;

        for (Cliente cliente : clientes) {
            bw = cliente.getBw();
            bwS = (BufferedWriter) bw;
            if (bwSaida == bwS) {
                bwCliente = bw;
            }
        }
        bwCliente.write("Lista de Clientes:\r\n");
        bwCliente.flush();
        for (Cliente clientes : clientes) {
            clientes.getNome();
            bwCliente.write(clientes.getNome() + "\r\n");
            bwCliente.flush();
        }

    }

    public void privado(String privado, String mensagem) throws IOException {
        BufferedWriter bw = null;

        for (Cliente cliente : clientes) {
            if (cliente.getNome().equals(privado)) {
                bw = cliente.getBw();
            }
        }
        if (bw != null) {
            bw.write(nome + " disse: " + mensagem + "\r\n");
            bw.flush();
        }

    }

    public static void main(String[] args) {

        try {
            server = new ServerSocket(5555);
            clientes = new ArrayList<Cliente>();
            while (true) {
                System.out.println("Aguardando conexão...");
                Socket con = server.accept();
                System.out.println("Cliente conectado...");
                Thread t = new Servidor(con);
                t.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }// Fim do método main                      
} //Fim da classe
