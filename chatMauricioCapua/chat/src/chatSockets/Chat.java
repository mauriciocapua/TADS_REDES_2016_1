package chatSockets;

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
import java.util.ArrayList;

import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Chat extends JFrame implements ActionListener, KeyListener {

    private static final long serialVersionUID = 1L;
    private JTextArea texto;
    private JTextArea lista;
    private JTextField txtMsg;
    private JButton btnSend;
    private JButton btnSair;
    private JLabel lblHistorico;
    private JLabel lblMsg;
    private JPanel pnlContent;
    private Socket socket;
    private OutputStream outputStream;
    private Writer Writer;
    private BufferedWriter bufferedWriter;
    private JTextField Nome;
    private ArrayList<String> listar;

    public Chat() throws IOException {
        JLabel lblMessage = new JLabel("Chat");
        Nome = new JTextField("");
        Object[] texts = {lblMessage, Nome};
        JOptionPane.showMessageDialog(null, texts);
        pnlContent = new JPanel();
        texto = new JTextArea(10, 20);
        lista = new JTextArea(10, 12);
        texto.setEditable(false);
        texto.setBackground(new Color(240, 240, 240));
        lista.setEditable(false);
        lista.setBackground(new Color(240, 240, 240));
        txtMsg = new JTextField(20);
        lblHistorico = new JLabel("Histórico");
        lblMsg = new JLabel("Mensagem");
        btnSend = new JButton("Enviar");
        btnSend.setToolTipText("Enviar Mensagem");
        btnSair = new JButton("Sair");
        btnSair.setToolTipText("Sair do Chat");
        btnSend.addActionListener(this);
        btnSair.addActionListener(this);
        btnSend.addKeyListener(this);
        txtMsg.addKeyListener(this);
        JScrollPane scroll = new JScrollPane(texto);
        texto.setLineWrap(true);
        JScrollPane scroll2 = new JScrollPane(lista);
        lista.setLineWrap(true);
        pnlContent.add(lblHistorico);
        pnlContent.add(scroll);
        pnlContent.add(scroll2);
        pnlContent.add(lblMsg);
        pnlContent.add(txtMsg);
        pnlContent.add(btnSair);
        pnlContent.add(btnSend);
        pnlContent.setBackground(Color.LIGHT_GRAY);
        texto.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
        texto.append("Envie sua mensagem: \n");
        lista.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
        lista.append("Lista de conectados: \n");
        txtMsg.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
        setTitle(Nome.getText());
        setContentPane(pnlContent);
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(500, 250);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        listar = new ArrayList<String>();
        listar.add("Lista de conectados: ");
    }

    public void connect() throws IOException {

        socket = new Socket("127.0.0.1", 5555);
        outputStream = socket.getOutputStream();
        Writer = new OutputStreamWriter(outputStream);
        bufferedWriter = new BufferedWriter(Writer);

        bufferedWriter.write(Nome.getText() + "\r\n");
        bufferedWriter.flush();
    }

    public void enviarMensagem(String msg) throws IOException {
        if (msg.equalsIgnoreCase("Bye")) {
            bufferedWriter.write(Nome.getText() + ": Desconectado");
            texto.append("Desconectado \r\n");
            bufferedWriter.close();
            Writer.close();
            outputStream.close();
            socket.close();
        } else {
            bufferedWriter.write(msg + "\r\n");
            texto.append(" - " + txtMsg.getText() + "\r\n");
        }
        bufferedWriter.flush();
        txtMsg.setText("");
    }

    public void SolicitarLista() throws IOException {
        String msg = "manda_lista";
        bufferedWriter.write(msg + "\r\n");
        bufferedWriter.flush();
    }

    public void listen() throws IOException {

        InputStream in = socket.getInputStream();
        InputStreamReader inr = new InputStreamReader(in);
        BufferedReader bfr = new BufferedReader(inr);
        String msg = "";
        boolean flag = false;

        while (!"Bye".equalsIgnoreCase(msg)) {

            if (bfr.ready()) {
                msg = bfr.readLine();
                if (msg.startsWith("/")) {
                    msg = msg.substring(1);
                    if (msg.startsWith("list")) {
                        atualizaLista(flag, msg);
                    }
                } else if (msg.contains("Desconectado")) {
                    String cliente = msg.substring(0, msg.indexOf(":"));
                    for (int i = 0; i < listar.size(); i++) {
                        if (listar.get(i).equals(cliente)) {
                            listar.remove(listar.get(i));
                        }
                    }
                } else {
                    if (msg.equals("Bye")) {
                        texto.append("Servidor caiu! \r\n");
                    } else {
                        texto.append(msg + "\r\n");
                    }
                }
            }
        }
    }

    public void sair() throws IOException {

        enviarMensagem("Bye");

        bufferedWriter.close();
        Writer.close();
        outputStream.close();
        socket.close();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getActionCommand().equals(btnSend.getActionCommand())) {
                enviarMensagem(txtMsg.getText());
            } else if (e.getActionCommand().equals(btnSair.getActionCommand())) {
                sair();
            }
        } catch (IOException e1) {
            System.out.println(e1);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                enviarMensagem(txtMsg.getText());
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                System.out.println(e1);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub               
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub               
    }

    public static void main(String[] args) throws IOException {
        Chat chat = new Chat();
        chat.connect();
        chat.listen();

    }

    private void atualizaLista(boolean flag, String msg) {

        for (int i = 1; i < listar.size(); i++) {
            if (listar.get(i).equals(msg.substring(5))) {
                flag = true;
            }
        }
        if (flag == false) {
            listar.add(msg.substring(5));
        }

        lista.setText("");
        for (int i = 0; i < listar.size(); i++) {
            lista.append(listar.get(i) + "\n");

        }
        flag = false;
    }

}
