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


public class funciones{

    public static String[] consultarSaldo(Connection connection, int usuarioId) {
        String[] datos = new String[3];
        String query = "SELECT nombre, numero_cuenta, saldo FROM cuentas WHERE usuario_id = ?";
    try {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, usuarioId);
        ResultSet resultSet = preparedStatement.executeQuery();
    
        if (resultSet.next()) {
            datos[0] = resultSet.getDouble("nombre");
            datos[1] = resultSet.getDouble("numero_cuenta");
            datos[2] = resultSet.getDouble("saldo");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return datos;
    
    }
    
    public static void actualizarSaldo(Connection connection, double nuevoSaldo) {
        String query = "UPDATE cuentas SET saldo = ? WHERE usuario_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, nuevoSaldo);
            preparedStatement.setInt(2, usuarioId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static double realizarDeposito(Connection connection, int usuarioId, double cantidad) {
    
        double saldoActual = consultarSaldo(connection, usuarioId);
        double nuevoSaldo = saldoActual + cantidad;
        actualizarSaldo(connection, nuevoSaldo);
    
        registrarEnHistorico(connection, "deposito", cantidad);
    
        return nuevoSaldo;
    }
    
    public static double realizarRetiro(Connection connection, int usuarioId) {
  
        double saldoActual = consultarSaldo(connection, usuarioId);
        if (cantidad > saldoActual) {
            JOptionPane.showMessageDialog("Saldo insuficiente.");
            return 0;
        }
    
        double nuevoSaldo = saldoActual - cantidad;
        actualizarSaldo(connection, nuevoSaldo);
    
        registrarEnHistorico(connection, "retiro", cantidad);
    
        return nuevoSaldo;
    }
    
    public static void registrarEnHistorico(Connection connection, String accion, double cantidad , int usuarioId) {
        String query = "INSERT INTO historico (usuario_id, numero_cuenta, accion, cantidad) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, usuarioId);
            preparedStatement.setString(2, obtenerNumeroCuenta(connection, usuarioId));
            preparedStatement.setString(3, accion);
            preparedStatement.setDouble(4, cantidad);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int PinCorrecto(Connection connection, int id) {
        String query = "SELECT pin FROM usuarios WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
        
            if (resultSet.next()) {
                return resultSet.getInt("pin");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    
    public static void cambiarPIN(Connection connection, int usuarioId, int pinIngresado, int nuevoPin, int confirmacionPin) {

    
        if (pinIngresado == PinCorrecto(connection, usuarioId)) {
    
            if (nuevoPin == confirmacionPin) {
                String updateQuery = "UPDATE usuarios SET pin = ? WHERE id = ?";
                try {
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setInt(1, nuevoPin);
                    updateStatement.setInt(2, usuarioId);
                    updateStatement.executeUpdate();
    
                    pinActual = nuevoPin;
                    JOptionPane.showMessageDialog("PIN actualizado con éxito.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog("Los PINs no coinciden.");
            }
        } else {
            JOptionPane.showMessageDialog("PIN incorrecto.");
        }
    }
    
    public static double realizarTransferencia(Connection connection, int usuarioId, int cuentaDestino, double cantidad) {
    
        double saldoActual = consultarSaldo(connection);
    
        if (cantidad > saldoActual) {
            JOptionPane.showMessageDialog("Saldo insuficiente.");
            return 0;
        }
    
        String insertTransferenciaQuery = "INSERT INTO transferencias (emisor_id, receptor_id, emisor_cuenta, receptor_cuenta, cantidad) VALUES (?, ?, ?, ?, ?)";
        String updateSaldoEmisorQuery = "UPDATE cuentas SET saldo = saldo - ? WHERE usuario_id = ?";
        String updateSaldoReceptorQuery = "UPDATE cuentas SET saldo = saldo + ? WHERE numero_cuenta = ?";
        
        try {
            // Realizar transferencia
            connection.setAutoCommit(false);
            
            PreparedStatement insertTransferenciaStatement = connection.prepareStatement(insertTransferenciaQuery);
            insertTransferenciaStatement.setInt(1, usuarioId);
            insertTransferenciaStatement.setInt(2, obtenerIdPorNumeroCuenta(connection, cuentaDestino));
            insertTransferenciaStatement.setString(3, obtenerNumeroCuenta(connection, usuarioId));
            insertTransferenciaStatement.setString(4, cuentaDestino);
            insertTransferenciaStatement.setDouble(5, cantidad);
            insertTransferenciaStatement.executeUpdate();
    
            PreparedStatement updateSaldoEmisorStatement = connection.prepareStatement(updateSaldoEmisorQuery);
            updateSaldoEmisorStatement.setDouble(1, cantidad);
            updateSaldoEmisorStatement.setInt(2, usuarioId);
            updateSaldoEmisorStatement.executeUpdate();
    
            PreparedStatement updateSaldoReceptorStatement = connection.prepareStatement(updateSaldoReceptorQuery);
            updateSaldoReceptorStatement.setDouble(1, cantidad);
            updateSaldoReceptorStatement.setString(2, cuentaDestino);
            updateSaldoReceptorStatement.executeUpdate();
    
            connection.commit();
            connection.setAutoCommit(true);
    
            return saldoActual - cantidad;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static int obtenerIdPorNumeroCuenta(Connection connection, String numeroCuenta, int usuarioId) {
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
    
    }
    

