package MainServidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class PetitionHandler implements Runnable {
    ServerSocket mainServerSocket;
    String HOST = "10.2.9.16";
    int PORT = 5000;
    PrintWriter out;
    BufferedReader in;

    public static void main(String[] args) {
        new PetitionHandler();
    }

    public PetitionHandler() {

    }

    @Override
    public void run() {
        try{
            mainServerSocket = new ServerSocket(PORT);
            while (true){
                Socket incomingSocket = mainServerSocket.accept();
                out = new PrintWriter(incomingSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(incomingSocket.getInputStream()));
                String petition = in.readLine();
                String returnedString = handlePetition(petition);
                out.println(returnedString);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String handlePetition(String petition) {
        String returnedString = "";
        switch (petition) {
            case "CONN":

                break;
            case "LOGIN":
                // login(mensaje);
                break;
            default:
                // Enviar el mensaje a todos los clientes
                // for (PrintWriter cliente : clientes) {
                //     cliente.println(mensaje);
                // }
                break;
        }
        return returnedString;
    }

}
