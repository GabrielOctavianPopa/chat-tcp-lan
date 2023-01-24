package ClienteApuestas;

import ClienteApuestas.comunicaciones.SocketCliente; //<-- migrar
import ClienteApuestas.comunicaciones.SocketClienteListener; //<-- migrar
import ClienteApuestas.datos.Jugador;
import ClienteApuestas.guis.VentanaCliente;

public class ClienteLauncher implements SocketClienteListener { //<-- migrar
    private SocketCliente socket;
    private VentanaCliente gui;
    private Jugador jugador;

    public ClienteLauncher() {
        jugador = new Jugador();
        gui = new VentanaCliente(jugador);
        jugador.setChivato(gui); //<-- migrar
        socket = new SocketCliente(this);
    }

    public static void main(String[] args) {
        new ClienteLauncher();
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
