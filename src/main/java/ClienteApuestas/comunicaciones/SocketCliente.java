package ClienteApuestas.comunicaciones;

import ServidorApuestas.comunicaciones.SocketServidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class SocketCliente {
    private SocketClienteListener chivato;
    public SocketCliente(SocketClienteListener chivato) {
        this.chivato = chivato;
        InetAddress direccion = null;
        try {
            direccion = InetAddress.getByName(SocketServidor.DIRECCION);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }
        byte[] buffer = new byte[1];
        try (MulticastSocket clientSocket = new MulticastSocket(SocketServidor.PUERTO)) {
            clientSocket.joinGroup(direccion);
            while (true) {
                DatagramPacket mensaje = new DatagramPacket(buffer, buffer.length);
                clientSocket.receive(mensaje);
                int valor = Integer.parseInt(new String(buffer, 0, buffer.length));
                if(valor == 9) {
                    chivato.notifyEmpezarCarrera();
                } else {
                    chivato.notifyGanador(valor);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
