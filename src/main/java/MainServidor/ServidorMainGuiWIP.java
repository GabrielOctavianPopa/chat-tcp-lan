package MainServidor;

import BackApuestas.ServidorCarrerasLauncher;
import ClienteApuestas.ClienteCarrerasLauncher;
import ClienteChat.Login;

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
    private JTextField campoPuerto;
    private ServerSocket socketServer;
    private boolean encendidoMain, encendidoCarreras, encendidoChat;
    private ArrayList<PrintWriter> clientes = new ArrayList<PrintWriter>();
    private int numeroclientes = 0;

    public static void main(String[] args) {
        EventQueue.invokeLater(() ->
            new ServidorMainGuiWIP().setVisible(true)
        );
    }

    public ServidorMainGuiWIP() {
        // Crear elementos de la interfaz
        initComponents();
        setLayout(new BorderLayout());

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
            startClientCarreras();
        });

        botoniniciarLogin.addActionListener(e ->  {
            startClientLogin();
        });
    }

    private void startServer() {
        if (!encendidoMain) {
            encendidoMain = true;
            botoniniciar.setText("Parar Servidor");
            //botoniniciar.setIcon(new ImageIcon("src/main/resources/imagenes/stop.png"));
            labelEstado.setText("Servidor inciado en puerto : " + campoPuerto.getText());
            new Thread(() -> {
                try {
                    int port = Integer.parseInt(campoPuerto.getText());
                    socketServer = new ServerSocket(port);
                    System.out.println("Server iniciado en:  " + port);
                    //logica botones
                    botonPuerto.setEnabled(false);
                    campoPuerto.setEnabled(false);
                    botoniniciarChat.setEnabled(true);
                    botoniniciarCarreras.setEnabled(true);
                    while (encendidoMain) {
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
            }).start();
        } else {
            encendidoMain = false;
            botoniniciar.setText("Iniciar Servidor");
            //botoniniciar.setIcon(new ImageIcon("src/main/resources/imagenes/iniciar_server.png"));
            labelEstado.setText("Servidor parado");
            botonPuerto.setEnabled(true);
            campoPuerto.setEnabled(true);
            botoniniciarChat.setEnabled(false);
            botoniniciarCarreras.setEnabled(false);
            botoniniciarClienteCarreras.setEnabled(false);
            botoniniciarLogin.setEnabled(false);
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
            System.out.println("Puerto del servidor cambiado a " + puerto + ".");
        } catch (NumberFormatException e) {
            System.out.println("Numero de puerto no valido.");
        }
    }

    //Todo: cuando se cierra el servidor de carreras se cierra el servidor principal
    private void startServerCarreras() {
        if(!encendidoCarreras){
            encendidoCarreras = true;
            botoniniciarCarreras.setText("Parar Servidor Carreras");
            botoniniciarClienteCarreras.setEnabled(true);
            Thread threadServer = new Thread(new ServidorCarrerasLauncher());
            threadServer.start();
        } else if (encendidoCarreras) {
            encendidoCarreras = false;
            botoniniciarCarreras.setText("Iniciar Servidor Carreras");
            botoniniciarClienteCarreras.setEnabled(false);
            //Todo: matar el proceso del servidor de carreras
        }
    }

    //Todo: arreglar el cliente, da error porque no es multicast, migrar esquema
    private void startClientCarreras(){
        Thread threadClient = new Thread(new ClienteCarrerasLauncher());
        if(encendidoCarreras){
            threadClient.start();
        } else {
            JOptionPane.showMessageDialog(null, "El servidor principal no esta encendido");
        }
    }

    private void startServerChat(){
        Thread threadServer = new Thread(new ServidorChat());
        if(!encendidoChat){
            encendidoChat = true;
            botoniniciarChat.setText("Parar Servidor Chat");
            botoniniciarLogin.setEnabled(true);
            threadServer.start();
        } else if (encendidoChat) {
            encendidoChat = false;
            botoniniciarChat.setText("Iniciar Servidor Chat");
            botoniniciarLogin.setEnabled(false);
            //Todo: para el hilo pero no libera el puerto 6000
            System.out.println("Servidor de chat parado");
        }
    }

    private void startClientLogin(){ //Todo: cuando se cierra el cliente de login se cierra el servidor principal
        if(encendidoChat){
            Login login = new Login();
            Thread threadLogin = new Thread(login);
            threadLogin.start();
        } else {
            JOptionPane.showMessageDialog(null, "El servidor de chat no esta disponible.");
        }
    }


    //Todo: cambiar la interfaz para hacerla compatible con imagenes, cuando se activa uno de los botones
    @SuppressWarnings("unchecked")
    private void initComponents() {
        JPanel jPanel1 = new JPanel();
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
                                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, true)
                                                        .addComponent(botoniniciarChat, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(botoniniciar, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(botonPuerto, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(botoniniciarCarreras, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGap(10, 10, 50)
                                                                .addComponent(campoPuerto, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, true)
                                                                        .addComponent(botoniniciarLogin, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(botoniniciarClienteCarreras, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(botoniniciar)
                                .addGap(18, 18, 50)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(botonPuerto)
                                        .addComponent(campoPuerto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 50)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(botoniniciarCarreras)
                                        .addComponent(botoniniciarClienteCarreras))
                                .addGap(18, 18, 50)
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
        setSize(400, 300); //debug
        setVisible(true);
    }// </editor-fold>
}
