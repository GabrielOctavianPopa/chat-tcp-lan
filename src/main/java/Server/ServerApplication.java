package Server;

import Client.Login;

public class ServerApplication {
    // Crear un objeto servidor y ejecutarlo y crear un objeto login y ejecutarlo
    public static void main(String[] args) {
        Servidor server = new Servidor();
        Login login = new Login();
    }
}