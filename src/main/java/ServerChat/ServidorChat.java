package ServerChat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import static java.lang.System.out;

public class ServidorChat implements Runnable{
    ServerSocket socketServidor = null;
    Socket socketCliente = null;

    public static void main(String[] args) {
        new ServidorChat();
    }

    public ServidorChat() {

    }

    @Override
    public void run() {
        try {
            // Crear el socket del servidor
            socketServidor = new ServerSocket(6000);
            System.out.println("Servidor de chat iniciado en el puerto 6000");

            // Crear un arraylist para almacenar los clientes conectados
            ArrayList<PrintWriter> clientes = new ArrayList<PrintWriter>();

            // Bucle infinito para esperar conexiones de clientes
            try{
                while (true) {
                    // Esperar conexión de un cliente
                    socketCliente = socketServidor.accept();
                    out.println("Cliente conectado desde: " + socketCliente.getInetAddress().getHostAddress());

                    // Crear un hilo para manejar la conexión del cliente
                    HiloClientes handler = new HiloClientes(socketCliente, clientes);
                    Thread hilo = new Thread(handler);
                    hilo.start();
                }
            } catch (SocketException e){
                System.out.println("Servidor de chat cerrado");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closePort(){
        try{
            socketServidor.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}