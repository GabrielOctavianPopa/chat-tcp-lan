package ClienteChat;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class Login extends JFrame implements Runnable {
    // Atributos para la interfaz gráfica de usuario
    private JTextField campoNombreUsuario;
    private JPasswordField campoContraseña;
    private JButton botonLogin;
    private JLabel errorLabel;
    private JLabel labelGuia;
    ImageIcon loginIcon = null;
    // Atributos para la conexión y la comunicación
    private Socket socket;
    private PrintWriter printWrite;
    private BufferedReader bufferedRead;
    private final String HOST = "localhost";
    private final int PUERTO = 6000;

    public static void main(String[] args) {
        new Login();
    }

    public Login() {
    }


    @Override
    public void run() {
        setLocationRelativeTo(null); // para que aparezca en el centro de la pantalla
        // Inicializar la interfaz gráfica de usuario
        campoNombreUsuario = new JTextField(20);
        campoContraseña = new JPasswordField(20);
        //tamaño de la imagen a 16x16
        loginIcon = new ImageIcon( "src/main/resources/imagenes/login.png" );
        botonLogin = new JButton();

        loginIcon = new ImageIcon( loginIcon.getImage().getScaledInstance( 128, 42, Image.SCALE_DEFAULT ) );
        botonLogin = new JButton(  loginIcon );
        errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        labelGuia = new JLabel("Introduce tu nombre de usuario y contraseña");
        labelGuia.setForeground(new Color(0, 0, 200));

        ImageIcon loginIcon = new ImageIcon( "src/main/resources/imagenes/cliente.png" );
        this.setIconImage(loginIcon.getImage());

        // Añadir evento de botón para iniciar sesión
        botonLogin.addActionListener(e -> {
            if(validarCampos()) {
                tryConnect();
            }
        });

        // Añadir evento de botón para registrarse

        // Colocar elementos de la interfaz gráfica de usuario en el marco
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        add(labelGuia, constraints);

        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.WEST;
        add(new JLabel("Nombre de usuario:"), constraints);

        constraints.gridx = 1;
        add(campoNombreUsuario, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        add(new JLabel("Contraseña:"), constraints);

        constraints.gridx = 1;
        add(campoContraseña, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.WEST;

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.anchor = GridBagConstraints.CENTER;
        add(botonLogin, constraints);

        constraints.gridx = 1;

        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        add(errorLabel, constraints);

        // Ajustar tamaño del marco y hacerlo visible
        setSize(400, 300);
        setTitle("Iniciar sesión");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void tryConnect(){
        try {
            String username = campoNombreUsuario.getText();
            char[] password = campoContraseña.getPassword();
            String passwordString = new String(password);

            //Conectar con el servidor
            connect();

            printWrite.println("LOGIN," + username + "," + passwordString);
            String response = bufferedRead.readLine();
            if (response.equals("OK")) {
                ChatClient chatClient = new ChatClient();
                chatClient.setNombreDeUsuario(username);
                chatClient.execute();
                dispose();
            } else if (response.equals("ERROR")) {
                errorLabel.setText("Nombre de usuario o contraseña incorrectos");
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private boolean validarCampos() {
        if(campoNombreUsuario.getText().isEmpty()) {
            errorLabel.setText("Introduce un nombre de usuario");
            return false;
        }
        if(campoContraseña.getPassword().length == 0) {
            errorLabel.setText("Introduce una contraseña");
            return false;
        }
        return true;
    }

    public void connect() throws IOException {
        try{
            socket = new Socket(HOST, PUERTO);
            printWrite = new PrintWriter(socket.getOutputStream(), true);
            bufferedRead = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("El servidor no está disponible");
        }
    }
}
