package Client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class ChatClient extends JFrame {
    // Atributos para la interfaz gráfica de usuario
    private JTextArea chatArea;
    private JTextField zonaEscritura;
    private JButton botonEnviar;

    // Atributos para la conexión y la comunicación
    private Socket socket;
    private PrintWriter printWrite;
    private BufferedReader bufferedRead;
    static String nombreDeUsuario = "";

    private final String HOST = "10.2.9.16";
    private final int PUERTO = 6000;

    public ChatClient(String nombreUsuario) {
        this.nombreDeUsuario = nombreUsuario;
        // Inicializar la interfaz gráfica de usuario
        chatArea = new JTextArea();
        zonaEscritura = new JTextField();
        botonEnviar = new JButton("Enviar");

        // Añadir evento de botón para enviar mensajes
        botonEnviar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mandarMensaje();
            }
        });

        // Añadir evento de teclado para enviar mensajes con la tecla Enter
        zonaEscritura.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    mandarMensaje();
                }
            }
        });

        // Colocar elementos de la interfaz gráfica de usuario en el marco
        setLayout(new BorderLayout());
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);
        add(zonaEscritura, BorderLayout.SOUTH);
        add(botonEnviar, BorderLayout.EAST);

        // Ajustar tamaño del marco y hacerlo visible
        setSize(500, 300);
        setVisible(true);

        // Establecer conexión con el servidor
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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    // Clase interna para escuchar mensajes entrantes
    private class LectorMensajesInterno implements Runnable {
        public void run() {
            String mensaje;
            try {
                while ((mensaje = bufferedRead.readLine()) != null) {
                    if(mensaje.startsWith(nombreDeUsuario)){
                        //no mostrar mis propios mensajes
                    }else{
                        chatArea.append(mensaje + "\n");
                        //si la ventana no está activa, mostrar notificación
                        if(!isActive()){
                            windowsPopUp(mensaje);
                        }
                    }
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
