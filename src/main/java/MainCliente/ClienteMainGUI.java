package MainCliente;

import ClienteApuestas.ClienteLauncher;
import ClienteChat.Login;

import javax.swing.*;
import java.awt.*;

class ClienteMainGUI {
    Button botonConectar, botonCrearClienteCarreras, botonCrearLogin;
    boolean conectado = false;
    public static void main(String[] args) {
        new ClienteMainGUI();
    }

    public ClienteMainGUI() {
        initComponents();
    }

    public void initComponents(){
        JFrame frame = new JFrame("Cliente");
        botonConectar = new Button("On");
        botonCrearLogin = new Button("Login");
        botonCrearLogin.setEnabled(false);
        botonCrearClienteCarreras = new Button("Crear Cliente Carreras");
        botonCrearClienteCarreras.setEnabled(false);

        botonConectar.addActionListener(e -> {
            if (!conectado) {
                botonConectar.setLabel("Off");
                conectado = true;
                botonCrearLogin.setEnabled(true);
                botonCrearClienteCarreras.setEnabled(true);
            } else if(conectado){
                botonConectar.setLabel("On");
                conectado = false;
                botonCrearLogin.setEnabled(false);
                botonCrearClienteCarreras.setEnabled(false);
            }
        });

        botonCrearLogin.addActionListener(e -> {
            Thread hiloLogin = new Thread(new Login());
            hiloLogin.start();
        });

        botonCrearClienteCarreras.addActionListener(e -> {
            Thread hiloClienteCarreras = new Thread(new ClienteLauncher());
            hiloClienteCarreras.start();
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new FlowLayout());
        frame.setVisible(true);

        frame.add(botonConectar);
        frame.add(botonCrearLogin);
        frame.add(botonCrearClienteCarreras);
        frame.pack();
    }
}