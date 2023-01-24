package ClienteChat;

import MainServidor.HiloClientes;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;


public class Chat extends JFrame {

    private static JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JList<String> onlineUsers;
    private JLabel onlineUsersLabel;
    private static Socket socket;
    private PrintWriter out;
    private static BufferedReader in;
    static String nombreDeUsuario = "";
    private final String HOST = "10.2.9.16";
    private final int PUERTO = 6000;


    public Chat(String username) {
        this.nombreDeUsuario = username;
        setTitle("Chat");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);


        JPanel chatPanel = new JPanel(new BorderLayout());
        mainPanel.add(chatPanel, BorderLayout.CENTER);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        chatPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel messagePanel = new JPanel(
                new BorderLayout());
        mainPanel.add(messagePanel, BorderLayout.SOUTH);
        messageField = new JTextField();
        messagePanel.add(messageField, BorderLayout.CENTER);

        sendButton = new JButton("Enviar" );
        messagePanel.add(sendButton, BorderLayout.EAST);

        JPanel onlineUsersPanel = new JPanel(new BorderLayout());
        mainPanel.add(onlineUsersPanel, BorderLayout.EAST);
        onlineUsersPanel.setPreferredSize(new Dimension(150, 0));

        onlineUsersLabel = new JLabel("Usuarios en linea");
        onlineUsersPanel.add(onlineUsersLabel, BorderLayout.NORTH);

        onlineUsers = new JList<>();
        onlineUsers.setFixedCellWidth(150);
        onlineUsersPanel.add(onlineUsers, BorderLayout.CENTER);

        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());
        JButton onlineButton = new JButton("Online Users");
        onlineUsersPanel.add(onlineButton, BorderLayout.SOUTH);
        //onlineButton.addActionListener(e -> HiloClientes.getClientes().forEach(cliente -> {
          //  System.out.println(cliente);
        //}));

        // Add functionality for updating the list of onlien users

        // Add functionality for updating the chat area
        // Add functionality for handling private messages to specific users
        // Add functionality for handling user joining and leaving the chat
        onlineUsers.addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()) {
                String selectedUsername = onlineUsers.getSelectedValue();
                int option = JOptionPane.showConfirmDialog(this, "Enviar mensaje a " + selectedUsername, "Escoge una opcion", JOptionPane.YES_NO_OPTION);
                if(option == JOptionPane.YES_OPTION) {
                    // code to send private message
                }
            }
        });
    }




    private void sendMessage() {
        if(!messageField.getText().isEmpty()){
            if(socket.isConnected()){
                //mensaje que muestro al cliente
                chatArea.append("Yo: " + messageField.getText() +"\n");

                //mensaje que se envia al servidor
                out.println(nombreDeUsuario + ": " + messageField.getText());
                messageField.setText("");
            } else {
                chatArea.append("Servidor no disponible.");
            }
        }
    }

    public static void main(String[] args) {
        Chat chat = new Chat(nombreDeUsuario);
        chat.setVisible(true);

        try {
            chat.socket = new Socket("localhost", 6000);
            chat.out = new PrintWriter(chat.socket.getOutputStream(), true);
            chat.in = new BufferedReader(new InputStreamReader(chat.socket.getInputStream()));
            chat.chatArea.append("Conectado al servidor\n");
            chat.startReadingThread();
        } catch (ConnectException e) {
            chat.chatArea.append("Servidor no disponible\n");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReadingThread() {
        Thread readingThread = new Thread() {
            @Override
            public void run() {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        if (serverMessage.startsWith("ONLINE")) {
                            String[] onlineList = serverMessage.substring(6).split(", ");
                            onlineUsers.setListData(onlineList);
                        }else if (serverMessage.startsWith(nombreDeUsuario)) {

                        } else {
                            chatArea.append(serverMessage + "\n");
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        readingThread.start();
    }
}