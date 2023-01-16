import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class ChatClient extends JFrame {
    // Atributos para la interfaz gráfica de usuario
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;

    // Atributos para la conexión y la comunicación
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    static String username = "";

    public ChatClient(String nombreUsuario) {
        this.username = nombreUsuario;
        // Inicializar la interfaz gráfica de usuario
        chatArea = new JTextArea();
        messageField = new JTextField();
        sendButton = new JButton("Enviar");

        // Añadir evento de botón para enviar mensajes
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Añadir evento de teclado para enviar mensajes con la tecla Enter
        messageField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });

        // Colocar elementos de la interfaz gráfica de usuario en el marco
        setLayout(new BorderLayout());
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);
        add(messageField, BorderLayout.SOUTH);
        add(sendButton, BorderLayout.EAST);

        // Ajustar tamaño del marco y hacerlo visible
        setSize(500, 300);
        setVisible(true);

        // Establecer conexión con el servidor
        try {
            socket = new Socket("10.2.9.16", 6000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (ConnectException e){
            chatArea.append("Servidor no disponible");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Iniciar hilo para escuchar mensajes entrantes
        new Thread(new IncomingReader()).start();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Método para enviar mensajes
    private void sendMessage() {
        //primero comprobamos que el mensaje no esté vacío y que haya conexion con el servidor
        if(!messageField.getText().isEmpty()){
            if(socket.isConnected()){
                //mensaje que muestro al cliente
                chatArea.append("Yo: " + messageField.getText() +"\n");

                //mensaje que se envia al servidor
                out.println(username + ": " + messageField.getText());
                messageField.setText("");
            } else {
                chatArea.append("Servidor no disponible");
            }
        }
    }

    // Clase interna para escuchar mensajes entrantes
    private class IncomingReader implements Runnable {
        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    if(message.startsWith(username)){
                        //no mostrar mis propios mensajes
                    }else{
                        chatArea.append(message + "\n");
                        //si la ventana no está activa, mostrar notificación
                        if(!isActive()){
                            windowsPopUp(message);
                        }
                    }
                }
            } catch (IOException | AWTException e) {
                e.printStackTrace();
            }
        }
    }

    public void windowsPopUp(String message) throws AWTException {
        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage("chat.png");

        TrayIcon trayIcon = new TrayIcon(image, "Chat DAM");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip("Chat DAM");
        tray.add(trayIcon);

        trayIcon.displayMessage("Chat", message, TrayIcon.MessageType.INFO);

        //añadir listener para que al hacer click en la notificación se abra la ventana
        trayIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(true);
                toFront();
                requestFocus();
            }
        });
    }

    public static void main(String[] args) {
        new ChatClient(username);
    }
}
