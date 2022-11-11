package datos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.Conexion;
import entidad.Categoria;
import datos.Interfaces.CrudSimpleInterface;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class CategoriaDAO implements CrudSimpleInterface<Categoria>{
    private final Conexion con;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;

    public CategoriaDAO() {
        con=Conexion.getInstancia();
    }
    
    
    @Override
    public List<Categoria> listar(String texto) {
        List<Categoria> registros = new ArrayList();
        try {
            ps=con.conectar().prepareStatement("SELECT * FROM categoria WHERE nombre LIKE?");
            ps.setString(1, "%"+texto+"%");
            rs=ps.executeQuery();
            while (rs.next()) {                
                registros.add(new Categoria(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getBoolean(4)));
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
    
    public List<Categoria> seleccionar() {
        List<Categoria> registros = new ArrayList();
        try {
            ps=con.conectar().prepareStatement("SELECT id, nombre FROM categoria ORDER BY nombre ASC");
            rs=ps.executeQuery();
            while (rs.next()) {                
                registros.add(new Categoria(rs.getInt(1),rs.getString(2)));
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
    public boolean insertar(Categoria obj) {
        resp=false;
        try {
            ps=con.conectar().prepareStatement("INSERT INTO categoria (nombre, descripcion, activo) VALUES(?,?,1)");
            ps.setString(1, obj.getNombre());
            ps.setString(2, obj.getDescripcion());
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
    public boolean actualizar(Categoria obj) {
        resp=false;
        try {
            ps=con.conectar().prepareStatement("UPDATE categoria SET nombre=?,descripcion=? WHERE id=? ");
            ps.setString(1, obj.getNombre());
            ps.setString(2, obj.getDescripcion());
            ps.setInt(3, obj.getId());
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
    public boolean desactivar(int id) {
        resp=false;
        try {
            ps=con.conectar().prepareStatement("UPDATE categoria SET activo=0 WHERE id=? ");
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
    public boolean activar(int id) {
        resp=false;
        try {
            ps=con.conectar().prepareStatement("UPDATE categoria SET activo=1 WHERE id=? ");
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
            ps=con.conectar().prepareStatement("SELECT COUNT(id) FROM categoria");
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
    public boolean existe(String texto) {
        resp=false;
        try {
            ps=con.conectar().prepareStatement("SELECT nombre FROM categoria WHERE nombre=?");
            ps.setString(1, texto);
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
