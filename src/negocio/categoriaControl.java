package negocio;

import datos.CategoriaDAO;
import entidad.Categoria;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import java.util.List;


public class categoriaControl {
    private final CategoriaDAO datos;
    private Categoria obj;
    private DefaultTableModel modeloTabla;
    public int registrosMostrados;
    
    public categoriaControl(){
        this.datos=new CategoriaDAO();
        this.obj=new Categoria();
        this.registrosMostrados=0;
    }
    
    public DefaultTableModel listar(String texto){
        List<Categoria> lista = new ArrayList();
        lista.addAll(datos.listar(texto));
        
        String[] titulos = {"ID","Nombre","Descripción","Estado"};
        this.modeloTabla=new DefaultTableModel(null, titulos);
        
        String estado;
        String[] registro = new String[4];
        
        this.registrosMostrados=0;
        for (Categoria item : lista) {
            if (item.isActivo()) {
                estado="Activo";
            }else{
                estado="Inactivo";
            }
            
            registro[0]=Integer.toString(item.getId());
            registro[1]=item.getNombre();
            registro[2]=item.getDescripcion();
            registro[3]=estado;
            
            this.modeloTabla.addRow(registro);
            this.registrosMostrados++;
        }
        return this.modeloTabla;
    }
    
    public String insertar(String nombre, String descripcion){
        if (datos.existe(nombre)) {
            return "El registro ya existe!";
        } else {
            obj.setNombre(nombre);
            obj.setDescripcion(descripcion);
            if (datos.insertar(obj)) {
                return "OK";
            }else{
                return "Error en el registro";
            }
        }
    }
    
    public String actualizar(int id, String nombre, String nombreAnt, String descripcion){
        if (nombre.equals(nombreAnt)) {
            obj.setId(id);
            obj.setNombre(nombre);
            obj.setDescripcion(descripcion);
            if (datos.actualizar(obj)) {
                return "OK";
            }else{
                return "Error en la actualizacion";
            }
        }else{
            if (datos.existe(nombre)) {
                return "El registro ya existe";
            }else{
                obj.setId(id);
                obj.setNombre(nombre);
                obj.setDescripcion(descripcion);
                if (datos.actualizar(obj)) {
                    return "OK";
                }else{
                    return "Error en la actualizacion";
                }
            }
        }
    }
    
    public String desactivar(int id){
        if (datos.desactivar(id)) {
            return "OK";
        }else{
            return "Error al desactivar el registro";
        }
    }
    
    public String activar(int id){
        if (datos.activar(id)) {
            return "OK";
        }else{
            return "Error al activar el registro";
        }
    }
    
    public int total(){
        return datos.total();
    }
    
    public int totalMostrados(){
        return this.registrosMostrados;
    }
}
