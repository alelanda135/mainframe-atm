package bo.edu.ucb.sis213.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;


public class Operaciones {

    public static void realizarDeposito(Connection connection, int usuarioId, double cantidad) {
            double saldoActual = consultarSaldo(connection, usuarioId);
            double nuevoSaldo = saldoActual + cantidad;
            actualizarSaldo(connection, nuevoSaldo, usuarioId);
            registrarEnHistorico(connection, "deposito", cantidad, usuarioId);
            JOptionPane.showMessageDialog(null,"Depósito realizado con éxito. Su nuevo saldo es: $" + nuevoSaldo);

    }

    public static void realizarRetiro(Connection connection, int usuarioId, double cantidad, double saldoActual) {
                        double nuevoSaldo = saldoActual - cantidad;
                        actualizarSaldo(connection, nuevoSaldo, usuarioId);
                        registrarEnHistorico(connection, "retiro", cantidad, usuarioId);
                        JOptionPane.showMessageDialog(null,"Retiro realizado con éxito. Su nuevo saldo es: $" + nuevoSaldo);
    }
  
    public static void cambiarPIN(Connection connection, int usuarioId, int nuevoPin, int confirmacionPin) {
        if (nuevoPin == confirmacionPin) {
            String updateQuery = "UPDATE usuarios SET pin = ? WHERE id = ?";
            try {
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setInt(1, nuevoPin);
                updateStatement.setInt(2, usuarioId);
                updateStatement.executeUpdate();

                JOptionPane.showMessageDialog(null,"PIN actualizado con éxito.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Los PINs no coinciden.");
        }
    }

        public static double consultarSaldo(Connection connection, int usuarioId) {
        String query = "SELECT saldo FROM cuentas WHERE usuario_id = ?";
    try {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, usuarioId);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            
            return resultSet.getDouble("saldo");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return 0.0; // Si no se puede obtener el saldo, devuelve 0.0

    }

    public static void actualizarSaldo(Connection connection, double nuevoSaldo, int usuarioId) {
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

    
    
    public static void registrarEnHistorico(Connection connection, String accion, double cantidad, int usuarioId) {
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

    public static void realizarTransferencia(Connection connection, int usuarioId, double cantidad, String cuentaDestino, double saldoActual) {

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

            JOptionPane.showMessageDialog(null,"Transferencia realizada con éxito. Su nuevo saldo es: $" + (saldoActual - cantidad));
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
        return -1; 
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

    

    public static boolean validarPIN(Connection connection, int pin, int usuarioId) {
        String query = "SELECT id FROM usuarios WHERE id = ? AND pin = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, usuarioId);
            preparedStatement.setInt(2, pin);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int validarUsuario(Connection connection, int pin, String alias) {
        String query = "SELECT id FROM usuarios WHERE alias = ? AND pin = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, alias);
            preparedStatement.setInt(2, pin);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                return resultSet.getInt("id"); // Retorna la ID del usuario si las credenciales son válidas
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Retorna -1 si las credenciales no son válidas
    }
    
}
