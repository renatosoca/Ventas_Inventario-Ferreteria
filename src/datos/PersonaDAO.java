package datos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.Conexion;
import entidad.Persona;
import datos.Interfaces.CrudPaginadoInterface;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class PersonaDAO implements CrudPaginadoInterface<Persona>{
    private final Conexion con;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;

    public PersonaDAO() {
        con=Conexion.getInstancia();
        
    }
    
    @Override
    public List<Persona> listar(String texto,int totalPorPagina, int numPAgina) {
        List<Persona> registros = new ArrayList();
        try {
            ps=con.conectar().prepareStatement("SELECT p.id, p.tipo_persona, p.nombre, p.tipo_docum, p.nro_docum, p.direccion, p.telefono, p.email, p.activo FROM persona p WHERE p.nombre LIKE ? ORDER BY p.id ASC LIMIT ?,?");
            ps.setString(1, "%"+texto+"%");
            ps.setInt(2, (numPAgina-1)*totalPorPagina); //para empezar la paginación
            ps.setInt(3, totalPorPagina);
            rs=ps.executeQuery();
            while (rs.next()) {                
                registros.add(new Persona(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getBoolean(9)));
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
    
    public List<Persona> listarTipo(String texto,int totalPorPagina, int numPAgina, String tipoPersona) {
        List<Persona> registros = new ArrayList();
        try {
            ps=con.conectar().prepareStatement("SELECT p.id, p.tipo_persona, p.nombre, p.tipo_docum, p.nro_docum, p.direccion, p.telefono, p.email, p.activo FROM persona p WHERE p.nombre LIKE ? AND tipo_persona=? ORDER BY p.id ASC LIMIT ?,?");
            ps.setString(1, "%"+texto+"%");
            ps.setString(2, tipoPersona);
            ps.setInt(3, (numPAgina-1)*totalPorPagina); //para empezar la paginación
            ps.setInt(4, totalPorPagina);
            rs=ps.executeQuery();
            while (rs.next()) {                
                registros.add(new Persona(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getBoolean(9)));
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
    public boolean insertar(Persona obj) {
        resp=false;
        try {
            ps=con.conectar().prepareStatement("INSERT INTO persona (tipo_persona, nombre, tipo_docum, nro_docum,direccion,telefono,email,activo) VALUES(?,?,?,?,?,?,?,1)");
            ps.setString(1, obj.getTipoPersona());
            ps.setString(2, obj.getNombre());
            ps.setString(3, obj.getTipodocumento());
            ps.setString(4, obj.getNumDocumento());
            ps.setString(5, obj.getDireccion());
            ps.setString(6, obj.getTelefono());
            ps.setString(7, obj.getEmail());
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
    public boolean actualizar(Persona obj) {
        resp=false;
        try {
            ps=con.conectar().prepareStatement("UPDATE persona SET tipo_persona=?, nombre=?, tipo_docum=?, nro_docum=?, direccion=?, telefono=?, email=? WHERE id=?");
            ps.setString(1, obj.getTipoPersona());
            ps.setString(2, obj.getNombre());
            ps.setString(3, obj.getTipodocumento());
            ps.setString(4, obj.getNumDocumento());
            ps.setString(5, obj.getDireccion());
            ps.setString(6, obj.getTelefono());
            ps.setString(7, obj.getEmail());
            ps.setInt(8, obj.getId());
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
            ps=con.conectar().prepareStatement("UPDATE persona SET activo=0 WHERE id=? ");
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
            ps=con.conectar().prepareStatement("UPDATE persona SET activo=1 WHERE id=? ");
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
            ps=con.conectar().prepareStatement("SELECT COUNT(id) FROM persona");
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
            ps=con.conectar().prepareStatement("SELECT nombre FROM persona WHERE nombre=?");
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
