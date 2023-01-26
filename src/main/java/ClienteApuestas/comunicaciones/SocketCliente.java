package ClienteApuestas.comunicaciones;

import java.io.IOException;
import java.net.DatagramPacket; //<-- migrar
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import BackApuestas.comunicaciones.SocketServidor;

//Todo: implementar el handler de los mensajes que uso en el chat cliente para migrar del esquema de datagramas a este
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
                System.out.println("El cliente ha recibido: " + valor);
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
