package MainServidor;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import BackApuestas.ServidorCarrerasLauncher;

public class ServidorMainGui extends JFrame {

    private JButton botoniniciar, botonpuerto, botoniniciarCarreras;
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
            changePort();
        });

        // añadir los elementos a la interfaz
        portPanel.add(labelPuerto);
        portPanel.add(campoPuerto);
        portPanel.add(botonpuerto);
        portPanel.add(botoniniciarCarreras);

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
        ServidorCarrerasLauncher server = new ServidorCarrerasLauncher();
        server.iniciarServidor();
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
                        log("Server iniciado en:  " + port);
                        while (encendido) {
                            // Esperando a la conexion del cliente
                            Socket clientSocket = socketServer.accept();
                            log("Cliente conectado desde: " + clientSocket.getInetAddress().getHostAddress());
                            numeroclientes++;
                            labelEstado.setText("Clientes conectados: " + numeroclientes);

                            // Creamos un hilo para los diferentes clientes conectados
                            HiloClientes handler = new HiloClientes(clientSocket, clientes);
                            Thread thread = new Thread(handler);
                            thread.start();
                        }
                    } catch (IOException e) {
                        log("Error: " + e.getMessage());
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
                log("Servidor Parado");
            } catch (IOException e) {
                log("Error: " + e.getMessage());
            }
        }
    }

    private void changePort() {
        String newPort = campoPuerto.getText();
        try {
            int port = Integer.parseInt(newPort);
            if (port < 1 || port > 65535) {
                throw new NumberFormatException();
            }
            log("Puerto cambiado a " + port);
        } catch (NumberFormatException e) {
            log("Numero invalido");
        }
    }

    private void log(String mensaje) {
        areaLog.append(mensaje + "\n");
    }

    public static void main(String[] args) {
        new ServidorMainGui();
    }
}
