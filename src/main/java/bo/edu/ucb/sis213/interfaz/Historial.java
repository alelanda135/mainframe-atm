package interfaz;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;

public class Historial extends JPanel {

    public Historial(Connection connection, int usuarioId) {
        this.connection = connection;
        this.usuarioId = usuarioId;
    }

	/**
	 * Create the panel.
	 */
	public Historial() {
		setLayout(null);
		
		JLabel lblHistoriaol = new JLabel("HISTORIAL");
		lblHistoriaol.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblHistoriaol.setBounds(188, 34, 97, 14);
		add(lblHistoriaol);
		
		JButton btnNewButton = new JButton("Volver");
        btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnNewButton.setBounds(176, 263, 97, 28);
		add(btnNewButton);

	}

}