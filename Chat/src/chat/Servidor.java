package chat;

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

public class Servidor extends Thread {

    private static ArrayList<Cliente> clientes;
    private static ServerSocket server;
    private String nome;
    private Socket conexao;
    private InputStream input;
    private InputStreamReader inputReader;
    private BufferedReader bufferedReader;

    public Servidor(Socket conexao) {
        this.conexao = conexao;
        try {
            input = conexao.getInputStream();
            inputReader = new InputStreamReader(input);
            bufferedReader = new BufferedReader(inputReader);
        } catch (IOException e) {
            System.out.println("deu ruim");
        }
    }

    @Override
    public void run() {
        try {
            String msg;
            OutputStream output = this.conexao.getOutputStream();
            Writer writer = new OutputStreamWriter(output);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            nome = msg = bufferedReader.readLine();
            clientes.add(new Cliente(nome, bufferedWriter));

            System.out.println(nome + " se conectou");

            while (!"Bye".equalsIgnoreCase(msg) && msg != null) {
                msg = bufferedReader.readLine();
                if (msg.startsWith("/")) {
                    msg = msg.substring(1);
                    if (msg.startsWith("list")) {
                        listar(bufferedWriter);
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
                    sendToAll(bufferedWriter, msg);
                    System.out.println(msg);
                }
            }

        } catch (IOException e) {
            System.out.println("deu ruim");

        }
    }

    public void sendToAll(BufferedWriter bufferedWriterSaida, String msg) throws IOException {
        BufferedWriter bufferedWriterStream;

        for (Cliente cliente : clientes) {
            BufferedWriter bufferedWriter = cliente.getBufferedWriter();
            bufferedWriterStream = (BufferedWriter) bufferedWriter;
            if (!(bufferedWriterSaida == bufferedWriterStream)) {
                bufferedWriterStream.write(nome + " -> " + msg + "\r\n");
                bufferedWriterStream.flush();
            }
        }
    }

    public void listar(BufferedWriter bwSaida) throws IOException {
        BufferedWriter bufferedWriterStream;
        BufferedWriter bwCliente = null;
        BufferedWriter bufferedWriter;
        for (Cliente cliente : clientes) {
            bufferedWriter = cliente.getBufferedWriter();
            bufferedWriterStream = (BufferedWriter) bufferedWriter;
            if (bwSaida == bufferedWriterStream) {
                bwCliente = bufferedWriter;
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
                bw = cliente.getBufferedWriter();
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
            clientes = new ArrayList<>();
            while (true) {
                System.out.println("Aguardando conexão...");
                Socket con = server.accept();
                System.out.println("Cliente conectado...");

                Thread t = new Servidor(con);
                t.start();
            }

        } catch (IOException e) {
            System.out.println("deu ruim");
        }
    }
}
