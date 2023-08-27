package bo.edu.ucb.sis213.BL;

import java.sql.Connection;
import javax.swing.JOptionPane;

import bo.edu.ucb.sis213.DAO.Operaciones;

public class LogicaDao {

    public static void metodoDeposito(Connection connection, int usuarioId) {

        String montocad = JOptionPane.showInputDialog(null, "Ingrese el monto a depositar:");
                if (montocad.matches("^\\d*\\.?\\d+$")) {
                    double cantidad = Double.parseDouble(montocad);
                    if(cantidad > 0){
                                Operaciones.realizarDeposito(connection,usuarioId,cantidad);
                    }else{
                        JOptionPane.showMessageDialog(null, "El monto debe ser mayor que cero.");
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Ingrese un monto válido.");
        }
    }

    public static void metodoRetiro(Connection connection, int usuarioId) {

        String montocad = JOptionPane.showInputDialog(null, "Ingrese el monto a depositar:");
                if (montocad.matches("^\\d*\\.?\\d+$")) {
                    double cantidad = Double.parseDouble(montocad);
                    if(cantidad > 0){
                        double saldoActual = Operaciones.consultarSaldo(connection, usuarioId);
                        if (cantidad > saldoActual) {
                            JOptionPane.showMessageDialog(null,"Saldo insuficiente.");
                            return;
                        }

                            Operaciones.realizarRetiro(connection, usuarioId, cantidad, saldoActual);
                    }else{
                        JOptionPane.showMessageDialog(null, "El monto debe ser mayor que cero.");
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Ingrese un monto válido.");
        }
    }


    public static void metodoTransferencia(Connection connection, int usuarioId) {
        String cuentaDestino = JOptionPane.showInputDialog(null, "Ingrese el número de cuenta de destino:");
        if (cuentaDestino != null) {
            // Verificar si la cuenta de destino existe en el sistema
            int receptorId = Operaciones.obtenerIdPorNumeroCuenta(connection, cuentaDestino);
            if (receptorId != -1) {
                String montoStr = JOptionPane.showInputDialog(null, "Ingrese el monto a transferir:");
                if (montoStr != null && montoStr.matches("^\\d*\\.?\\d+$")) {
                    double monto = Double.parseDouble(montoStr);
                    if (monto > 0) {
                        double saldoActual = Operaciones.consultarSaldo(connection, usuarioId);
                        if (monto <= saldoActual) {
                                Operaciones.realizarTransferencia(connection, usuarioId, monto, cuentaDestino, saldoActual);
                        } else {
                            JOptionPane.showMessageDialog(null, "Saldo insuficiente para realizar la transferencia.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "El monto debe ser mayor que cero.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Ingrese un monto válido.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "La cuenta de destino no existe en el sistema.");
            }
        }
    }

    public static void metodoCambiarPIN(Connection connection, int usuarioId) {
        String pinIngresadoStr = JOptionPane.showInputDialog(null, "Ingrese su PIN actual:");
        if (pinIngresadoStr != null && pinIngresadoStr.matches("\\d+")) {
            int pinIngresado = Integer.parseInt(pinIngresadoStr);
    
            if (Operaciones.validarPIN(connection, pinIngresado, usuarioId)) {
                String nuevoPinStr = JOptionPane.showInputDialog(null, "Ingrese su nuevo PIN:");
                if (nuevoPinStr != null && nuevoPinStr.matches("\\d+")) {
                    int nuevoPin = Integer.parseInt(nuevoPinStr);
    
                    String confirmacionPinStr = JOptionPane.showInputDialog(null, "Confirme su nuevo PIN:");
                    if (confirmacionPinStr != null && confirmacionPinStr.matches("\\d+")) {
                        int confirmacionPin = Integer.parseInt(confirmacionPinStr);
                        Operaciones.cambiarPIN(connection, usuarioId, nuevoPin, confirmacionPin);
                    } else {
                        JOptionPane.showMessageDialog(null, "Ingrese una confirmación de PIN válida.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Ingrese un nuevo PIN válido.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "PIN incorrecto.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Ingrese un PIN actual válido.");
        }
    }






    
}
