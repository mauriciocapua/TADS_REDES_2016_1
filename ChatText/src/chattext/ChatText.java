package chattext;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class ChatText implements ActionListener, KeyListener {

    private static final long serialVersionUID = 1L;
    private JTextArea texto;
    private JTextField txtMsg;
    private JButton btnSend;
    private JButton btnSair;
    private JLabel lblHistorico;
    private JLabel lblMsg;
    private JPanel pnlContent;
    private Socket socket;
    private OutputStream ou;
    private Writer ouw;
    private BufferedWriter bfw;
    private String txtIP = "localhost";
    private int txtPorta = 5555;
    private JTextField txtNome;
    private String nome;

    public ChatText() throws IOException {

        JLabel lblMessage = new JLabel("Chat da Uol!");
        txtNome = new JTextField("Fulano");
        Object[] texts = {lblMessage, txtNome};
        JOptionPane.showMessageDialog(null, texts);
        pnlContent = new JPanel();
        texto = new JTextArea(10, 20);
        texto.setEditable(false);
        texto.setBackground(new Color(240, 240, 240));
        txtMsg = new JTextField(20);
        lblHistorico = new JLabel("Histórico");
        lblMsg = new JLabel("Mensagem");
        btnSend = new JButton("Enviar");
        btnSend.setToolTipText("Enviar Mensagem");
        btnSair = new JButton("Sair");
        btnSair.setToolTipText("Sair do Chat");
        JScrollPane scroll = new JScrollPane(texto);
        texto.setLineWrap(true);
        pnlContent.add(lblHistorico);
        pnlContent.add(scroll);
        pnlContent.add(lblMsg);
        pnlContent.add(txtMsg);
        pnlContent.add(btnSair);
        pnlContent.add(btnSend);
        pnlContent.setBackground(Color.LIGHT_GRAY);
        texto.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
        texto.append("Digite /list para listar as pessoas conectadas\n");
        txtMsg.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));

    }

    public void conectar() throws IOException {

        socket = new Socket(txtIP, txtPorta);
        ou = socket.getOutputStream();
        ouw = new OutputStreamWriter(ou);
        bfw = new BufferedWriter(ouw);

        bfw.write(nome + "\r\n");
        bfw.flush();

    }

    public void enviarMensagem(String msg) throws IOException {
        if (msg.equals("Bye")) {
            bfw.write("Desconectado \r\n");
            texto.append("Desconectado \r\n");
            bfw.close();
            ouw.close();
            ou.close();
            socket.close();
        } else {
            bfw.write(msg + "\r\n");
            texto.append(txtNome.getText() + " diz -> " + txtMsg.getText() + "\r\n");
        }
        bfw.flush();
        txtMsg.setText("");
    }

    public void escutar() throws IOException {

        InputStream in = socket.getInputStream();
        InputStreamReader inr = new InputStreamReader(in);
        BufferedReader bfr = new BufferedReader(inr);
        String msg = "";

        while (!"Bye".equalsIgnoreCase(msg)) {
            if (bfr.ready()) {
                msg = bfr.readLine();
                if (msg.equals("Bye")) {
                    texto.append("Servidor caiu! \r\n");
                } else {
                    texto.append(msg + "\r\n");
                }
            }
        }
    }

    public void sair() throws IOException {

        enviarMensagem("Bye");
        bfw.close();
        ouw.close();
        ou.close();
        socket.close();
    }

//    @Override
//    public void actionPerformed(ActionEvent e) {
//
//        try {
//            if (e.getActionCommand().equals(btnSend.getActionCommand())) {
//                enviarMensagem(txtMsg.getText());
//            } else if (e.getActionCommand().equals(btnSair.getActionCommand())) {
//                sair();
//            }
//        } catch (IOException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//    }
//
    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                enviarMensagem(txtMsg.getText());
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }
//
//    @Override
//    public void keyReleased(KeyEvent arg0) {
//        // TODO Auto-generated method stub               
//    }
//
//    @Override
//    public void keyTyped(KeyEvent arg0) {
//        // TODO Auto-generated method stub               
//    }

    public static void main(String[] args) throws IOException {
        ChatText app = new ChatText();
        app.nome = JOptionPane.showInputDialog("informe seu nome");
        app.conectar();
        app.escutar();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
