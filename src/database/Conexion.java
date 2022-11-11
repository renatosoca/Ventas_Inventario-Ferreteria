package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import javax.swing.JOptionPane;


public class Conexion {
    //PARA 
    private final String DRIVER="com.mysql.jdbc.Driver";
    private final String URL="jdbc:mysql://localhost:3306/";
    private final String DB="bd_sistema";
    private final String USER="root";
    private final String PASS="";
    
    public Connection cadena;
    public static Conexion instancia; //APLICANDO EL PATRON SINGLETON
    
    private Conexion(){
        this.cadena=null;
    }
    
    public Connection conectar(){
        try {
            Class.forName(DRIVER);
            this.cadena=DriverManager.getConnection(URL+DB,USER,PASS);
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            System.exit(0);
        }
    return this.cadena;
    }
    
    public void desconectar(){
        try {
            this.cadena.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    //PARA NO TENER PROBLEMAS EN LA CONSISTENCIA DE DATOS, TENER MEJOR CONTROL EN EL TRAFICO.
    public synchronized static Conexion getInstancia(){
        if (instancia==null) {
            instancia=new Conexion();
        }
    return instancia;
    }
}
