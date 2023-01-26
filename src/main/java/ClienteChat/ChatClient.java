package ClienteChat;

import MainServidor.HiloClientes;

import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class ChatClient extends JFrame {
    // Atributos para la interfaz gráfica de usuario
    private JTextArea chatArea;
    private JTextField zonaEscritura;
    private JButton botonEnviar;
    private JList<String> onlineUsers;
    private JLabel onlineUsersLabel;

    // Atributos para la conexión y la comunicación
    private Socket socket;
    private PrintWriter printWrite;
    private BufferedReader bufferedRead;
    static String nombreDeUsuario = "";

    private final String HOST = "10.2.9.16";
    private final int PUERTO = 6000;
    private String onlineList[];

    public ChatClient(String nombreUsuario) {
        this.nombreDeUsuario = nombreUsuario;
        // Inicializar la interfaz gráfica de usuario

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
        zonaEscritura = new JTextField();
        messagePanel.add(zonaEscritura, BorderLayout.CENTER);

        botonEnviar = new JButton("Enviar" );
        messagePanel.add(botonEnviar, BorderLayout.EAST);

        JPanel onlineUsersPanel = new JPanel(new BorderLayout());
        mainPanel.add(onlineUsersPanel, BorderLayout.EAST);
        onlineUsersPanel.setPreferredSize(new Dimension(150, 0));

        try {
            socket = new Socket(HOST, PUERTO);
            printWrite = new PrintWriter(socket.getOutputStream(), true);
            bufferedRead = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (ConnectException e){
            chatArea.append("Servidor no disponible.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Iniciar hilo para escuchar mensajes entrantes
        new Thread(new LectorMensajesInterno()).start();
        onlineUsersLabel = new JLabel("Usuarios en linea");
        onlineUsersPanel.add(onlineUsersLabel, BorderLayout.NORTH);

        onlineUsers = new JList<>();
        onlineUsers.setFixedCellWidth(150);
        onlineUsersPanel.add(onlineUsers, BorderLayout.CENTER);

        botonEnviar.addActionListener(e -> mandarMensaje());
        zonaEscritura.addActionListener(e -> mandarMensaje());
        //JButton onlineButton = new JButton("Cambiar estado a activo");
        //onlineUsersPanel.add(onlineButton, BorderLayout.SOUTH);
        //onlineButton.addActionListener(e -> {
            //###printWrite.println("ONLINE"+nombreDeUsuario);###
            //Todo: solo te muestra el nombre de usuario del cliente local, no te muestra realmente los demas usuarios, arreglar esto
        //});


        // Add functionality for updating the list of online users

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
        new Thread(new hiloOnline("ONLINE")).start();
    }

    // Método para enviar mensajes
    private void mandarMensaje() {
        //primero comprobamos que el mensaje no esté vacío y que haya conexion con el servidor
        if(!zonaEscritura.getText().isEmpty()){
            if(socket.isConnected()){
                //mensaje que muestro al cliente
                chatArea.append("Yo: " + zonaEscritura.getText() +"\n");

                //mensaje que se envia al servidor
                printWrite.println(nombreDeUsuario + ": " + zonaEscritura.getText());
                zonaEscritura.setText("");
            } else {
                chatArea.append("Servidor no disponible.");
            }
        }
    }
    private class hiloOnline implements Runnable{
        String comando;
        public hiloOnline(String comando){
            this.comando=comando;
        }
        public void run(){
            String usuarios[]=new String[20];
            if(comando=="ONLINE"){
                while (true){
                    synchronized(this) {
                        usuarios=HiloClientes.usuarios;
                        onlineList= new String[20];
                        try {
                            for(int i=0;i<usuarios.length;i++){
                                onlineList[i]=usuarios[i];
                            }
                            wait(2000);
                            onlineUsers.setListData(onlineList);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    // Clase interna para escuchar mensajes entrantes
    private class LectorMensajesInterno implements Runnable {
        public void run() {
            String mensaje;
            try {
                while ((mensaje = bufferedRead.readLine()) != null) {
                    if (mensaje.startsWith("ONLINE")) {
                        onlineList = mensaje.substring(6).split(", ");
                        onlineUsers.setListData(onlineList);
                    }else if(mensaje.startsWith(nombreDeUsuario)){

                    }
                    else{
                        chatArea.append(mensaje + "\n");
                        //si la ventana no está activa, mostrar notificación
                        if(!isActive()){
                            windowsPopUp(mensaje);
                        }
                    }
                    //El mensaje se muestra dos veces; Hay que arreglarlo

                }
            } catch (IOException | AWTException e) {
                e.printStackTrace();
            }
        }
    }

    public void windowsPopUp(String mensaje) throws AWTException {
        //Obtain only one instance of the SystemTray object
        SystemTray bandeja = SystemTray.getSystemTray();

        //Si el icono es una imagen
        Image imagen = Toolkit.getDefaultToolkit().createImage("src/main/resources/chat_ico.svg");

        TrayIcon trayIcon = new TrayIcon(imagen, "Chat DAM");

        // Hacer que la imagen sea resizable
        trayIcon.setImageAutoSize(true);

        // añade el tooltip al icono
        trayIcon.setToolTip("Chat DAM");
        bandeja.add(trayIcon);

        trayIcon.displayMessage("Chat", mensaje, TrayIcon.MessageType.INFO);
        trayIcon.addActionListener(e -> {
            setVisible(true);
            requestFocus();
        });
    }

    public static void main(String[] args) {
        new ChatClient(nombreDeUsuario);
    }
}
