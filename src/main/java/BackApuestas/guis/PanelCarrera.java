package BackApuestas.guis;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import BackApuestas.datos.Carrera;

public class PanelCarrera extends JPanel {
    BufferedImage frameCaballo1 = null;
    BufferedImage frameCaballo2 = null;
    BufferedImage frameCaballo3 = null;
    private Carrera carrera;
    private int contador;

    public PanelCarrera(Carrera carrera) {
        super();
        this.carrera = carrera;
        try {
            frameCaballo1 = ImageIO.read(new File("src/main/resources/imagenes/frame1.png"));
            frameCaballo2 = ImageIO.read(new File("src/main/resources/imagenes/frame2.png"));
            frameCaballo3 = ImageIO.read(new File("src/main/resources/imagenes/frame3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void frameAnimacion() {
        contador++;
    }

    public void paint(Graphics g) {
        int frameAnimacion = contador % 3;
        int alturaLinea = this.getHeight() / Carrera.NUMERO_CORREDORES;
        int anchuraLinea = this.getWidth() - frameCaballo1.getWidth();

        int y = 0;
        for(int i = 0; i< Carrera.NUMERO_CORREDORES; i++) {
            int x = (int)(carrera.getCorredores().get(i).getFinalCarrera() * anchuraLinea);
            if(frameAnimacion == 0) {
                g.drawImage(frameCaballo1, x, y, frameCaballo1.getWidth(), alturaLinea, null);
            }
            if(frameAnimacion == 1) {
                g.drawImage(frameCaballo2, x, y, frameCaballo2.getWidth(), alturaLinea, null);
            }
            if(frameAnimacion == 2) {
                g.drawImage(frameCaballo3, x, y, frameCaballo3.getWidth(), alturaLinea, null);
            }
            y += alturaLinea;
        }
    }
}