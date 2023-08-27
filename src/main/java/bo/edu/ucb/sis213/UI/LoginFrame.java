package bo.edu.ucb.sis213.UI;

import javax.swing.*;
import bo.edu.ucb.sis213.DAO.Operaciones;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class LoginFrame extends JFrame {
    private JTextField aliasField;
    private JPasswordField pinField;
    private JButton loginButton;
    private int usuarioId;
    private int intentos = 3;

    public LoginFrame(Connection connection) {
        setTitle("INICIO DE SESION");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout()); // Cambio en el layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaciado interno
        

        JLabel aliasLabel = new JLabel("ALIAS:");
        aliasLabel.setFont(aliasLabel.getFont().deriveFont(Font.BOLD));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END; // Alineación del label a la derecha
        panel.add(aliasLabel, gbc);

        aliasField = new JTextField(10); // Tamaño preferido
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START; // Alineación del campo a la izquierda
        panel.add(aliasField, gbc);

        JLabel pinLabel = new JLabel("PIN:");
        pinLabel.setFont(pinLabel.getFont().deriveFont(Font.BOLD));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(pinLabel, gbc);

        pinField = new JPasswordField(10); // Tamaño preferido
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(pinField, gbc);

        loginButton = new JButton("Iniciar Sesión");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Ocupa dos columnas
        gbc.anchor = GridBagConstraints.CENTER; // Centrado horizontal
        panel.add(loginButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (intentos > 0) {
                    String alias = aliasField.getText();
                    int pin = Integer.parseInt(new String(pinField.getPassword()));
                    usuarioId = Operaciones.validarUsuario(connection, pin, alias);
                    if (usuarioId != -1) {
                        dispose();
                        mostrarMenu(connection, usuarioId);
                    } else {
                        intentos--;
                        if (intentos > 0) {
                            JOptionPane.showMessageDialog(null, "Alias o PIN incorrecto. Intentos restantes: " + intentos);
                        } else {
                            JOptionPane.showMessageDialog(null, "Demasiados intentos fallidos. Sistema Cerrado");
                            System.exit(0);
                        }
                    }
                }
            }
        });
        add(panel);
        
        // Centrar el panel en el frame
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setAlignmentY(Component.CENTER_ALIGNMENT);
    }

    private void mostrarMenu(Connection connection, int usuarioId) {
        MenuFrame menuFrame = new MenuFrame(connection, usuarioId);
        menuFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Connection connection = null; // Aquí debes establecer tu conexión a la base de datos
                LoginFrame loginFrame = new LoginFrame(connection);
                loginFrame.setVisible(true);
            }
        });
    }
}
