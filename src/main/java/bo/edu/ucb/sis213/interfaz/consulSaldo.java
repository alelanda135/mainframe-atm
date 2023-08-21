package interfaz;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;

import bo.edu.ucb.sis213.funciones;

public class consulSaldo extends JPanel {
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;


    public consulSaldo(Connection connection, int usuarioId) {
        this.connection = connection;
        this.usuarioId = usuarioId;
    }

	/**
	 * Create the panel.
	 */
	public consulSaldo() {
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("CONSULTA SALDO");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel.setBounds(155, 42, 165, 14);
		add(lblNewLabel);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(222, 193, 129, 28);
		add(textField);
		
		JLabel lblNewLabel_1 = new JLabel("Saldo Disponible:");
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel_1.setBounds(71, 199, 138, 14);
		add(lblNewLabel_1);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(168, 86, 222, 28);
		add(textField_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Nombre:");
		lblNewLabel_1_1.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel_1_1.setBounds(88, 92, 70, 14);
		add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Cuenta:");
		lblNewLabel_1_1_1.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel_1_1_1.setBounds(139, 142, 70, 14);
		add(lblNewLabel_1_1_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(203, 136, 148, 28);
		add(textField_2);

        

        String[] datosUsuario = funciones.consultarSaldo(connection, usuarioId);
        String nomb = datosUsuario[0];
        String cuenta = datosUsuario[1];
        String monto = datosUsuario[2];

        textField_1.setText(nomb);
        textField_2.setText(cuenta);
        textField.setText(monto);

	}
}