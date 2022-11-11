package negocio;

import database.Conexion;
import datos.ArticuloDAO;
import datos.CategoriaDAO;
import entidad.Articulo;
import entidad.Categoria;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;


public class articuloControl {
    private final ArticuloDAO datos;
    private final CategoriaDAO datoscat;
    private Articulo obj;
    private DefaultTableModel modeloTabla;
    public int registrosMostrados;
    
    public articuloControl(){
        this.datos=new ArticuloDAO();
        this.datoscat=new CategoriaDAO();
        this.obj=new Articulo();
        this.registrosMostrados=0;
    }
    
    public DefaultTableModel listar(String texto, int totalPorPagina, int nroPagina){
        List<Articulo> lista = new ArrayList();
        lista.addAll(datos.listar(texto,totalPorPagina,nroPagina));
        
        String[] titulos = {"ID","Categoria ID","Categoria","Codigo","Nombre","Precio Venta","Stock","Descripción","Imagen","Estado"};
        this.modeloTabla=new DefaultTableModel(null, titulos);
        
        String estado;
        String[] registro = new String[10];
        
        this.registrosMostrados=0;
        for (Articulo item : lista) {
            if (item.isActivo()) {
                estado="Activo";
            }else{
                estado="Inactivo";
            }
            
            registro[0]=Integer.toString(item.getId());
            registro[1]=Integer.toString(item.getCategoriaid());
            registro[2]=item.getCategoriaNombre();
            registro[3]=item.getCodigo();
            registro[4]=item.getNombre();
            registro[5]=Double.toString(item.getPrecioVenta());
            registro[6]=Integer.toString(item.getStock());
            registro[7]=item.getDescripcion();
            registro[8]=item.getImagen();
            registro[9]=estado;
            
            this.modeloTabla.addRow(registro);
            this.registrosMostrados++;
        }
        return this.modeloTabla;
    }
    
    public DefaultTableModel listarArticuloVenta(String texto, int totalPorPagina, int nroPagina){
        List<Articulo> lista = new ArrayList();
        lista.addAll(datos.listarArticuloVenta(texto,totalPorPagina,nroPagina));
        
        String[] titulos = {"ID","Categoria ID","Categoria","Codigo","Nombre","Precio Venta","Stock","Descripción","Imagen","Estado"};
        this.modeloTabla=new DefaultTableModel(null, titulos);
        
        String estado;
        String[] registro = new String[10];
        
        this.registrosMostrados=0;
        for (Articulo item : lista) {
            if (item.isActivo()) {
                estado="Activo";
            }else{
                estado="Inactivo";
            }
            
            registro[0]=Integer.toString(item.getId());
            registro[1]=Integer.toString(item.getCategoriaid());
            registro[2]=item.getCategoriaNombre();
            registro[3]=item.getCodigo();
            registro[4]=item.getNombre();
            registro[5]=Double.toString(item.getPrecioVenta());
            registro[6]=Integer.toString(item.getStock());
            registro[7]=item.getDescripcion();
            registro[8]=item.getImagen();
            registro[9]=estado;
            
            this.modeloTabla.addRow(registro);
            this.registrosMostrados++;
        }
        return this.modeloTabla;
    }
    
    public DefaultComboBoxModel seleccionar(){
        DefaultComboBoxModel items = new DefaultComboBoxModel();
        List<Categoria> list = new ArrayList();
        list=datoscat.seleccionar();
        for (Categoria item: list) {
            items.addElement(new Categoria(item.getId(), item.getNombre()));
        }
        return items;
    }
    
    public String insertar(int categoria_id, String codigo, String nombre, double precio_venta, int stock, String descripcion, String imagen){
        if (datos.existe(nombre)) {
            return "El registro ya existe!";
        } else {
            obj.setCategoriaid(categoria_id);
            obj.setCodigo(codigo);
            obj.setNombre(nombre);
            obj.setPrecioVenta(precio_venta);
            obj.setStock(stock);
            obj.setDescripcion(descripcion);
            obj.setImagen(imagen);
            if (datos.insertar(obj)) {
                return "OK";
            }else{
                return "Error en el registro";
            }
        }
    }
    
    public String actualizar(int id,int categoria_id, String codigo, String nombre, String nombreAnt, double precio_venta, int stock, String descripcion, String imagen){
        if (nombre.equals(nombreAnt)) {  // si el nombre es igual al nombre anterior
            obj.setId(id);
            obj.setCategoriaid(categoria_id);
            obj.setCodigo(codigo);
            obj.setNombre(nombre);
            obj.setPrecioVenta(precio_venta);
            obj.setStock(stock);
            obj.setDescripcion(descripcion);
            obj.setImagen(imagen);
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
                obj.setCategoriaid(categoria_id);
                obj.setCodigo(codigo);
                obj.setNombre(nombre);
                obj.setPrecioVenta(precio_venta);
                obj.setStock(stock);
                obj.setDescripcion(descripcion);
                obj.setImagen(imagen);
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
    
    
    public void ReporteArticulos(){
        Map p=new HashMap();
        JasperReport report;
        JasperPrint print;
        
        Conexion cnn = Conexion.getInstancia();
        
        try {
        
            report = JasperCompileManager.compileReport(new File("").getAbsolutePath()+"/src/reportes/RptArticulo.jrxml");
            print = JasperFillManager.fillReport(report, p,cnn.conectar());
            JasperViewer view = new JasperViewer(print,false);
            view.setTitle("Reporte de Articulos");
            view.setVisible(true);
        } catch (Exception e) {
            e.getMessage();
        }
    }
    
    public void ReporteArticulosBarras(){
        Map p=new HashMap();
        JasperReport report;
        JasperPrint print;
        
        Conexion cnn = Conexion.getInstancia();
        
        try {
        
            report = JasperCompileManager.compileReport(new File("").getAbsolutePath()+"/src/reportes/RptArticulosBarras.jrxml");
            print = JasperFillManager.fillReport(report, p,cnn.conectar());
            JasperViewer view = new JasperViewer(print,false);
            view.setTitle("Reporte de Articulos");
            view.setVisible(true);
        } catch (Exception e) {
            e.getMessage();
        }
    }
    
    
}
