package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static java.lang.System.out;

public class Servidor {
    ServerSocket socketServidor = null;
    Socket socketCliente = null;

    public static void main(String[] args) {
        Servidor server = new Servidor();
    }
    public Servidor() {
        try {
            // Crear el socket del servidor
            socketServidor = new ServerSocket(6000);
            System.out.println("Servidor iniciado");

            // Crear un arraylist para almacenar los clientes conectados
            ArrayList<PrintWriter> clientes = new ArrayList<PrintWriter>();

            // Bucle infinito para esperar conexiones de clientes
            while (true) {
                // Esperar conexión de un cliente
                socketCliente = socketServidor.accept();
                out.println("Cliente conectado desde: " + socketCliente.getInetAddress().getHostAddress());

                // Crear un hilo para manejar la conexión del cliente
                HiloClietes handler = new HiloClietes(socketCliente, clientes);
                Thread hilo = new Thread(handler);
                hilo.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}