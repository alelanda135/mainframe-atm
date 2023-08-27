package bo.edu.ucb.sis213.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class Historicos {
        public static void mostrarHistorialDepositosRetiros(Connection connection, int usuarioId) {
    String query = "SELECT fecha, accion, cantidad FROM historico WHERE usuario_id = ? AND (accion = 'deposito' OR accion = 'retiro')";
    try {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, usuarioId);
        ResultSet resultSet = preparedStatement.executeQuery();

        StringBuilder history = new StringBuilder();
        history.append("Historial de Depósitos y Retiros:\n");
        history.append("Fecha\t\tAccion\tCantidad\n");
        while (resultSet.next()) {
            String fecha = resultSet.getString("fecha");
            String tipo = resultSet.getString("accion");
            double monto = resultSet.getDouble("cantidad");
            history.append(fecha).append("\t").append(tipo).append("\t$").append(monto).append("\n");
        }

        JOptionPane.showMessageDialog(null, new JTextArea(history.toString()));
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public static void mostrarHistorialTransferencias(Connection connection, int usuarioId) {
    String query = "SELECT fecha, receptor_cuenta, cantidad FROM transferencias WHERE emisor_id = ?";
    try {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, usuarioId);
        ResultSet resultSet = preparedStatement.executeQuery();

        StringBuilder history = new StringBuilder();
        history.append("Historial de Transferencias:\n");
        history.append("Fecha\t\tCuenta de Destino\tMonto\n");
        while (resultSet.next()) {
            String fecha = resultSet.getString("fecha");
            String receptorCuenta = resultSet.getString("receptor_cuenta");
            double cantidad = resultSet.getDouble("cantidad");
            history.append(fecha).append("\t").append(receptorCuenta).append("\t\t$").append(cantidad).append("\n");
        }

        JOptionPane.showMessageDialog(null, new JTextArea(history.toString()));
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
}
