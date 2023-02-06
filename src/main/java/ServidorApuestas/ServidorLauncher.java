package ServidorApuestas;

import ServidorApuestas.comunicaciones.SocketServidor;
import ServidorApuestas.datos.Caballo;
import ServidorApuestas.datos.Carrera;
import ServidorApuestas.guis.VentanaServer;

import javax.swing.*;
import java.util.Date;

public class ServidorLauncher extends SwingWorker<Void, Void> {

    private static final int TIEMPO_ENTRE_CARRERAS = 10;
    private Carrera carrera;
    Date tiempoActual = null;
    private SocketServidor socket;

    @Override
    protected Void doInBackground() throws Exception {
        tiempoActual = new Date();
        carrera = new Carrera();

        socket = new SocketServidor();

        VentanaServer gui = new VentanaServer(carrera);
        carrera.setChivato(gui);

        while (!isCancelled()) {
            long tiempoParaCorrer = (new Date().getTime() - tiempoActual.getTime()) / 1000;
            while (tiempoParaCorrer < TIEMPO_ENTRE_CARRERAS) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                gui.notifyEstado("La carrera empieza en: " + (TIEMPO_ENTRE_CARRERAS - tiempoParaCorrer) + " segundos.");
                tiempoParaCorrer = (new Date().getTime() - tiempoActual.getTime()) / 1000;
            }

            socket.notifyInicioCarrera();
            carrera.reiniciar();
            gui.notifyEstado("Carrera en progreso");
            Caballo caballoGanador = carrera.caballoCorredor();
            socket.notifyFinCarrera(caballoGanador);
            tiempoActual = new Date();
        }

        return null;
    }

    public static void main(String[] args) {
        ServidorLauncher servidor = new ServidorLauncher();
        servidor.execute();
    }
}