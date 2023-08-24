package bo.edu.ucb.sis213;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class App {
    private static int usuarioId;
    private static int pinActual;
    private static String aliasActual;

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 3306;
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    private static final String DATABASE = "atm";

    public static Connection getConnection() throws SQLException {
        String jdbcUrl = String.format("jdbc:mysql://%s:%d/%s", HOST, PORT, DATABASE);
        try {
            // Asegúrate de tener el driver de MySQL agregado en tu proyecto
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL Driver not found.", e);
        }

        return DriverManager.getConnection(jdbcUrl, USER, PASSWORD);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int intentos = 3;

        System.out.println("Bienvenido al Cajero Automático.");

        Connection connection = null;
        try {
            connection = getConnection(); // Reemplaza esto con tu conexión real
        } catch (SQLException ex) {
            System.err.println("No se puede conectar a Base de Datos");
            ex.printStackTrace();
            System.exit(1);
        }


        while (intentos > 0) {
            System.out.print("Ingrese su alias: ");
            String aliasIngresado = scanner.next();
            System.out.print("Ingrese su PIN de 4 dígitos: ");
            int pinIngresado = scanner.nextInt();
            if (validarPIN(connection, pinIngresado, aliasIngresado)) {
                pinActual = pinIngresado;
                aliasActual = aliasIngresado;
                mostrarMenu(connection);
                break;
            } else {
                intentos--;
                if (intentos > 0) {
                    System.out.println("PIN incorrecto. Le quedan " + intentos + " intentos.");
                } else {
                    System.out.println("PIN incorrecto. Ha excedido el número de intentos.");
                    System.exit(0);
                }
            }
        }
    }

    public static boolean validarPIN(Connection connection, int pin, String alias) {
        String query = "SELECT id FROM usuarios WHERE alias = ? and pin = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, alias);
            preparedStatement.setInt(2, pin);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                usuarioId = resultSet.getInt("id");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void mostrarMenu(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nMenú Principal:");
            System.out.println("1. Consultar saldo.");
            System.out.println("2. Realizar un depósito.");
            System.out.println("3. Realizar un retiro.");
            System.out.println("4. Realizar transferencia.");
            System.out.println("5. Cambiar PIN.");
            System.out.println("6. Salir.");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                System.out.println("Su saldo actual es: " + consultarSaldo(connection));
                    break;
                case 2:
                    realizarDeposito(connection);
                    break;
                case 3:
                    realizarRetiro(connection);
                    break;
                case 4:
                    realizarTransferencia(connection);
                    break;
                case 5:
                    cambiarPIN(connection);
                    break;
                case 6:
                    System.out.println("Gracias por usar el cajero. ¡Hasta luego!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }

    public static double consultarSaldo(Connection connection) {
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

    public static void realizarDeposito(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese la cantidad a depositar: $");
        double cantidad = scanner.nextDouble();

        if (cantidad <= 0) {
            System.out.println("Cantidad no válida.");
            return;
        }

        double saldoActual = consultarSaldo(connection);
        double nuevoSaldo = saldoActual + cantidad;
        actualizarSaldo(connection, nuevoSaldo);

        registrarEnHistorico(connection, "deposito", cantidad);

        System.out.println("Depósito realizado con éxito. Su nuevo saldo es: $" + nuevoSaldo);
    }

    public static void realizarRetiro(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese la cantidad a retirar: $");
        double cantidad = scanner.nextDouble();

        if (cantidad <= 0) {
            System.out.println("Cantidad no válida.");
            return;
        }

        double saldoActual = consultarSaldo(connection);
        if (cantidad > saldoActual) {
            System.out.println("Saldo insuficiente.");
            return;
        }

        double nuevoSaldo = saldoActual - cantidad;
        actualizarSaldo(connection, nuevoSaldo);

        registrarEnHistorico(connection, "retiro", cantidad);

        System.out.println("Retiro realizado con éxito. Su nuevo saldo es: $" + nuevoSaldo);
    }

    public static void registrarEnHistorico(Connection connection, String accion, double cantidad) {
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


    public static void cambiarPIN(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese su PIN actual: ");
        int pinIngresado = scanner.nextInt();

        if (pinIngresado == pinActual) {
            System.out.print("Ingrese su nuevo PIN: ");
            int nuevoPin = scanner.nextInt();
            System.out.print("Confirme su nuevo PIN: ");
            int confirmacionPin = scanner.nextInt();

            if (nuevoPin == confirmacionPin) {
                String updateQuery = "UPDATE usuarios SET pin = ? WHERE id = ?";
                try {
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setInt(1, nuevoPin);
                    updateStatement.setInt(2, usuarioId);
                    updateStatement.executeUpdate();

                    pinActual = nuevoPin;
                    System.out.println("PIN actualizado con éxito.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Los PINs no coinciden.");
            }
        } else {
            System.out.println("PIN incorrecto.");
        }
    }

    public static void realizarTransferencia(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el número de cuenta de destino: ");
        String cuentaDestino = scanner.next();
        System.out.print("Ingrese la cantidad a transferir: $");
        double cantidad = scanner.nextDouble();

        double saldoActual = consultarSaldo(connection);

        if (cantidad <= 0) {
            System.out.println("Cantidad no válida.");
            return;
        } else if (cantidad > saldoActual) {
            System.out.println("Saldo insuficiente.");
            return;
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

            System.out.println("Transferencia realizada con éxito. Su nuevo saldo es: $" + (saldoActual - cantidad));
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
