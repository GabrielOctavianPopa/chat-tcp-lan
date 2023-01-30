package ServerApuestas.comunicaciones;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import ServerApuestas.datos.Caballo;

/**
 * TODO: MIGRAR DE ESTE ESQUEMA DE COMUNICACIONES A UNA MAS PARECIDA A LA DEL CHAT
 */

@Deprecated
public class SocketServidor {

    public static String DIRECCION = "10.2.9.16";
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
        System.out.println("Servidor enviando: " + mensaje);
        DatagramPacket paquete = new DatagramPacket(String.valueOf(mensaje).getBytes(), String.valueOf(mensaje).length(), direccion, PUERTO);
        try {
            socketServidor.send(paquete);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
