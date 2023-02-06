package ServidorApuestas.comunicaciones;

import ServidorApuestas.datos.Caballo;

import java.io.IOException;
import java.net.*;

public class SocketServidor {

    public static String DIRECCION = "224.0.0.1";
    public static int PUERTO = 8888;
    private DatagramSocket socketServidor;
    private InetAddress direccion;

    public SocketServidor() {
        try {
            direccion = InetAddress.getByName(DIRECCION);
            socketServidor = new DatagramSocket();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void notifyInicioCarrera() {
        send(9);
    }

    public void notifyFinCarrera(Caballo caballoGanador) {
        send(caballoGanador.getNumeroCaballo());
    }

    private void send(int mensaje) {
        DatagramPacket paquete = new DatagramPacket(String.valueOf(mensaje).getBytes(), String.valueOf(mensaje).length(), direccion, PUERTO);
        try {
            socketServidor.send(paquete);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
