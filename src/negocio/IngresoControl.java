package negocio;

import datos.IngresoDAO;
import datos.ArticuloDAO;
import entidad.Articulo;
import entidad.DetalleIngreso;
import entidad.Ingreso;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.ArrayList;


public class IngresoControl {
    private final IngresoDAO datos;
    private final ArticuloDAO datosart;
    private Ingreso obj;
    private DefaultTableModel modeloTabla;
    public int registrosMostrados;
    
    public IngresoControl(){
        this.datos=new IngresoDAO();
        this.datosart= new ArticuloDAO();
        this.obj=new Ingreso();
        this.registrosMostrados=0;
    }
    
    public DefaultTableModel listar(String texto, int totalPorPagina, int nroPagina){
        List<Ingreso> lista = new ArrayList();
        lista.addAll(datos.listar(texto,totalPorPagina,nroPagina));
        
        String[] titulos = {"ID","Usuario ID","Usuario","Proveedor ID","Proveedor","Tipo Comprobante","Serie","Número","Fecha","Impuesto","Total","Estado"};
        this.modeloTabla=new DefaultTableModel(null, titulos);
        
        String[] registro = new String[12];
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        this.registrosMostrados=0;
        for (Ingreso item : lista) {
            
            registro[0]=Integer.toString(item.getId());
            registro[1]=Integer.toString(item.getUsuarioID());
            registro[2]=item.getUsuarioNombre();
            registro[3]=Integer.toString(item.getPersonaID());
            registro[4]=item.getPersonaNombre();
            registro[5]=item.getTipoComprobante();
            registro[6]=item.getSerieComprobante();
            registro[7]=item.getNroComprobante();
            registro[8]=sdf.format(item.getFecha());
            registro[9]=Double.toString(item.getImpuesto());
            registro[10]=Double.toString(item.getTotal());
            registro[11]=item.getEstado();
            
            this.modeloTabla.addRow(registro);
            this.registrosMostrados++;
        }
        return this.modeloTabla;
    }
    
    public DefaultTableModel listarDetalle(int id){
        List<DetalleIngreso> lista = new ArrayList();
        lista.addAll(datos.listarDetalle(id));
        
        String[] titulos = {"ID","CODIGO","ARTICULO","CANTIDAD","PRECIO","SUBTOTAL"};
        this.modeloTabla=new DefaultTableModel(null, titulos);
        
        String[] registro = new String[6];
        for (DetalleIngreso item : lista) {
            
            registro[0]=Integer.toString(item.getArticuloID());
            registro[1]=(item.getArticuloCodigo());
            registro[2]=item.getArticuloNombre();
            registro[3]=Integer.toString(item.getCantidad());
            registro[4]=Double.toString(item.getPrecio());
            registro[5]=Double.toString(item.getSubTotal());
            
            this.modeloTabla.addRow(registro);
        }
        return this.modeloTabla;
    }
    
    public Articulo obtenerArticuloCodigoIngreso(String codigo){
        Articulo art=datosart.obtenerArticuloCodigoIngreso(codigo);
        return art;
        
    }
    
    
    public String insertar(int persona_id, String TipoComprobante, String SerieComprobante, String NroComprobante, double Impuesto, double total, DefaultTableModel modeloDetalle){
        if (datos.existe(SerieComprobante,NroComprobante)) {
            return "El registro ya existe!";
        } else {
            obj.setPersonaID(persona_id);
            obj.setUsuarioID(Variables.usuarioID);
            obj.setTipoComprobante(TipoComprobante);
            obj.setSerieComprobante(SerieComprobante);
            obj.setNroComprobante(NroComprobante);
            obj.setImpuesto(Impuesto);
            obj.setTotal(total);
            
            List<DetalleIngreso> detalles = new ArrayList();
            int articuloID;
            int cantidad;
            double precio;
            
            for (int i = 0; i < modeloDetalle.getRowCount(); i++) {
                articuloID=Integer.parseInt(String.valueOf(modeloDetalle.getValueAt(i, 0)));
                cantidad=Integer.parseInt(String.valueOf(modeloDetalle.getValueAt(i, 3)));
                precio=Double.parseDouble(String.valueOf(modeloDetalle.getValueAt(i, 4)));
                detalles.add(new DetalleIngreso(articuloID,cantidad,precio));
            }
            
            obj.setDetalles(detalles);
            
            if (datos.insertar(obj)) {
                return "OK";
            }else{
                return "Error en el registro";
            }
        }
    }
    
    public String anular(int id){
        if (datos.anular(id)) {
            return "OK";
        }else{
            return "No se puede anular el registro";
        }
    }
    public int total(){
        return datos.total();
    }
    
    public int totalMostrados(){
        return this.registrosMostrados;
    }
}
