package interfaz;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;

import bo.edu.ucb.sis213.funciones;

import javax.swing.JButton;

public class Depositar extends JPanel {
	private JTextField textField;
	private JTextField textField_1;


    public Depositar(Connection connection, int usuarioId) {
        this.connection = connection;
        this.usuarioId = usuarioId;
    }


	/**
	 * Create the panel.
	 */
	public Depositar() {
		setLayout(null);
		
		JLabel lblDepsito = new JLabel("DEP\u00D3SITO");
		lblDepsito.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblDepsito.setBounds(197, 39, 97, 14);
		add(lblDepsito);
		
		JLabel lblNewLabel_1_1 = new JLabel("MONTO A DEPOSITAR:");
		lblNewLabel_1_1.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel_1_1.setBounds(60, 123, 184, 14);
		add(lblNewLabel_1_1);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(242, 117, 121, 28);
		add(textField);
		
		JLabel lblNewLabel_1 = new JLabel("Saldo Disponible:");
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel_1.setBounds(92, 193, 138, 14);
		add(lblNewLabel_1);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(234, 187, 129, 28);
		add(textField_1);
		
		JButton btnNewButton = new JButton("Ingresar");
        btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                textField_1.setText(funciones.realizarDeposito(connection, usuarioId, Double.parseDouble(textField.getText())));
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnNewButton.setBounds(197, 256, 97, 28);
		add(btnNewButton);

	}

}