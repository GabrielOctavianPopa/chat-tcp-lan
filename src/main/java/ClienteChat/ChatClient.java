package ClienteChat;

import javax.swing.*;

import ClienteChat.VentanaChatClient;

public class ChatClient extends SwingWorker<Void, Void> {
    String nombreDeUsuario = "";

    public void setNombreDeUsuario(String nombreDeUsuario) {
        this.nombreDeUsuario = nombreDeUsuario;
    }

    @Override
    protected Void doInBackground() throws Exception {
        VentanaChatClient ventanaChatClient = new VentanaChatClient(nombreDeUsuario);
        ventanaChatClient.setVisible(true);
        return null;
    }

    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();
        chatClient.execute();
    }
}
