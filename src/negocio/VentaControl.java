package negocio;

import database.Conexion;
import datos.VentaDAO;
import datos.ArticuloDAO;
import entidad.Articulo;
import entidad.DetalleVenta;
import entidad.Venta;
import java.io.File;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.ArrayList;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;


public class VentaControl {
    private final VentaDAO datos;
    private final ArticuloDAO datosart;
    private Venta obj;
    private DefaultTableModel modeloTabla;
    public int registrosMostrados;
    
    public VentaControl(){
        this.datos=new VentaDAO();
        this.datosart= new ArticuloDAO();
        this.obj=new Venta();
        this.registrosMostrados=0;
    }
    
    public DefaultTableModel listar(String texto, int totalPorPagina, int nroPagina){
        List<Venta> lista = new ArrayList();
        lista.addAll(datos.listar(texto,totalPorPagina,nroPagina));
        
        String[] titulos = {"ID","Usuario ID","Usuario","cliente ID","Cliente","Tipo Comprobante","Serie","Número","Fecha","Impuesto","Total","Estado"};
        this.modeloTabla=new DefaultTableModel(null, titulos);
        
        String[] registro = new String[12];
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        this.registrosMostrados=0;
        for (Venta item : lista) {
            
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
        List<DetalleVenta> lista = new ArrayList();
        lista.addAll(datos.listarDetalle(id));
        
        String[] titulos = {"ID","CODIGO","ARTICULO","STOCK","CANTIDAD","PRECIO","DESCUENTO","SUBTOTAL"};
        this.modeloTabla=new DefaultTableModel(null, titulos);
        
        String[] registro = new String[8];
        for (DetalleVenta item : lista) {
            
            registro[0]=Integer.toString(item.getArticuloID());
            registro[1]=(item.getArticuloCodigo());
            registro[2]=item.getArticuloNombre();
            registro[3]=Integer.toString(item.getArticuloStock());
            registro[4]=Integer.toString(item.getCantidad());
            registro[5]=Double.toString(item.getPrecio());
            registro[6]=Double.toString(item.getDescuento());
            registro[7]=Double.toString(item.getSubTotal());
            
            this.modeloTabla.addRow(registro);
        }
        return this.modeloTabla;
    }
    
    public DefaultTableModel consultaFechas(Date fechaInicio, Date fechaFin){
        List<Venta> lista=new ArrayList();
        lista.addAll(datos.consultaFechas(fechaInicio,fechaFin));
        
        String[] titulos={"ID","Usuario ID","Usuario","Cliente ID","Cliente","Tipo Comprobante","Serie","Número","Fecha","Impuesto","Total","Estado"};
        this.modeloTabla=new DefaultTableModel(null,titulos);        
        
        String[] registro = new String[12];
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
        
        this.registrosMostrados=0;
        for (Venta item:lista){
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
            this.registrosMostrados=this.registrosMostrados+1;
        }
        return this.modeloTabla;
    }
    
    public Articulo obtenerArticuloCodigoVenta(String codigo){
        Articulo art=datosart.obtenerArticuloCodigoVenta(codigo);
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
            
            List<DetalleVenta> detalles = new ArrayList();
            int articuloID;
            int cantidad;
            double precio;
            double descuento;
            
            for (int i = 0; i < modeloDetalle.getRowCount(); i++) {
                articuloID=Integer.parseInt(String.valueOf(modeloDetalle.getValueAt(i, 0)));
                cantidad=Integer.parseInt(String.valueOf(modeloDetalle.getValueAt(i, 4)));
                precio=Double.parseDouble(String.valueOf(modeloDetalle.getValueAt(i, 5)));
                descuento=Double.parseDouble(String.valueOf(modeloDetalle.getValueAt(i, 6)));
                detalles.add(new DetalleVenta(articuloID,cantidad,precio,descuento));
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
    
    public String GenerarUltimaSerieComprobante(String tipoComprobante) {
        return this.datos.GenerarUltimaSerieComprobante(tipoComprobante);
    }
    
    public String GenerarUltimoNroComprobante(String tipoComprobante,String serieComprobante) {
        return this.datos.GenerarUltimoNroComprobante(tipoComprobante, serieComprobante);
    }
    
    public int total(){
        return datos.total();
    }
    
    public int totalMostrados(){
        return this.registrosMostrados;
    }
    
    public void ReporteComprobante(String idventa){
        Map p=new HashMap();
        p.put("IdVenta", idventa);
        JasperReport report;
        JasperPrint print;
        
        Conexion cnn = Conexion.getInstancia();
        
        try {
        
            report = JasperCompileManager.compileReport(new File("").getAbsolutePath()+"/src/reportes/RptComprobante.jrxml");
            print = JasperFillManager.fillReport(report, p,cnn.conectar());
            JasperViewer view = new JasperViewer(print,false);
            view.setTitle("Reporte de Articulos");
            view.setVisible(true);
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
