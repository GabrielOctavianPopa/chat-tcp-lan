package ClienteApuestas;

import ClienteApuestas.comunicaciones.SocketCliente;
import ClienteApuestas.comunicaciones.SocketClienteListener;
import ClienteApuestas.datos.Jugador;
import ClienteApuestas.guis.VentanaCliente;
import ServidorApuestas.ServidorLauncher;

import javax.swing.*;

public class ClienteLauncher extends SwingWorker<Void, Void> implements SocketClienteListener {
    private SocketCliente socket;
    private VentanaCliente gui;
    private Jugador jugador;

    @Override
    protected Void doInBackground() throws Exception {
        jugador = new Jugador();
        gui = new VentanaCliente(jugador);
        jugador.setChivato(gui);
        socket = new SocketCliente(this);

        return null;
    }

    public static void main(String[] args) {
        ClienteLauncher cliente = new ClienteLauncher();
        Thread hiloCliente = new Thread(cliente);
        hiloCliente.start();
        cliente.execute();
    }

    @Override
    public void notifyEmpezarCarrera() {
        gui.notifyReinicioCarrera();
    }

    @Override
    public void notifyGanador(int ganador) {
        gui.notifyFinCarrera();
        jugador.notifyGanador(Integer.valueOf(ganador));
    }
}
