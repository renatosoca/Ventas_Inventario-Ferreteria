/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;

import com.mysql.jdbc.Statement;
import java.sql.Connection;
import database.Conexion;
import datos.Interfaces.CrudIngresoInterface;
import entidad.DetalleIngreso;
import entidad.Ingreso;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author rena_
 */
public class IngresoDAO implements CrudIngresoInterface<Ingreso, DetalleIngreso>{

    private final Conexion con;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;

    public IngresoDAO() {
        con=Conexion.getInstancia();
    }
    
    
    @Override
    public List<Ingreso> listar(String texto, int totalPorPagina, int numPAgina) {
        List<Ingreso> registros = new ArrayList();
        try {
            ps=con.conectar().prepareStatement("SELECT i.id, i.usuario_id, u.nombre as usuario_nombre, i.persona_id, p.nombre as persona_nombre, i.tipo_comprobante, i.serie_comprobante, i.nro_comprobante, i.fecha, i.impuesto, i.total, i.estado FROM ingreso i INNER JOIN persona p ON i.persona_id=p.id INNER JOIN usuario u ON i.usuario_id=u.id WHERE i.nro_comprobante LIKE ? ORDER BY i.id ASC LIMIT ?,?");
            ps.setString(1, "%"+texto+"%");
            ps.setInt(2, (numPAgina-1)*totalPorPagina); //para empezar la paginación
            ps.setInt(3, totalPorPagina);
            rs=ps.executeQuery();
            while (rs.next()) {                
                registros.add(new Ingreso(rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getInt(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getDate(9),rs.getDouble(10), rs.getDouble(11), rs.getString(12)));
            }
            ps.close();
            rs.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally{
            //PARA LIBERAR MEMORIA
            ps=null;
            rs=null;
            con.desconectar();
        }
        return registros;
    }

    @Override
    public List<DetalleIngreso> listarDetalle(int id) {
        List<DetalleIngreso> registros = new ArrayList();
        try {
            ps=con.conectar().prepareStatement("SELECT a.id ,a.codigo, a.nombre, di.cantidad, di.precio, (di.cantidad*precio) as sub_total FROM detalle_ingreso di INNER JOIN articulo a ON di.articulo_id=a.id WHERE di.ingreso_id=?");
            ps.setInt(1, id);
            rs=ps.executeQuery();
            while (rs.next()) {                
                registros.add(new DetalleIngreso(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4),rs.getDouble(5),rs.getDouble(6)));
            }
            ps.close();
            rs.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally{
            //PARA LIBERAR MEMORIA
            ps=null;
            rs=null;
            con.desconectar();
        }
        return registros;
    }

    @Override
    public boolean insertar(Ingreso obj) {
        resp=false;
        Connection conn=null;
        
        try { //Usaremos Transacciones
            conn=con.conectar();
            conn.setAutoCommit(false); //Para iniciar una trasaccion necesitamos desactivar el autocommit
            String sqlInsertIngreso="INSERT INTO ingreso (persona_id, usuario_id,tipo_comprobante,serie_comprobante,nro_comprobante,fecha,impuesto,total, estado) VALUES(?,?,?,?,?,now(),?,?,?)";
            
            ps=conn.prepareStatement(sqlInsertIngreso,Statement.RETURN_GENERATED_KEYS); //El statement es para obtener el id generado en la base de datos
            ps.setInt(1, obj.getPersonaID());
            ps.setInt(2, obj.getUsuarioID());
            ps.setString(3, obj.getTipoComprobante());
            ps.setString(4, obj.getSerieComprobante());
            ps.setString(5, obj.getNroComprobante());
            ps.setDouble(6, obj.getImpuesto());
            ps.setDouble(7, obj.getTotal());
            ps.setString(8, "Aceptado");
            
            int filasAfectadas = ps.executeUpdate(); //si se ejecuta, el valor de la variable seria 1
            rs=ps.getGeneratedKeys();
            int idGenerado=0;
            if (rs.next()) {
                idGenerado=rs.getInt(1);
            }
            
            if (filasAfectadas==1) {
                String sqlInsertDetalle="INSERT INTO detalle_ingreso (ingreso_id, articulo_id, cantidad, precio) VALUES (?,?,?,?)";
                ps=conn.prepareStatement(sqlInsertDetalle);
                for (DetalleIngreso item: obj.getDetalles()) {
                    ps.setInt(1, idGenerado);
                    ps.setInt(2, item.getArticuloID());
                    ps.setInt(3, item.getCantidad());
                    ps.setDouble(4, item.getPrecio());
                    resp=ps.executeUpdate()>0;  // para comprobar si se a insertado los detalles de ingreso, pasa a "TRUE"
                }
                conn.commit(); //
            }else{
                conn.rollback(); // como vamos a insertar los detalles si no hemos ingresado una cabezera, para regresar todas las peticiones y no verse afectado la base de datos
            }
            
        } catch (SQLException e) {
            try {
                
                if (conn!=null) {
                conn.rollback();
                }
                
                JOptionPane.showMessageDialog(null, e.getMessage());
                
            } catch (SQLException ex) {
                Logger.getLogger(IngresoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }finally{
            try {
                //PARA LIBERAR MEMORIA
                if (rs!=null) rs.close();
                if (ps!=null) ps.close();
                if (conn!=null) conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(IngresoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return resp;
    }
    
    /*
    PARA ACTUALIZAR EL STOCK DEL ARTICULO
    CODIGO EN EL MySQL:
        DELIMITER //
        CREATE TRIGGER tr_updStockIngreso AFTER INSERT ON detalle_ingreso
        FOR EACH ROW BEGIN
        UPDATE articulo SET stock = stock + NEW.cantidad
        WHERE articulo.id = NEW.articulo_id;
        END
        //
        DELIMITER ;
    
    */

    @Override
    public boolean anular(int id) {
        resp=false;
        try {
            ps=con.conectar().prepareStatement("UPDATE ingreso SET estado='Anulado' WHERE id=? ");
            ps.setInt(1, id);
            if (ps.executeUpdate()>0) {
                resp=true;
            }
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }finally{
            //PARA LIBERAR MEMORIA
            ps=null;
            con.desconectar();
        }
        return resp;
    }

    @Override
    public int total() {
        int totalregistros=0;
        try {
            ps=con.conectar().prepareStatement("SELECT COUNT(id) FROM ingreso");
            rs=ps.executeQuery();
            while (rs.next()) {                
                totalregistros=rs.getInt("COUNT(id)");
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }finally{
            //PARA LIBERAR MEMORIA
            ps=null;
            rs=null;
            con.desconectar();
        }
        return totalregistros;
    }

    @Override
    public boolean existe(String texto1 , String texto2) {
        resp=false;
        try {
            ps=con.conectar().prepareStatement("SELECT id FROM ingreso WHERE serie_comprobante=? AND nro_comprobante=?");
            ps.setString(1, texto1);
            ps.setString(2, texto2);
            rs=ps.executeQuery();
            rs.last();
            
            if (rs.getRow()>0) {
                resp=true;
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }finally{
            //PARA LIBERAR MEMORIA
            ps=null;
            rs=null;
            con.desconectar();
        }
        return resp;
    }
    
}
