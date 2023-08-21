package interfaz;

import javax.swing.JPanel;
import javax.swing.JTextField;

import bo.edu.ucb.sis213.funciones;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;

public class Retirar extends JPanel {
	private JTextField textField;
	private JTextField textField_1;
    private JTextField textField_2;

    public cambiarPIN(Connection connection, int usuarioId) {
        this.connection = connection;
        this.usuarioId = usuarioId;
    }

	/**
	 * Create the panel.
	 */
	public cambiarPIN() {
		setLayout(null);
		
		JLabel lblCambiarPin = new JLabel("CAMBIAR PIN");
		lblCambiarPin.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblCambiarPin.setBounds(178, 38, 121, 14);
		add(lblCambiarPin);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("PIN ACTUAL:");
		lblNewLabel_1_1_1.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel_1_1_1.setBounds(100, 86, 97, 14);
		add(lblNewLabel_1_1_1);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(207, 84, 121, 20);
		add(textField);
		
		JButton btnInresar = new JButton("Cambiar");
        btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                funciones.cambiarPIN(connection, usuario_id, Integer.parseInt(textField.getText()), 
                Integer.parseInt(textField_1.getText()), Integer.parseInt(textField_2.getText()));
			}
		});
		btnInresar.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnInresar.setBounds(202, 247, 97, 28);
		add(btnInresar);
		
		JLabel lblNewLabel_1_1_1_1 = new JLabel("NUEVO PIN:");
		lblNewLabel_1_1_1_1.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel_1_1_1_1.setBounds(100, 139, 97, 14);
		add(lblNewLabel_1_1_1_1);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(207, 137, 121, 20);
		add(textField_1);
		
		JLabel lblNewLabel_1_1_1_2 = new JLabel("CONFIRME SU PIN:");
		lblNewLabel_1_1_1_2.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel_1_1_1_2.setBounds(68, 180, 130, 14);
		add(lblNewLabel_1_1_1_2);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(207, 178, 121, 20);
		add(textField_2);

	}

}



