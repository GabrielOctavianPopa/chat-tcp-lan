package ServidorApuestas.guis;

import ServidorApuestas.datos.Carrera;
import ServidorApuestas.datos.CarreraListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VentanaServer extends JFrame implements CarreraListener {
    private Carrera carrera;
    private JLabel labelEstado = new JLabel("");
    private PanelCarrera panel;

    public VentanaServer(Carrera carrera) {
        this.carrera = carrera;
        this.setLayout(new BorderLayout());
        panel = new PanelCarrera(carrera);

        this.add(panel, BorderLayout.CENTER);
        this.add(labelEstado, BorderLayout.SOUTH);

        ImageIcon icono = new ImageIcon("src/main/resources/imagenes/horse.png");
        this.setIconImage(icono.getImage());
        this.setTitle("Servidor Apuestas");

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

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

    @Override
    public void notifyProgresoCarrera() {
        actualizar();
    }

    @Override
    public void notifyEstado(String estado) {
        labelEstado.setText(estado);
        repaint();
    }
}
