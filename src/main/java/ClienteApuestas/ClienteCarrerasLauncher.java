package ClienteApuestas;

import ClienteApuestas.comunicaciones.SocketCliente; //<-- migrar
import ClienteApuestas.comunicaciones.SocketClienteListener; //<-- migrar
import ClienteApuestas.datos.Jugador;
import ClienteApuestas.guis.VentanaCliente;

public class ClienteCarrerasLauncher implements SocketClienteListener, Runnable{ //<-- migrar
    private SocketCliente socket;
    private VentanaCliente gui;
    private Jugador jugador;

    public static void main(String[] args) {
        new ClienteCarrerasLauncher();
    }

    public ClienteCarrerasLauncher() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        jugador = new Jugador();
        gui = new VentanaCliente(jugador);
        jugador.setChivato(gui); //<-- migrar
        socket = new SocketCliente(this);
    }

    @Override
    public void notifyEmpezarCarrera() {
        gui.notifyReinicioCarrera();
    } //<-- migrar

    @Override
    public void notifyGanador(int ganador) { //<-- migrar
        gui.notifyFinCarrera(); //<-- migrar
        jugador.notifyGanador(Integer.valueOf(ganador)); //<-- migrar
    }
}
