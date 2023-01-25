package MainServidor;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import BackApuestas.ServidorCarrerasLauncher;

import javax.swing.*;

public class ServidorMainGui extends JFrame {

    private JButton botoniniciar, botonpuerto, botoniniciarCarreras, botoniniciarChat;
    private JTextField campoPuerto;
    private JLabel labelPuerto, labelEstado;
    private JTextArea areaLog;
    private ServerSocket socketServer;
    private boolean encendido;
    private ArrayList<PrintWriter> clientes = new ArrayList<PrintWriter>();
    private int numeroclientes = 0;

    public ServidorMainGui() {
        super("Aplicacion servidor");
        setLayout(new BorderLayout());

        // Crear elementos de la interfaz
        // boton que inicie el servidor de principal
        botoniniciar = new JButton("Iniciar Servidor");
        botoniniciar.setIcon(new ImageIcon("src/main/resources/imagenes/iniciar_server.png"));

        // parte para cambiar el puerto principal, se ejecuta cuando se le da a cambiar puerto
        labelPuerto = new JLabel("Puerto:");
        campoPuerto = new JTextField("6000", 5);
        botonpuerto = new JButton("Cambiar Puerto");
        botonpuerto.setIcon(new ImageIcon("src/main/resources/imagenes/cambiar.png"));

        //crea un panel para los elementos de puerto
        JPanel portPanel = new JPanel();

        //boton que inicie el servidor de carreras
        botoniniciarCarreras = new JButton("Iniciar Carreras");
        botoniniciarCarreras.setIcon(new ImageIcon("src/main/resources/imagenes/iniciar_server.png"));

        //boton que inicie el servidor de chat
        botoniniciarChat = new JButton("Iniciar Chat");
        botoniniciarChat.setIcon(new ImageIcon("src/main/resources/imagenes/iniciar_server.png"));

        // Create log area
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        JScrollPane logScroll = new JScrollPane(areaLog);

        // Create status bar
        labelEstado = new JLabel("Servidor parado");

        //listener de los botones
        botoniniciar.addActionListener(e ->  {
            startServer();
        });

        botoniniciarCarreras.addActionListener(e ->  {
            startServerCarreras();
        });

        botonpuerto.addActionListener(e ->  {
            cambiarPuerto();
        });

        botoniniciarChat.addActionListener(e ->  {
            startServerChat();
        });

        // añadir los elementos a la interfaz
        portPanel.add(labelPuerto);
        portPanel.add(campoPuerto);
        portPanel.add(botonpuerto);
        //temporal, cambiar a un pane
        portPanel.add(botoniniciarCarreras);
        portPanel.add(botoniniciarChat);

        add(logScroll, BorderLayout.SOUTH);

        add(botoniniciar, BorderLayout.NORTH);

        add(portPanel, BorderLayout.CENTER);

        add(labelEstado, BorderLayout.SOUTH);

        // set theme
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set window properties
        setSize(400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void startServerCarreras() {
        ServidorCarrerasLauncher server = new ServidorCarrerasLauncher();//:D
        Thread threadServer = new Thread(server);
        threadServer.start();
    }

    private void startServerChat(){
        ServidorChat servidorChat = new ServidorChat();
        Thread threadServer = new Thread(servidorChat);
        threadServer.start();
    }


    private void startServer() {
        if (!encendido) {
            encendido = true;
            botoniniciar.setText("Parar Servidor");
            botoniniciar.setIcon(new ImageIcon("src/main/resources/imagenes/stop.png"));
            labelEstado.setText("Servidor inciado en puerto : " + campoPuerto.getText());
            new Thread(new Runnable() {
                public void run() {
                    try {
                        int port = Integer.parseInt(campoPuerto.getText());
                        socketServer = new ServerSocket(port);
                        System.out.println("Server iniciado en:  " + port);
                        while (encendido) {
                            // Esperando a la conexion del cliente
                            Socket clientSocket = socketServer.accept();
                            System.out.println("Cliente conectado desde: " + clientSocket.getInetAddress().getHostAddress());
                            numeroclientes++;
                            labelEstado.setText("Clientes conectados: " + numeroclientes);

                            // Creamos un hilo para los diferentes clientes conectados
                            HiloClientes handler = new HiloClientes(clientSocket, clientes);
                            Thread thread = new Thread(handler);
                            thread.start();
                        }
                    } catch (IOException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            }).start();
        } else {
            encendido = false;
            botoniniciar.setText("Iniciar Servidor");
            botoniniciar.setIcon(new ImageIcon("src/main/resources/imagenes/iniciar_server.png"));
            labelEstado.setText("Servidor parado");
            try {
                socketServer.close();
                System.out.println("Servidor Parado");
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void cambiarPuerto() {
        String nuevoPuerto = campoPuerto.getText();
        try {
            int puerto = Integer.parseInt(nuevoPuerto);
            if (puerto < 1 || puerto > 65535) {
                throw new NumberFormatException();
            }
            System.out.println("Puerto cambiado a " + puerto + ".");
        } catch (NumberFormatException e) {
            System.out.println("Numero de puerto no valido.");
        }
    }

    private void log(String mensaje) {
        areaLog.append(mensaje + "\n");
    }

    public static void main(String[] args) {
        new ServidorMainGui();
    }
}
