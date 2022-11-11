package negocio;

import datos.PersonaDAO;
import entidad.Persona;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import java.util.List;


public class PersonaControl {
    private final PersonaDAO datos;
    private Persona obj;
    private DefaultTableModel modeloTabla;
    public int registrosMostrados;
    
    public PersonaControl(){
        this.datos=new PersonaDAO();
        this.obj=new Persona();
        this.registrosMostrados=0;
    }
    
    public DefaultTableModel listar(String texto, int totalPorPagina, int nroPagina){
        List<Persona> lista = new ArrayList();
        lista.addAll(datos.listar(texto,totalPorPagina,nroPagina));
        
        String[] titulos = {"ID","Tipo Persona","Persona","Documento","# Documento","Dirección","Telefono","Email","Estado"};
        this.modeloTabla=new DefaultTableModel(null, titulos);
        
        String estado;
        String[] registro = new String[9];
        
        this.registrosMostrados=0;
        for (Persona item: lista) {
            if (item.isActivo()) {
                estado="Activo";
            }else{
                estado="Inactivo";
            }
            
            registro[0]=Integer.toString(item.getId());
            registro[1]=item.getTipoPersona();
            registro[2]=item.getNombre();
            registro[3]=item.getTipodocumento();
            registro[4]=item.getNumDocumento();
            registro[5]=item.getDireccion();
            registro[6]=item.getTelefono();
            registro[7]=item.getEmail();
            registro[8]=estado;
            
            this.modeloTabla.addRow(registro);
            this.registrosMostrados++;
        }
        return this.modeloTabla;
    }
    
    public DefaultTableModel listarTipo(String texto, int totalPorPagina, int nroPagina, String tipoPersona){
        List<Persona> lista = new ArrayList();
        lista.addAll(datos.listarTipo(texto,totalPorPagina,nroPagina,tipoPersona));
        
        String[] titulos = {"ID","Tipo Persona","Persona","Documento","# Documento","Dirección","Telefono","Email","Estado"};
        this.modeloTabla=new DefaultTableModel(null, titulos);
        
        String estado;
        String[] registro = new String[9];
        
        this.registrosMostrados=0;
        for (Persona item: lista) {
            if (item.isActivo()) {
                estado="Activo";
            }else{
                estado="Inactivo";
            }
            
            registro[0]=Integer.toString(item.getId());
            registro[1]=item.getTipoPersona();
            registro[2]=item.getNombre();
            registro[3]=item.getTipodocumento();
            registro[4]=item.getNumDocumento();
            registro[5]=item.getDireccion();
            registro[6]=item.getTelefono();
            registro[7]=item.getEmail();
            registro[8]=estado;
            
            this.modeloTabla.addRow(registro);
            this.registrosMostrados++;
        }
        return this.modeloTabla;
    }
    
    public String insertar(String TipoPersona,String nombre, String tipoDocumento, String numDocumento, String direccion, String Telefono, String email){
        if (datos.existe(nombre)) {
            return "El registro ya existe!";
        } else {
            obj.setTipoPersona(TipoPersona);
            obj.setNombre(nombre);
            obj.setTipodocumento(tipoDocumento);
            obj.setNumDocumento(numDocumento);
            obj.setDireccion(direccion);
            obj.setTelefono(Telefono);
            obj.setEmail(email);
            if (datos.insertar(obj)) {
                return "OK";
            }else{
                return "Error en el registro";
            }
        }
    }
    
    public String actualizar(int id, String TipoPersona,String nombre, String nombreAnterior, String tipoDocumento, String numDocumento, String direccion, String Telefono, String email){
        if (nombre.equals(nombreAnterior)) {  // si el email es igual al email anterior
            obj.setId(id);
            obj.setTipoPersona(TipoPersona);
            obj.setNombre(nombre);
            obj.setTipodocumento(tipoDocumento);
            obj.setNumDocumento(numDocumento);
            obj.setDireccion(direccion);
            obj.setTelefono(Telefono);
            obj.setEmail(email);
            
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
                obj.setTipoPersona(TipoPersona);
                obj.setNombre(nombre);
                obj.setTipodocumento(tipoDocumento);
                obj.setNumDocumento(numDocumento);
                obj.setDireccion(direccion);
                obj.setTelefono(Telefono);
                obj.setEmail(email);
            
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
