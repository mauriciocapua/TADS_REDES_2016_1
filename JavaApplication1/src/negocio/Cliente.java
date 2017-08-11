/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import java.io.OutputStream;

/**
 *
 * @author Mauricio Capua
 */
public class Cliente {

    private int id;
    private String nome;
    private String mensagem;

    public Cliente() {
    }

    public Cliente(int id, String nome, String mensagem) {
        this.id = id;
        this.nome = nome;
        this.mensagem = mensagem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

}
