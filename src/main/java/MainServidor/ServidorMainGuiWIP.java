package MainServidor;

import BackApuestas.ServidorCarrerasLauncher;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServidorMainGuiWIP extends JFrame {
    private JButton botonPuerto, botoniniciar, botoniniciarCarreras , botoniniciarChat, botoniniciarClienteCarreras, botoniniciarLogin;
    private JLabel labelEstado;
    private JPanel jPanel1;
    private JTextField campoPuerto;
    private ServerSocket socketServer;
    private boolean encendido;
    private ArrayList<PrintWriter> clientes = new ArrayList<PrintWriter>();
    private int numeroclientes = 0;

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServidorMainGuiWIP().setVisible(true);
            }
        });
    }

    public ServidorMainGuiWIP() {
        initComponents();
        setLayout(new BorderLayout());

        // Crear elementos de la interfaz
        // boton que inicie el servidor de principal
        //botoniniciar.setIcon(new ImageIcon("src/main/resources/imagenes/iniciar_server.png"));

        // parte para cambiar el puerto principal, se ejecuta cuando se le da a cambiar puerto
        //botonPuerto.setIcon(new ImageIcon("src/main/resources/imagenes/cambiar.png"));

        //boton que inicie el servidor de carreras
        //botoniniciarCarreras.setIcon(new ImageIcon("src/main/resources/imagenes/iniciar_server.png"));

        //boton que inicie el servidor de chat
        //botoniniciarChat.setIcon(new ImageIcon("src/main/resources/imagenes/iniciar_server.png"));

        // Create status bar
        labelEstado = new JLabel("Servidor parado");

        //listener de los botones
        botoniniciar.addActionListener(e ->  {
            startServer();
        });

        botoniniciarCarreras.addActionListener(e ->  {
            startServerCarreras();
        });

        botonPuerto.addActionListener(e ->  {
            cambiarPuerto();
        });

        botoniniciarChat.addActionListener(e ->  {
            startServerChat();
        });

        botoniniciarClienteCarreras.addActionListener(e ->  {
            //startClientCarreras();
        });

        botoniniciarLogin.addActionListener(e ->  {
            //startClientLogin();
        });
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
            //botoniniciar.setIcon(new ImageIcon("src/main/resources/imagenes/stop.png"));
            labelEstado.setText("Servidor inciado en puerto : " + campoPuerto.getText());
            new Thread(new Runnable() {
                public void run() {
                    try {
                        int port = Integer.parseInt(campoPuerto.getText());
                        socketServer = new ServerSocket(port);
                        System.out.println("Server iniciado en:  " + port);
                        botonPuerto.setEnabled(false);
                        campoPuerto.setEnabled(false);
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
            //botoniniciar.setIcon(new ImageIcon("src/main/resources/imagenes/iniciar_server.png"));
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

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanel1 = new JPanel();
        botonPuerto = new JButton();
        campoPuerto = new JTextField();
        botoniniciar = new JButton();
        botoniniciarCarreras = new JButton();
        botoniniciarChat = new JButton();
        labelEstado = new JLabel();
        botoniniciarClienteCarreras = new JButton();
        botoniniciarLogin = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        botonPuerto.setText("Cambiar puerto");
        botonPuerto.setName("botonpuerto"); // NOI18N

        campoPuerto.setText("5000");
        campoPuerto.setToolTipText("");
        campoPuerto.setName("campopuerto"); // NOI18N

        botoniniciar.setText("Encender servidor");
        botoniniciar.setName("botoniniciar"); // NOI18N

        botoniniciarCarreras.setText("Encender Carreras");
        botoniniciarCarreras.setName("botoniniciarCarreras"); // NOI18N
        botoniniciarCarreras.setEnabled(false);

        botoniniciarChat.setText("Encender Chat");
        botoniniciarChat.setName("botoniniciarChat"); // NOI18N
        botoniniciarChat.setEnabled(false);

        labelEstado.setName("labelEstado"); // NOI18N

        botoniniciarClienteCarreras.setText("Crear Cliente");
        botoniniciarClienteCarreras.setName("botoniniciarClienteCarreras"); // NOI18N
        botoniniciarClienteCarreras.setEnabled(false);

        botoniniciarLogin.setText("Crear Login");
        botoniniciarLogin.setName("botoniniciarLogin"); // NOI18N
        botoniniciarLogin.setEnabled(false);

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(labelEstado)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(botoniniciarChat, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(botoniniciar, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(botonPuerto, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(botoniniciarCarreras, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGap(10, 10, 10)
                                                                .addComponent(campoPuerto, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(botoniniciarLogin, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(botoniniciarClienteCarreras, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(botoniniciar)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(botonPuerto)
                                        .addComponent(campoPuerto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(botoniniciarCarreras)
                                        .addComponent(botoniniciarClienteCarreras))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(botoniniciarChat)
                                        .addComponent(botoniniciarLogin))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(labelEstado))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );
        pack();
        //setSize(400, 800);
        setVisible(true);
    }// </editor-fold>
}
