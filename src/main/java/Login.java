import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.io.*;
import java.net.Socket;
import java.awt.event.KeyEvent;

public class Login extends JFrame {
    // Atributos para la interfaz gráfica de usuario
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JCheckBox rememberMeCheckbox;
    private JLabel errorLabel;
    private JLabel guideLabel;
    // Atributos para la conexión y la comunicación
    private Socket socket;
    private PrintWriter out;
    private BufferedReader input;

    public Login() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // Inicializar la interfaz gráfica de usuario
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        //tamaño de la imagen a 16x16
        ImageIcon loginIcon = new ImageIcon( "src/main/resources/login.png" );
        loginButton = new JButton("Iniciar sesión");

        loginIcon = new ImageIcon( loginIcon.getImage().getScaledInstance( 128, 42, Image.SCALE_DEFAULT ) );
        loginButton = new JButton(  loginIcon );
        rememberMeCheckbox = new JCheckBox("Recordarme");
        errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        guideLabel = new JLabel("Introduce tu nombre de usuario y contraseña");
        guideLabel.setForeground(new Color(0, 0, 200));

        // Añadir evento de botón para iniciar sesión
        loginButton.addActionListener(e ->  {
            tryConnect();
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
        add(guideLabel, constraints);

        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.WEST;
        add(new JLabel("Nombre de usuario:"), constraints);

        constraints.gridx = 1;
        add(usernameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        add(new JLabel("Contraseña:"), constraints);

        constraints.gridx = 1;
        add(passwordField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.WEST;
        add(rememberMeCheckbox, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.anchor = GridBagConstraints.CENTER;
        add(loginButton, constraints);

        constraints.gridx = 1;


        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        add(errorLabel, constraints);

        // Ajustar tamaño del marco y hacerlo visible
        setSize(400, 300);
        setTitle("Iniciar sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void tryConnect(){
        loginButton.addActionListener(e -> {
            if(validateForm()) {
                try {
                    String username = usernameField.getText();
                    char[] password = passwordField.getPassword();
                    String passwordString = new String(password);

                    //Conectar con el servidor
                    connect();

                    out.println("LOGIN," + username + "," + passwordString);
                    String response = input.readLine();
                    if (response.equals("OK")) {
                        if (rememberMeCheckbox.isSelected()) {
                            // Guardar credenciales en un archivo o en el registro
                        }
                        ChatClient chat = new ChatClient(username);
                        chat.setVisible(true);
                        dispose();
                    } else if (response.equals("ERROR")) {
                        errorLabel.setText("Nombre de usuario o contraseña incorrectos");
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);

                }
            }
        });
    }

    private boolean validateForm() {
        if(usernameField.getText().isEmpty()) {
            errorLabel.setText("Introduce un nombre de usuario");
            return false;
        }
        if(passwordField.getPassword().length == 0) {
            errorLabel.setText("Introduce una contraseña");
            return false;
        }
        return true;
    }

    public void connect() throws IOException {
        socket = new Socket("10.2.9.16", 6000);
        out = new PrintWriter(socket.getOutputStream(), true);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public static void main(String[] args) {
        Login login = new Login();
        login.setVisible(true);
    }
}