package ClienteApuestas.guis;

import ClienteApuestas.datos.Jugador;
import ClienteApuestas.datos.JugadorListener;
import ServidorApuestas.datos.Carrera;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

public class VentanaCliente extends JFrame implements JugadorListener, ActionListener {
    private Map<Integer, JButton> botonesApuesta = new HashMap<Integer, JButton>();
    private JLabel labelEstado = new JLabel("Estado: ");
    private JLabel labelDinero = new JLabel("Dinero: ");
    private Jugador jugador;

    public VentanaCliente(Jugador jugador) {
        this.jugador = jugador;
        this.setLayout(new GridLayout(Carrera.NUMERO_CORREDORES + 2, 1));

        for(int i = 0; i< Carrera.NUMERO_CORREDORES; i++) {
            JButton botonApuesta = new JButton("Apostar por el caballo " + String.valueOf(i+1));
            botonApuesta.setActionCommand(String.valueOf(i));
            botonApuesta.addActionListener(this);
            botonesApuesta.put(i, botonApuesta);
            this.add(botonApuesta);

            ImageIcon icon = new ImageIcon("src/main/resources/imagenes/chip50.png");
            this.setIconImage(icon.getImage());

            this.setTitle("Cliente Apuestas");
        }

        this.add(labelEstado);
        this.add(labelDinero);

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.setSize(300,300);
        this.setLocation(0, 0);
        this.setVisible(true);
    }

    public void notifyReinicioCarrera() {
        labelEstado.setText("Carrera en curso");
        for(JButton button : botonesApuesta.values()) {
            button.setEnabled(false);
        }
        repaint();
    }

    public void notifyFinCarrera() {
        labelEstado.setText("Hagan sus apuestas");
        for(JButton button : botonesApuesta.values()) {
            button.setEnabled(true);
        }
        actualizarDinero();
        repaint();
    }

    @Override
    public void notifyCambioDeJugador() {
        labelEstado.setText("Hagan sus apuestas: Dinero: " + jugador.getDineroActual());
        actualizarDinero();
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        jugador.apostarPorCaballo(Integer.valueOf(arg0.getActionCommand()));
        labelEstado.setText("Has apostado por el caballo " + (jugador.getCaballoApostado() + 1));
        habilitarBotones(false);
        actualizarDinero();
    }

    private void habilitarBotones(boolean enable) {
        for(JButton button : botonesApuesta.values()) {
            button.setEnabled(enable);
        }
    }

    private void actualizarDinero() {
        labelDinero.setText("Dinero: " + jugador.getDineroActual());
        labelDinero.repaint();
    }
}