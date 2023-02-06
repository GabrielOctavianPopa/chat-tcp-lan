package ClienteChat;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

public class VentanaChatClient extends JFrame {
    // Atributos para la interfaz gráfica de usuario
    private JTextArea chatArea;
    private JTextField zonaEscritura;
    private JButton botonEnviar;

    // Atributos para la conexión y la comunicación
    private Socket socket;
    private PrintWriter printWrite;
    private BufferedReader bufferedRead;
    static String nombreDeUsuario = "";
    JLabel labelRadu = new JLabel("Radu");
    JLabel labelGabriel = new JLabel("Gabriel");
    ImageIcon raduIcon = new ImageIcon("src/main/resources/imagenes/avatar_radu.jpg");
    ImageIcon gabrielIcon = new ImageIcon("src/main/resources/imagenes/avatar_gabriel.jpg");

    private final String HOST = "localhost";
    private final int PUERTO = 6000;

    public VentanaChatClient(String nombreUsuario) {
        this.nombreDeUsuario = nombreUsuario;
        // Inicializar la interfaz gráfica de usuario
        setTitle("Chat");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

        botonEnviar = new JButton("Enviar");
        messagePanel.add(botonEnviar, BorderLayout.EAST);

        JPanel panelDerecha = new JPanel(new BorderLayout());
        mainPanel.add(panelDerecha, BorderLayout.EAST);
        panelDerecha.setPreferredSize(new Dimension(150, 0));

        try{
            URL urlRadu = new URL("https://github.com/RGM73");
            URL urlGabriel = new URL("https://github.com/GabrielOctavianPopa");
            //set the image size to 100 100
            raduIcon = new ImageIcon(raduIcon.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
            gabrielIcon = new ImageIcon(gabrielIcon.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
            labelRadu.setIcon(raduIcon);
            labelGabriel.setIcon(gabrielIcon);




            panelDerecha.add(labelRadu, BorderLayout.CENTER);
            panelDerecha.add(labelGabriel, BorderLayout.NORTH);

            labelRadu.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    try {
                        java.awt.Desktop.getDesktop().browse(urlRadu.toURI());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            labelGabriel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    try {
                        java.awt.Desktop.getDesktop().browse(urlGabriel.toURI());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }


        try {
            socket = new Socket(HOST, PUERTO);
            printWrite = new PrintWriter(socket.getOutputStream(), true);
            bufferedRead = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (ConnectException e) {
            chatArea.append("Servidor no disponible.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        botonEnviar.addActionListener(e -> mandarMensaje());
        zonaEscritura.addActionListener(e -> mandarMensaje());
    }

    // Método para enviar mensajes
    private void mandarMensaje() {
        //primero comprobamos que el mensaje no esté vacío y que haya conexion con el servidor
        if (!zonaEscritura.getText().isEmpty()) {
            if (socket.isConnected()) {
                //mensaje que muestro al cliente
                chatArea.append("Yo: " + zonaEscritura.getText() + "\n");

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
                int i = 0;
                while ((mensaje = bufferedRead.readLine()) != null) {
                    if (mensaje.startsWith(nombreDeUsuario)) {

                    } else {
                        chatArea.append(mensaje + "\n");
                        //si la ventana no está activa, mostrar notificación
                        if (!isActive()) {
                            popUpDeWindows(mensaje);
                        }
                    }
                }
            } catch (IOException | AWTException e) {
                e.printStackTrace();
            }
        }
    }

    public void popUpDeWindows(String mensaje) throws AWTException {
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
        new VentanaChatClient(nombreDeUsuario);
    }
}
