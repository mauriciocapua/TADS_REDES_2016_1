/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.io.BufferedWriter;

/**
 *
 * @author Mauricio Capua
 */
class Cliente {

    private String Nome;
    private BufferedWriter bufferedWriter;

    public Cliente() {
    }

    public Cliente(String Nome, BufferedWriter bufferedWriter) {
        this.Nome = Nome;
        this.bufferedWriter = bufferedWriter;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String Nome) {
        this.Nome = Nome;
    }

    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

    public void setBufferedWriter(BufferedWriter bufferedWriter) {
        this.bufferedWriter = bufferedWriter;
    }

}
