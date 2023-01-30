package ClienteApuestas.guis;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;



import ClienteApuestas.datos.Jugador;
import ClienteApuestas.datos.JugadorListener;
import ServerApuestas.datos.Carrera;

import javax.swing.*;

public class VentanaCliente extends JFrame implements JugadorListener, ActionListener {
    private Map<Integer, JButton> botonesApuesta = new HashMap<Integer, JButton>();
    private JLabel labelEstado = new JLabel("Estado: ");
    private JLabel labelDinero = new JLabel("Dinero: ");
    private Jugador jugador;

    public VentanaCliente(Jugador jugador) {
        this.jugador = jugador;
        this.setLayout(new GridLayout(Carrera.NUMERO_CORREDORES + 2, 1));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        for(int i = 0; i< Carrera.NUMERO_CORREDORES; i++) {
            JButton botonApuesta = new JButton("Apostar por el caballo " + String.valueOf(i));
            botonApuesta.setActionCommand(String.valueOf(i));
            botonApuesta.addActionListener(this);
            botonesApuesta.put(i, botonApuesta);
            this.add(botonApuesta);
        }

        this.add(labelEstado);
        this.add(labelDinero);
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