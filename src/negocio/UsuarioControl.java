package negocio;

import datos.UsuarioDAO;
import datos.RolDAO;
import entidad.Usuario;
import entidad.Rol;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.DefaultComboBoxModel;


public class UsuarioControl {
    private final UsuarioDAO datos;
    private final RolDAO datosRol;   //para extraer el nombre y el id del rol
    private Usuario obj;
    private DefaultTableModel modeloTabla;
    public int registrosMostrados;
    
    public UsuarioControl(){
        this.datos=new UsuarioDAO();
        this.datosRol=new RolDAO();
        this.obj=new Usuario();
        this.registrosMostrados=0;
    }
    
    public DefaultTableModel listar(String texto, int totalPorPagina, int nroPagina){
        List<Usuario> lista = new ArrayList();
        lista.addAll(datos.listar(texto,totalPorPagina,nroPagina));
        
        String[] titulos = {"ID","Rol ID","Rol","Usuario","Documento","# Documento","Dirección","Telefono","Email","Clave","Estado"};
        this.modeloTabla=new DefaultTableModel(null, titulos);
        
        String estado;
        String[] registro = new String[11];
        
        this.registrosMostrados=0;
        for (Usuario item: lista) {
            if (item.isActivo()) {
                estado="Activo";
            }else{
                estado="Inactivo";
            }
            
            registro[0]=Integer.toString(item.getId());
            registro[1]=Integer.toString(item.getRolId());
            registro[2]=item.getNombreRol();
            registro[3]=item.getNombre();
            registro[4]=item.getTipodocumento();
            registro[5]=item.getNumDocumento();
            registro[6]=item.getDireccion();
            registro[7]=item.getTelefono();
            registro[8]=item.getEmail();
            registro[9]=item.getClave();
            registro[10]=estado;
            
            this.modeloTabla.addRow(registro);
            this.registrosMostrados++;
        }
        return this.modeloTabla;
    }
    
    public String login(String email, String clave){
        String resp="0"; //si no existe el usuario
        Usuario us = this.datos.login(email, this.encriptar(clave));
        if (us!=null) {
            if (us.isActivo()) {
                Variables.usuarioID=us.getId();
                Variables.rolID= us.getRolId();
                Variables.rolNombre=us.getNombreRol();
                Variables.usuarioNombre=us.getNombre();
                Variables.usuarioEmail = us.getEmail();
                resp="1"; // si el usuario Existe y esta activo
            }else{
                resp="2"; // sie l usuario existe pero no esta activo
            }
        }
        return resp;
    }
    
    public DefaultComboBoxModel seleccionar(){
        DefaultComboBoxModel items = new DefaultComboBoxModel();
        List<Rol> list = new ArrayList();
        list=datosRol.seleccionar();
        for (Rol item: list) {
            items.addElement(new Rol(item.getId(), item.getNombre()));
        }
        return items;
    }
    
    private static String encriptar(String valor){
        MessageDigest md;
        try {
                md=MessageDigest.getInstance("SHA-256"); //indicamos el tipo de algoritmo
        } catch (NoSuchAlgorithmException e) {
                return null;
        }
        
        byte[] hash = md.digest(valor.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
                sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    
    public String insertar(int RolId,String nombre, String tipoDocumento, String numDocumento, String direccion, String Telefono, String email, String clave){
        if (datos.existe(email)) {
            return "El registro ya existe!";
        } else {
            obj.setRodId(RolId);
            obj.setNombre(nombre);
            obj.setTipodocumento(tipoDocumento);
            obj.setNumDocumento(numDocumento);
            obj.setDireccion(direccion);
            obj.setTelefono(Telefono);
            obj.setEmail(email);
            obj.setClave(this.encriptar(clave));
            if (datos.insertar(obj)) {
                return "OK";
            }else{
                return "Error en el registro";
            }
        }
    }
    
    public String actualizar(int id, int RolId, String nombre, String tipoDocumento, String numDocumento, String direccion, String Telefono, String email, String emailAnterior, String clave){
        if (email.equals(emailAnterior)) {  // si el email es igual al email anterior
            obj.setId(id);
            obj.setRodId(RolId);
            obj.setNombre(nombre);
            obj.setTipodocumento(tipoDocumento);
            obj.setNumDocumento(numDocumento);
            obj.setDireccion(direccion);
            obj.setTelefono(Telefono);
            obj.setEmail(email);
            
            String encriptado;
            if (clave.length()==64) {
                encriptado=clave;
            }else{
                encriptado=this.encriptar(clave);
            }
            obj.setClave(encriptado);
            
            if (datos.actualizar(obj)) {
                return "OK";
            }else{
                return "Error en la actualizacion";
            }
        }else{
            if (datos.existe(email)) {
                return "El registro ya existe";
            }else{
                obj.setId(id);
                obj.setRodId(RolId);
                obj.setNombre(nombre);
                obj.setTipodocumento(tipoDocumento);
                obj.setNumDocumento(numDocumento);
                obj.setDireccion(direccion);
                obj.setTelefono(Telefono);
                obj.setEmail(email);

                String encriptado;
                if (clave.length() == 64) {
                    encriptado = clave;
                } else {
                    encriptado = this.encriptar(clave);
                }
                obj.setClave(encriptado);
                
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
