package datos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.Conexion;
import entidad.Usuario;
import datos.Interfaces.CrudPaginadoInterface;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class UsuarioDAO implements CrudPaginadoInterface<Usuario>{
    private final Conexion con;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;

    public UsuarioDAO() {
        con=Conexion.getInstancia();
        
    }
    
    @Override
    public List<Usuario> listar(String texto,int totalPorPagina, int numPAgina) {
        List<Usuario> registros = new ArrayList();
        try {
            ps=con.conectar().prepareStatement("SELECT u.id, u.rol_id, r.nombre as rol_nombre, u.nombre, u.tipo_docum, u.nro_docum, u.direccion, u.telefono, u.email, u.clave, u.activo FROM usuario u INNER JOIN rol r ON u.rol_id=r.id WHERE u.nombre LIKE ? ORDER BY r.id ASC LIMIT ?,?");
            ps.setString(1, "%"+texto+"%");
            ps.setInt(2, (numPAgina-1)*totalPorPagina); //para empezar la paginación
            ps.setInt(3, totalPorPagina);
            rs=ps.executeQuery();
            while (rs.next()) {                
                registros.add(new Usuario(rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getBoolean(11)));
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
    
    public Usuario login(String email, String clave){
        Usuario us=null;
        try {ps=con.conectar().prepareStatement("SELECT u.id, u.rol_id, r.nombre as rol_nombre, u.nombre, u.tipo_docum, u.nro_docum, u.direccion, u.telefono, u.email, u.activo FROM usuario u INNER JOIN rol r ON u.rol_id=r.id WHERE u.email=? AND u.clave=?");
            ps.setString(1, email);
            ps.setString(2, clave); 
            rs=ps.executeQuery();
            
            if (rs.first()) {
                us= new Usuario(rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9), rs.getBoolean(10));
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
        return us;
    }

    @Override
    public boolean insertar(Usuario obj) {
        resp=false;
        try {
            ps=con.conectar().prepareStatement("INSERT INTO usuario (rol_id, nombre, tipo_docum, nro_docum,direccion,telefono,email,clave,activo) VALUES(?,?,?,?,?,?,?,?,1)");
            ps.setInt(1, obj.getRolId());
            ps.setString(2, obj.getNombre());
            ps.setString(3, obj.getTipodocumento());
            ps.setString(4, obj.getNumDocumento());
            ps.setString(5, obj.getDireccion());
            ps.setString(6, obj.getTelefono());
            ps.setString(7, obj.getEmail());
            ps.setString(8, obj.getClave());
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
    public boolean actualizar(Usuario obj) {
        resp=false;
        try {
            ps=con.conectar().prepareStatement("UPDATE usuario SET rol_id=?, nombre=?, tipo_docum=?, nro_docum=?, direccion=?, telefono=?, email=?, clave=? WHERE id=?");
            ps.setInt(1, obj.getRolId());
            ps.setString(2, obj.getNombre());
            ps.setString(3, obj.getTipodocumento());
            ps.setString(4, obj.getNumDocumento());
            ps.setString(5, obj.getDireccion());
            ps.setString(6, obj.getTelefono());
            ps.setString(7, obj.getEmail());
            ps.setString(8, obj.getClave());
            ps.setInt(9, obj.getId());
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
            ps=con.conectar().prepareStatement("UPDATE usuario SET activo=0 WHERE id=? ");
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
            ps=con.conectar().prepareStatement("UPDATE usuario SET activo=1 WHERE id=? ");
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
            ps=con.conectar().prepareStatement("SELECT COUNT(id) FROM usuario");
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
            ps=con.conectar().prepareStatement("SELECT email FROM usuario WHERE email=?");
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
