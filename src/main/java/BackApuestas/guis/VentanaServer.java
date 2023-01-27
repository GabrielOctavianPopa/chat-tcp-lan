package BackApuestas.guis;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import BackApuestas.datos.Carrera;
import BackApuestas.datos.CarreraListener; //<-- migrar

public class VentanaServer extends JFrame implements CarreraListener { //<-- migrar
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

    @Override
    public void notifyProgresoCarrera() {
        actualizar();
    } //<-- migrar

    @Override
    public void notifyEstado(String estado) {
        labelEstado.setText(estado);
        repaint();
    } //<-- migrar
}
