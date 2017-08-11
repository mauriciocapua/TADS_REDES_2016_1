package chatSockets;

import java.io.BufferedWriter;

public class Cliente {

    private String Nome;
    private BufferedWriter bw;
    private int port;
    private String endereco;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Cliente() {
    }

    public Cliente(String Nome, BufferedWriter bw, int port, String endereco) {
        this.Nome = Nome;
        this.bw = bw;
        this.port = port;
        this.endereco = endereco;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String Nome) {
        this.Nome = Nome;
    }

    public BufferedWriter getBw() {
        return bw;
    }

    public void setBw(BufferedWriter bw) {
        this.bw = bw;
    }

}
