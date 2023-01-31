package ServerApuestas.guis;

import java.awt.BorderLayout;

import javax.swing.*;

import ServerApuestas.datos.Carrera;

public class VentanaServer extends JFrame {
    private Carrera carrera;
    private JLabel labelEstado = new JLabel("");
    private PanelCarrera panel;

    public VentanaServer(Carrera carrera) {
        this.carrera = carrera;
        this.setLayout(new BorderLayout());
        panel = new PanelCarrera(carrera);

        this.add(panel, BorderLayout.CENTER);
        this.add(labelEstado, BorderLayout.SOUTH);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        actualizar();

        this.setSize(1500,800);
        this.setLocation(300, 0);
        this.setVisible(true);
    }

    public void actualizar() {
        panel.frameAnimacion();
        panel.repaint();
        repaint();
    }

    public void notifyFinCarrera() {
        actualizar();
    }

    public void cambiarEstado(String estado) {
        labelEstado.setText(estado);
        repaint();
    }
}
