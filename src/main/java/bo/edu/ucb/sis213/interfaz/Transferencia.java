package interfaz;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;

public class Transferencia extends JPanel {
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

    public Transferencia(Connection connection, int usuarioId) {
        this.connection = connection;
        this.usuarioId = usuarioId;
    }


	/**
	 * Create the panel.
	 */
	public Transferencia() {
		setLayout(null);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(190, 83, 121, 20);
		add(textField);
		
		JLabel lblNewLabel_1_1 = new JLabel("Monto a transferir:");
		lblNewLabel_1_1.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel_1_1.setBounds(44, 140, 184, 14);
		add(lblNewLabel_1_1);
		
		JLabel lblTransferencia = new JLabel("TRANSFERENCIA");
		lblTransferencia.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblTransferencia.setBounds(172, 33, 166, 14);
		add(lblTransferencia);
		
		JButton btnInresar = new JButton("Ingresar");
        btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                textField_2.setText(funciones.realizarTransferencia(connection, usuarioId, Integer.parseInt(textField.getText())), Double.parseDouble(textField_1.getText()));
			}
		});
		btnInresar.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnInresar.setBounds(190, 264, 97, 28);
		add(btnInresar);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(190, 134, 129, 28);
		add(textField_1);
		
		JLabel lblNewLabel_1 = new JLabel("Saldo Disponible:");
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel_1.setBounds(90, 215, 138, 14);
		add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Cuenta destino:");
		lblNewLabel_1_1_1.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel_1_1_1.setBounds(75, 85, 121, 14);
		add(lblNewLabel_1_1_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(226, 209, 129, 28);
		add(textField_2);

	}

}
