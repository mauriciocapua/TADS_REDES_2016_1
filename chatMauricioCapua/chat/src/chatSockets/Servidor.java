package chatSockets;

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
    private Socket socket;
    private InputStream inputStream;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;

    public Servidor(Socket socket) {
        this.socket = socket;
        try {
            inputStream = this.socket.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void run() {
        try {
            String mensagem;
            OutputStream outputStream = this.socket.getOutputStream();
            Writer Writer = new OutputStreamWriter(outputStream);
            BufferedWriter bfw = new BufferedWriter(Writer);
            nome = mensagem = bufferedReader.readLine();
            clientes.add(new Cliente(nome, bfw, socket.getPort(), socket.getInetAddress().getHostAddress()));

//            for (int i = 0; i < clientes.size(); i++) {
//                mensagem = "/list " + clientes.get(i).getNome();
//                System.out.println(mensagem);
//                sendToAllList(bfw, mensagem);
//            }
            for (int i = 0; i < clientes.size(); i++) {
                mensagem = "/list " + clientes.get(i).getNome();
                System.out.println(mensagem);
                sendToAllList(bfw, mensagem);
            }
//            while (!"Bye".equalsIgnoreCase(mensagem) && mensagem != null) {
            while (true) {
                mensagem = bufferedReader.readLine();
                if (mensagem.equals("manda_lista")) {
                    System.out.println(mensagem);
                    for (int i = 0; i < clientes.size(); i++) {
                        mensagem = "/list " + clientes.get(i).getNome();
                        System.out.println(mensagem);
                        sendToAllList(bfw, mensagem);
                    }
                } else if (mensagem.startsWith("privado")) {
                    mensagem = mensagem.substring(1);
                    if (mensagem.contains(" ")) {
                        mensagem = mensagem.substring(mensagem.indexOf(" ") + 1);
                        if (!mensagem.isEmpty()) {
                            String cliente = mensagem.substring(0, mensagem.indexOf(" "));
                            if (mensagem.contains(" ")) {
                                mensagem = mensagem.substring(mensagem.indexOf(" ") + 1);
                                privado(cliente, mensagem);
                            }
                        }
                    }
                } else if (mensagem.contains("Desconectado")) {
                    String cliente = mensagem.substring(0, mensagem.indexOf(":"));
                    for (int i = 0; i < clientes.size(); i++) {
                        if (clientes.get(i).getNome().equals(cliente)) {
                            clientes.remove(clientes.get(i));
                        }
                    }

                    sendToAll(bfw, "Desconectado");

                    for (int i = 0; i < clientes.size(); i++) {
                        mensagem = "/list " + clientes.get(i).getNome();
                        System.out.println(mensagem);
                        sendToAllList(bfw, mensagem);
                    }

                } else {
                    sendToAll(bfw, mensagem);
                    System.out.println(mensagem);
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void sendToAll(BufferedWriter bufferedWriterOut, String msg) {
        try {
            BufferedWriter bwS;
            for (int i = 0; i < clientes.size(); i++) {
                BufferedWriter bufferedWriter = clientes.get(i).getBw();
                bwS = (BufferedWriter) bufferedWriter;
                if (!(bufferedWriterOut == bwS)) {
                    bufferedWriter.write(nome + ": " + msg + "\r\n");
                    bufferedWriter.flush();
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void sendToAllList(BufferedWriter bufferedWriterOut, String msg) {
        try {
            BufferedWriter bwS;
            for (int i = 0; i < clientes.size(); i++) {
                BufferedWriter bufferedWriter = clientes.get(i).getBw();
                bwS = (BufferedWriter) bufferedWriter;
//                if (!(bufferedWriterOut == bwS)) {
                bufferedWriter.write(msg + "\r\n");
                bufferedWriter.flush();
//                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void listar(BufferedWriter bwSaida) {
        try {
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
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    public void privado(String privado, String mensagem) {
        try {
            BufferedWriter bw = null;
            for (Cliente cliente : clientes) {
                if (cliente.getNome().equals(privado)) {
                    bw = cliente.getBw();
                }
            }
            if (bw != null) {
                bw.write(nome + " disse em privado para você: " + mensagem + "\r\n");
                bw.flush();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        try {
            server = new ServerSocket(5555);
            clientes = new ArrayList<Cliente>();
            while (true) {
                System.out.println("Aguardando conexão");
                Socket con = server.accept();
                System.out.println("Cliente conectado");
                Thread t = new Servidor(con);
                t.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
