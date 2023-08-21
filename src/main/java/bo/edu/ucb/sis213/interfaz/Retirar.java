package interfaz;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;

public class Retirar extends JPanel {
	private JTextField textField;
	private JTextField textField_1;


    public Retirar(Connection connection, int usuarioId) {
        this.connection = connection;
        this.usuarioId = usuarioId;
    }


	/**
	 * Create the panel.
     * 
     * 
	 */
	public Retirar() {
		setLayout(null);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(101, 107, 121, 28);
		add(textField);
		
		JLabel lblNewLabel_1_1 = new JLabel("MONTO A RETIRAR:");
		lblNewLabel_1_1.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel_1_1.setBounds(83, 82, 184, 14);
		add(lblNewLabel_1_1);
		
		JLabel lblRetirar = new JLabel("RETIRAR");
		lblRetirar.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblRetirar.setBounds(189, 32, 97, 14);
		add(lblRetirar);
		
		JButton btnInresar = new JButton("Ingresar");
		btnInresar.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnInresar.setBounds(258, 107, 97, 28);
		add(btnInresar);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(226, 180, 129, 28);
		add(textField_1);
		
		JLabel lblNewLabel_1 = new JLabel("Saldo Disponible:");
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel_1.setBounds(84, 186, 138, 14);
		add(lblNewLabel_1);
		
		JButton btnNewButton = new JButton("Ingresar");
        btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                textField_1.setText(funciones.realizarRetiro(connection, usuarioId, Double.parseDouble(textField.getText())));
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnNewButton.setBounds(189, 249, 97, 28);
		add(btnNewButton);

	}

}