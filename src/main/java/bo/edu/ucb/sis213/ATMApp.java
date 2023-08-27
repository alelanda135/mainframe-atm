package bo.edu.ucb.sis213;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.SwingUtilities;

import bo.edu.ucb.sis213.DAO.ConexionBDD;
import bo.edu.ucb.sis213.UI.LoginFrame;

public class ATMApp {
    private static Connection connection;

    public static void main(String[] args) {

        try {
            connection = ConexionBDD.getConnection();
        } catch (SQLException ex) {
            System.err.println("No se puede conectar a la Base de Datos");
            ex.printStackTrace();
            System.exit(1);
        }

    SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
            LoginFrame loginFrame = new LoginFrame(connection);
            loginFrame.setVisible(true);
        }
    });
    
}

}

