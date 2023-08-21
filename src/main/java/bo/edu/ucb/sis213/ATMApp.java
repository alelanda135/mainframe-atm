package bo.edu.ucb.sis213;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JOptionPane; // Agrega esta línea
import bo.edu.ucb.sis213.Menuu; // Agrega esta línea


public class ATMApp extends JFrame {

	private JPanel contentPane;
	private JTextField textField_1;
	private JPasswordField passwordField;
    private int intentos = 3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ATMApp frame = new ATMApp();
					frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ATMApp() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 404, 292);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("PIN:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(112, 128, 49, 27);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("USUARIO:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1.setBounds(78, 82, 94, 27);
		contentPane.add(lblNewLabel_1);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(167, 82, 128, 27);
		contentPane.add(textField_1);
		
		JButton btnNewButton = new JButton("Iniciar sesi\u00F3n");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                String alias = textField_1.getText();
                char[] passwordChars = passwordField.getPassword();
                String pin = new String(passwordChars);
                int pinIngresado = Integer.parseInt(pin);

                Connection connection = null;
                try {
                    connection = conexionBDD.getConnection(); // Reemplaza esto con tu conexión real
                    if (validarPIN(connection, alias, pinIngresado) != -1) {
                        pinActual = pinIngresado;
                        JOptionPane.showMessageDialog(ATMApp.this, "PIN Correcto. /nBienvenido al sistema!");

                        Menuu menu = new Menuu(connection, usuarioId); // Pasa la conexión y el usuarioId
                        menu.setVisible(true);
                        dispose(); 

                        break;
                    } else {
                        intentos--;
                        if (intentos > 0) {
                            JOptionPane.showMessageDialog(ATMApp.this,"PIN incorrecto. Le quedan " + intentos + " intentos.");
                        } else {
                            JOptionPane.showMessageDialog(ATMApp.this,"PIN incorrecto. Ha excedido el número de intentos.");
                            System.exit(0);
                        }
                    }

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(ATMApp.this,"No se puede conectar a Base de Datos");
                    ex.printStackTrace();
                    System.exit(1);
                }
                
        

			}
		});
		btnNewButton.setBounds(147, 193, 94, 27);
		contentPane.add(btnNewButton);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(167, 130, 128, 27);
		contentPane.add(passwordField);
	}

    public static int obtenerIdPorNumeroCuenta(Connection connection, String numeroCuenta) {
        String query = "SELECT usuario_id FROM cuentas WHERE numero_cuenta = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, numeroCuenta);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                return resultSet.getInt("usuario_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Si no se puede obtener el ID, devuelve -1
    }
    
    public static String obtenerNumeroCuenta(Connection connection, int usuarioId) {
        String query = "SELECT numero_cuenta FROM cuentas WHERE usuario_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, usuarioId);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                return resultSet.getString("numero_cuenta");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Si no se puede obtener el número de cuenta, devuelve null
    }

    public static int validarPIN(Connection connection, String alias, int pin) {
        String query = "SELECT id FROM usuarios WHERE alias = ? AND pin = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, alias);
            preparedStatement.setInt(2, pin);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Valor por defecto si no se encuentra el usuario
    }
    
}
