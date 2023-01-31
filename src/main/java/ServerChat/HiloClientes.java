package ServerChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;

public class HiloClientes implements Runnable {
    private Socket socket = null;
    private  ArrayList<PrintWriter> clientes = null;
    public static String[] usuarios = new String[20];
    int i=0;
    private BufferedReader bufferedRead = null;
    private PrintWriter printWrite = null;
    private String clientIP = "";
    private final String URL = "jdbc:mysql://localhost:3307/chat_db"; // URL de la base de datos, cambiar el puerto si es necesario (3306 por defecto)
    private final String nombreSQL = "root";
    private final String passwordSQL = "";
    private final String RESPUESTA_VALIDO = "OK";
    private final String RESPUESTA_ERROR = "ERROR";
    private HiloOnline hilo;


    public HiloClientes(Socket socket, ArrayList<PrintWriter> clientes,HiloOnline hilo) {
        this.socket = socket;
        this.clientes = clientes;
        this.hilo=hilo;

    }


    public void run() {
        try {
            // Obtener el stream de entrada y salida del cliente
            bufferedRead = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWrite = new PrintWriter(socket.getOutputStream(), true);

            // Añadir el stream de salida del cliente al arraylist de clientes
            clientes.add(printWrite);

            // coge la ip del cliente
            clientIP = socket.getInetAddress().getHostAddress();

            // Bucle infinito para esperar y recibir mensajes del cliente
            while (true) {
                // Recibir el mensaje del cliente
                String mensaje = bufferedRead.readLine();

                // separa el mensaje recibido en un arreglo de strings
                String[] parts = mensaje.split(",");
                String comando = parts[0];
                if (comando.equals("LOGIN")) {
                    login(mensaje);
                }else {
                    // Enviar el mensaje a todos los clientes
                    for (PrintWriter cliente : clientes) {
                        cliente.println(mensaje);
                    }
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
//        } finally {
//            // Eliminar el stream de salida del cliente del arraylist de clientes
//            clientes.remove(printWrite);
//            try {
//                // Cerrar el socket del cliente
//                socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        }
    }

    public boolean esLoginValido(String nombreUsuario, String contraseña) {
        boolean esValido = false;
        try {
            // Conectar a la base de datos
            Connection con = DriverManager.getConnection(URL,nombreSQL, passwordSQL);

            // Crea un prepared statement
            PreparedStatement pstmt = con.prepareStatement("SELECT COUNT(*) FROM users WHERE nombre = ? AND contraseña = PASSWORD(?)");

            // Asigna los valores a los parametros (?) del prepared statement
            pstmt.setString(1, nombreUsuario);
            pstmt.setString(2, contraseña);

            // Ejecuta la consulta
            ResultSet result = pstmt.executeQuery();
            if (result.next()) {
                int count = result.getInt(1);
                if (count > 0) {
                    esValido = true;
                    System.out.println("Login correcto: " + nombreUsuario); //<-----------
                    hilo.setnuevousuario(nombreUsuario);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return esValido;
    }

    public void comprobarConexion(){
        printWrite.println("El servidor esta encendido");
    }

    public void login(String peticionLogin) {
        try {
            String[] parts = peticionLogin.split(","); // divide la petición en tres partes, el comando "LOGIN", usuario y contraseña
            String usuario = parts[1];
            String contraseña = parts[2];

            if (esLoginValido(usuario, contraseña)) {
                printWrite.println(RESPUESTA_VALIDO);
            } else {
                printWrite.println(RESPUESTA_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}