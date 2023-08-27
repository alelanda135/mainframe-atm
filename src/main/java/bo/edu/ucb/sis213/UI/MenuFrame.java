package bo.edu.ucb.sis213.UI;

import javax.swing.*;

import bo.edu.ucb.sis213.BL.LogicaDao;
import bo.edu.ucb.sis213.DAO.Historicos;
import bo.edu.ucb.sis213.DAO.Operaciones;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;


public class MenuFrame extends JFrame {
    private JButton consultarSaldoButton;
    private JButton realizarDepositoButton;
    private JButton realizarRetiroButton;
    private JButton realizarTransferenciaButton;
    private JButton cambiarPINButton;
    private JButton historialButton;
    private JButton historialTransferenciaButton;
    private JButton salirButton;



    public MenuFrame(Connection connection, int usuarioId) {

        setTitle("Menú Principal");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        

        JPanel panel = new JPanel(new GridLayout(7, 1, 10, 10));
        add(panel);

        JLabel welcomeLabel = new JLabel("BIENVENIDO");
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 20)); // Ajustar el tamaño y negrilla
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrado horizontal
        panel.add(welcomeLabel);
    
        panel.add(Box.createVerticalStrut(50));

        consultarSaldoButton = new JButton("Consultar Saldo");
        realizarDepositoButton = new JButton("Realizar Depósito");
        realizarRetiroButton = new JButton("Realizar Retiro");
        realizarTransferenciaButton = new JButton("Realizar Transferencia");
        cambiarPINButton = new JButton("Cambiar PIN");
        historialButton = new JButton("Historial Movimientos");
        historialTransferenciaButton = new JButton("Historial Transferencias");
        salirButton = new JButton("Salir");


        panel.add(consultarSaldoButton);
        panel.add(realizarDepositoButton);
        panel.add(realizarRetiroButton);
        panel.add(realizarTransferenciaButton);
        panel.add(historialButton);
        panel.add(historialTransferenciaButton);
        panel.add(cambiarPINButton);
        panel.add(salirButton);

        consultarSaldoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               double saldo = Operaciones.consultarSaldo(connection, usuarioId);
               JOptionPane.showMessageDialog(null, "Su saldo actual es: $" + saldo);
            }
        });

        realizarDepositoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LogicaDao.metodoDeposito(connection, usuarioId);
            }
        });

        realizarRetiroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LogicaDao.metodoRetiro(connection, usuarioId);
            }
        });

        realizarTransferenciaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LogicaDao.metodoTransferencia(connection, usuarioId);
            }
        });
        

        historialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Historicos.mostrarHistorialDepositosRetiros(connection, usuarioId);
            }
        });

        historialTransferenciaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Historicos.mostrarHistorialTransferencias(connection, usuarioId);

            }
        });

        cambiarPINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LogicaDao.metodoCambiarPIN(connection, usuarioId);
            }
        });


        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}


