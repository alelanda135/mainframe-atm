package bo.edu.ucb.sis213;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class Menuu extends JFrame {

	private JPanel contentPane;
	private JPanel panelCont;
    private Connection connection; // Agrega esta variable
    private int usuarioId; // Agrega esta variable

    public Menuu(Connection connection, int usuarioId) {
        this.connection = connection;
        this.usuarioId = usuarioId;
        // Resto del constructor

        MenuPaneles();
    }

	/**
	 * Create the frame.
	 */
	public MenuPaneles() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 688, 390);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.controlShadow);
		panel.setBounds(10, 11, 163, 334);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JButton btnNewButton = new JButton("Consultar Saldo");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				consulSaldo met1 = new consulSaldo(connection, usuarioId);
				nuevoPanel(met1);
				
			}
		});
		btnNewButton.setBounds(10, 49, 143, 23);
		panel.add(btnNewButton);
		btnNewButton.setFont(new Font("Arial", Font.BOLD, 12));
		
		JButton btnDepositar = new JButton("Depositar");
		btnDepositar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Depositar met2 = new Depositar(connection, usuarioId);
				nuevoPanel(met2);
				
			}
		});
		
		btnDepositar.setFont(new Font("Arial", Font.BOLD, 12));
		btnDepositar.setBounds(10, 93, 143, 23);
		panel.add(btnDepositar);
		
		JButton btnNewButton_1_1 = new JButton("Retirar");
		btnNewButton_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Retirar met3 = new Retirar(connection, usuarioId);
				nuevoPanel(met3);
				
			}
		});
		btnNewButton_1_1.setFont(new Font("Arial", Font.BOLD, 12));
		btnNewButton_1_1.setBounds(10, 137, 143, 23);
		panel.add(btnNewButton_1_1);
		
		JButton btnNewButton_1_1_1 = new JButton("Transferencia");
		btnNewButton_1_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Transferencia met4 = new Transferencia(connection, usuarioId);
				nuevoPanel(met4);
			}
		});
		btnNewButton_1_1_1.setFont(new Font("Arial", Font.BOLD, 12));
		btnNewButton_1_1_1.setBounds(10, 179, 143, 23);
		panel.add(btnNewButton_1_1_1);
		
		JButton btnNewButton_1_1_2 = new JButton("Historial");
		btnNewButton_1_1_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			Historial met5 = new Historial(connection, usuarioId);
				nuevoPanel(met5);
			}
		});
		btnNewButton_1_1_2.setFont(new Font("Arial", Font.BOLD, 12));
		btnNewButton_1_1_2.setBounds(10, 224, 143, 23);
		panel.add(btnNewButton_1_1_2);
		
		JButton btnNewButton_1_1_2_1 = new JButton("Cambiar PIN");
		btnNewButton_1_1_2_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cambiarPIN met6 = new cambiarPIN(connection, usuarioId);
				nuevoPanel(met6);
			}
		});
		btnNewButton_1_1_2_1.setFont(new Font("Arial", Font.BOLD, 12));
		btnNewButton_1_1_2_1.setBounds(10, 268, 143, 23);
		panel.add(btnNewButton_1_1_2_1);
		
		JLabel lblNewLabel = new JLabel("MEN\u00DA");
		lblNewLabel.setBounds(59, 24, 46, 14);
		panel.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		
		panelCont = new JPanel();
		panelCont.setBounds(183, 11, 480, 329);
		contentPane.add(panelCont);
		panelCont.setLayout(new CardLayout(0, 0));

	}
	
	public void nuevoPanel(JPanel panelActual) {
		panelCont.removeAll();
		panelCont.add(panelActual);
		panelCont.repaint();
		panelCont.revalidate();
	}
}
