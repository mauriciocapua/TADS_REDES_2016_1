package chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Chat {

    private Socket socket;
    private OutputStream outputStream;
    private OutputStreamWriter writer;
    private BufferedWriter bufferWriter;

    public Chat() throws IOException {
    }

    public void conectar(String nome) {

        try {
            socket = new Socket("127.0.0.1", 5555);
            outputStream = socket.getOutputStream();
            writer = new OutputStreamWriter(outputStream);
            bufferWriter = new BufferedWriter(writer);
            bufferWriter.write(nome + "\r\n");
            bufferWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void enviarMensagem(String msg) throws IOException {
        if (msg.equals("Bye")) {
            bufferWriter.write("Desconectado \r\n");
//            texto.append("Desconectado \r\n");
        } else {
            bufferWriter.write(msg + "\r\n");
//            texto.append(txtNome.getText() + " diz -> " + txtMsg.getText() + "\r\n");
        }
        bufferWriter.flush();
//        txtMsg.setText("");
    }

    public void escutar(javax.swing.JTextArea TextPainel) {
        try {
            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String msg = "";

            while (!"Bye".equalsIgnoreCase(msg)) {
                if (bufferedReader.ready()) {
                    msg = bufferedReader.readLine();
                    if (msg.equals("Bye")) {
                        TextPainel.append("Servidor caiu! \r\n");
                    } else {
                        TextPainel.append(msg + "\r\n");
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sair() throws IOException {
        enviarMensagem("Sair");
        bufferWriter.close();
        writer.close();
        outputStream.close();
        socket.close();
    }

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
//    public void keyPressed(KeyEvent e) {
//
//        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//            try {
//                enviarMensagem(txtMsg.getText());
//            } catch (IOException e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();
//            }
//        }
//    }
    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public OutputStreamWriter getWriter() {
        return writer;
    }

    public void setWriter(OutputStreamWriter writer) {
        this.writer = writer;
    }

    public BufferedWriter getBufferWriter() {
        return bufferWriter;
    }

    public void setBufferWriter(BufferedWriter bufferWriter) {
        this.bufferWriter = bufferWriter;
    }
}
