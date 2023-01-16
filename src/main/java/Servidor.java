import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.sql.*;
import static java.lang.System.out;

public class Servidor {
    public static void main(String[] args) {
        Servidor server = new Servidor();
    }

    public Servidor() {
        try {
            // Crear el socket del servidor
            ServerSocket serverSocket = new ServerSocket(6000);
            out.println("Servidor iniciado");

            // Crear un arraylist para almacenar los clientes conectados
            ArrayList<PrintWriter> clientes = new ArrayList<PrintWriter>();

            // Bucle infinito para esperar conexiones de clientes
            while (true) {
                // Esperar conexión de un cliente
                Socket socketServer = serverSocket.accept();
                out.println("Cliente conectado desde: " + socketServer.getInetAddress().getHostAddress());

                // Crear un hilo para manejar la conexión del cliente
                ClientHandler handler = new ClientHandler(socketServer, clientes);
                Thread hilo = new Thread(handler);
                hilo.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ClientHandler implements Runnable {
        private Socket socket;
        private ArrayList<PrintWriter> clientes;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket, ArrayList<PrintWriter> clientes) {
            this.socket = socket;
            this.clientes = clientes;
        }

        public void run() {
            try {
                // Obtener el stream de entrada y salida del cliente
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Añadir el stream de salida del cliente al arraylist de clientes
                clientes.add(out);

                // get the IP of the client
                String clientIP = socket.getInetAddress().getHostAddress();

                // Bucle infinito para esperar y recibir mensajes del cliente
                while (true) {
                    // Recibir el mensaje del cliente
                    String mensaje = in.readLine();

                    // separa el mensaje recibido en un arreglo de strings
                    String[] parts = mensaje.split(",");
                    String comando = parts[0];
                    if (comando.equals("LOGIN")) {
                        login(mensaje, clientIP);
                    }else{
                        // Enviar el mensaje a todos los clientes
                        for (PrintWriter cliente : clientes) {
                            cliente.println(mensaje);
                        }
                    }
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } finally {
                // Eliminar el stream de salida del cliente del arraylist de clientes
                clientes.remove(out);
                try {
                    // Cerrar el socket del cliente
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public boolean isLoginValid(String username, String password, String clientIP) {
            boolean isValid = false;
            try {
                // Connect to the database
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/chat_db","root", "");
                // Create a statement
                Statement statement = con.createStatement();
                // Execute a query to check if the username, password, and IP match
                ResultSet result = statement.executeQuery("SELECT COUNT(*) as count FROM users WHERE nombre = '" + username + "' AND contraseña = PASSWORD('" + password +"') AND ip = '" + clientIP + "'");
                if (result.next()) {
                    int count = result.getInt(1);
                    if (count > 0) {
                        isValid = true;
                        System.out.println("Login successful");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return isValid;
        }

        public void login(String request, String clientIP) {
            try {
                String[] parts = request.split(","); // divide la petición en tres partes, el comando "LOGIN", usuario y contraseña
                String usuario = parts[1];
                String contraseña = parts[2];

                if (isLoginValid(usuario, contraseña, clientIP)) {
                    out.println("OK");
                } else {
                    out.println("ERROR");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}