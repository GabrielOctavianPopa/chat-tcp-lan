package BackApuestas;

import java.io.PrintWriter;
import java.util.Date;

import BackApuestas.comunicaciones.SocketServidor; //<-- migrar
import BackApuestas.datos.Caballo;
import BackApuestas.datos.Carrera;
import BackApuestas.guis.VentanaServer;

public class ServidorCarrerasLauncher {

    private static final int TIEMPO_ENTRE_CARRERAS = 10;
    private Carrera carrera;
    Date tiempoActual = null;
    private SocketServidor socket;

    private PrintWriter out;

    public static void main(String[] args) {
        new ServidorCarrerasLauncher();
    }

    public ServidorCarrerasLauncher() {
        tiempoActual = new Date();
        carrera = new Carrera();

        socket = new SocketServidor();

        VentanaServer gui = new VentanaServer(carrera);
        carrera.setChivato(gui); //<-- migrar

        while(true) {
            long tiempoParaCorrer = (new Date().getTime() - tiempoActual.getTime()) / 1000;
            while(tiempoParaCorrer < TIEMPO_ENTRE_CARRERAS) {
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException e) {}
                gui.notifyEstado("La carrera empieza en: " + (TIEMPO_ENTRE_CARRERAS - tiempoParaCorrer) + " segundos."); //<-- migrar
                tiempoParaCorrer = (new Date().getTime() - tiempoActual.getTime()) / 1000;
            }

            socket.notifyInicioCarrera();//<-- migrar
            carrera.reiniciar();//<-- creo que migrar
            gui.notifyEstado("Carrera en progreso"); //<-- migrar
            Caballo caballoGanador = carrera.caballoCorredor();
            socket.notifyFinCarrera(caballoGanador); //<-- migrar
            tiempoActual = new Date();
        }
    }

    public void iniciarServidor(){
        new ServidorCarrerasLauncher();
    }
}
