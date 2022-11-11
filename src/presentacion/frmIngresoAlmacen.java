/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentacion;

import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import negocio.IngresoControl;

/**
 *
 * @author rena_
 */
public class frmIngresoAlmacen extends javax.swing.JInternalFrame {
    private final IngresoControl control;
    private String accion;
    private String nombreAnterior;

    private int totalPorPagina=10;
    private int numPagina=1;
    private boolean primeraCarga=true;
    private int totalRegistros;
    
    public DefaultTableModel modeloDetalles;
    public JFrame contenedor;
    
    public frmIngresoAlmacen(JFrame frmPrincipal) {
        initComponents();
        this.contenedor=frmPrincipal;
        this.control=new IngresoControl();
        this.paginar();
        this.listar("",false);
        this.primeraCarga=false;
        tabGeneral.setEnabledAt(1, false); //Para el bloqueo de una vista
        this.accion="guardar";
        crearDetalles();
    }
    
    private void ocultarColumnas(){
        tablaListado.getColumnModel().getColumn(1).setMaxWidth(0);
        tablaListado.getColumnModel().getColumn(1).setMinWidth(0);
        tablaListado.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(0);
        tablaListado.getTableHeader().getColumnModel().getColumn(1).setMinWidth(0);
        
        tablaListado.getColumnModel().getColumn(3).setMaxWidth(0);
        tablaListado.getColumnModel().getColumn(3).setMinWidth(0);
        tablaListado.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(0);
        tablaListado.getTableHeader().getColumnModel().getColumn(3).setMinWidth(0);
    }
    
    private void paginar(){
        int totalPaginas;
        
        this.totalRegistros=this.control.total();
        this.totalPorPagina=Integer.parseInt((String)cbxtotalpagina.getSelectedItem());
        totalPaginas=(int)(Math.ceil((double)this.totalRegistros/totalPorPagina));
        if (totalPaginas==0) {
            totalPaginas=1;
        }
        this.cbxNumpagina.removeAllItems();
        for (int i = 1; i <= totalPaginas; i++) {
            cbxNumpagina.addItem(Integer.toString(i));
            
        }
        cbxNumpagina.setSelectedIndex(0);
    }

    private void listar(String texto, boolean paginar){
        
        this.totalPorPagina=Integer.parseInt((String)cbxtotalpagina.getSelectedItem());
        
        if ((String)cbxNumpagina.getSelectedItem()!=null) {
            this.numPagina=Integer.parseInt((String)cbxNumpagina.getSelectedItem());
        }
        
        if (paginar==true) {
            tablaListado.setModel(control.listar(texto, totalPorPagina, numPagina));
        }else{
            tablaListado.setModel(control.listar(texto, totalPorPagina, 1));
        }
        
        txtMostraRegistros.setText(" Mostrando "+this.control.totalMostrados()+" de un total de "+control.total()+" registros ");
        TableRowSorter order = new TableRowSorter(tablaListado.getModel()); // para el orden de mayor o menor, de A -Z o Z-A
        tablaListado.setRowSorter(order);
        this.ocultarColumnas();
    }
    
    private void crearDetalles(){
        modeloDetalles = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int fila, int columna){
            if (columna==3) {
                return columna==3;
            }
            if (columna==4) {
                return columna==4;
            }
            return columna==3;
        }
        
        @Override
        public Object getValueAt(int fila, int columna){
            if (columna==5) {
                Double cantD;
                try {
                    cantD=Double.parseDouble((String)getValueAt(fila, 3));
                } catch (Exception e) {
                    cantD=1.0;
                }
                Double precioD=Double.parseDouble((String)getValueAt(fila, 4));
                if (cantD!=null && precioD!=null) {
                    return String.format("%.2f",cantD*precioD).replace(",", "."); //PARA CONDICIONAR A SOLO 2 DECIMALES
                    
                }else{
                return 0;
                }
            }
            return super.getValueAt(fila, columna);
        }
        
        @Override
        public void setValueAt(Object aValue, int fila, int columna){
            super.setValueAt(aValue, fila, columna);
            calcularTotales();
            fireTableDataChanged();  // METODO PARA QUE ESTÉ PENDIENTE DE CUALQUIER CAMBIO EN LA TABLA
        }
            
            
        };
        
        modeloDetalles.setColumnIdentifiers(new Object[]{"ID","CODIGO","ARTICULO","CANTIDAD","PRECIO","SUBTOTAL"});
        tablaDetalles.setModel(modeloDetalles);
        
    }
    
    public void agregarDetalles(String id, String codigo, String nombre, String precio){
        
        String idTemporal;
        boolean existe=false;
        
        for (int i = 0; i < this.modeloDetalles.getRowCount(); i++) {
            idTemporal=String.valueOf(modeloDetalles.getValueAt(i, 0));
            if (idTemporal.equals(id)) {
                existe=true;
            }
        }
        if (existe) {
            this.mensajeError("El articulo ya ha sido agregado");
        }else{
            this.modeloDetalles.addRow(new Object[]{id,codigo,nombre,"1",precio,precio});
            this.calcularTotales();
        }
        
    }   
    
    
    
    private void calcularTotales(){
        double total=0;
        double subTotal;
        int items=modeloDetalles.getRowCount();
        if (items==0) {
            total=0;
        }else{
            for (int i = 0; i <items; i++) {
                total=(total+Double.parseDouble(String.valueOf(modeloDetalles.getValueAt(i, 5)).replace(",", ".")));
                
                
            }
        }
        subTotal=total/(1+Double.parseDouble(txtImpuesto.getText().replace(",", ".")));
        
        txtTotal.setText(String.format("%.2f", total));
        txtSubtotal.setText(String.format("%.2f", subTotal));
        txtIGV.setText(String.format("%.2f", total-subTotal));
    }
    
    private void limpiar(){
        txtNombreProveedor.setText("");
        txtCodigoProveedor.setText("");
        txtSerieComprobante.setText("");
        txtNroComprobante.setText("");
        txtImpuesto.setText("0.18");
        
        this.accion="guardar";
        
        txtSubtotal.setText("0.00");
        txtIGV.setText("0.00");
        txtTotal.setText("0.00");
        this.crearDetalles();
        this.btnGuardar.setVisible(true);
        
    }
    
    private void mensajeError(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje,"Sistema",JOptionPane.ERROR_MESSAGE);
    }
    
    private void mensajeOK(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje,"Sistema",JOptionPane.INFORMATION_MESSAGE);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabGeneral = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaListado = new javax.swing.JTable();
        txtMostraRegistros = new javax.swing.JLabel();
        btnNuevo = new javax.swing.JButton();
        btnDesactivar = new javax.swing.JButton();
        cbxNumpagina = new javax.swing.JComboBox<>();
        cbxtotalpagina = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        btnverIngreso = new javax.swing.JButton();
        btnquitar = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCodigoProveedor = new javax.swing.JTextField();
        txtNombreProveedor = new javax.swing.JTextField();
        btnBuscarProveedor = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtImpuesto = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cbxTipoComprobante = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtSerieComprobante = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtNroComprobante = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtCodigoArticulo = new javax.swing.JTextField();
        btnVerArticulo = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaDetalles = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        txtSubtotal = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtIGV = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Compras ");
        setVisible(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("Nombre: ");

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        tablaListado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tablaListado);

        txtMostraRegistros.setText("Registros");

        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnDesactivar.setText("Anular");
        btnDesactivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDesactivarActionPerformed(evt);
            }
        });

        cbxNumpagina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxNumpaginaActionPerformed(evt);
            }
        });

        cbxtotalpagina.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "10", "20", "50", "100", "200", "500" }));
        cbxtotalpagina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxtotalpaginaActionPerformed(evt);
            }
        });

        jLabel2.setText("Nro de Pagina:");

        jLabel11.setText("Total de Registros por Pagina:");

        btnverIngreso.setText("Ver Detalle Compra");
        btnverIngreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnverIngresoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(btnBuscar)
                        .addGap(28, 28, 28)
                        .addComponent(btnNuevo)
                        .addGap(117, 117, 117)
                        .addComponent(btnverIngreso)
                        .addContainerGap(260, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(btnDesactivar, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtMostraRegistros, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(93, 93, 93))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(107, 107, 107)
                        .addComponent(cbxNumpagina, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(146, 146, 146)
                        .addComponent(jLabel11)
                        .addGap(33, 33, 33)
                        .addComponent(cbxtotalpagina, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar)
                    .addComponent(btnNuevo)
                    .addComponent(btnverIngreso))
                .addGap(36, 36, 36)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(cbxtotalpagina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(cbxNumpagina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMostraRegistros, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDesactivar))
                .addGap(23, 23, 23))
        );

        tabGeneral.addTab("Listado", jPanel1);

        btnquitar.setBackground(new java.awt.Color(255, 255, 255));

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        jLabel4.setText("(*) Indica que es un campo obligatorio");

        jLabel5.setText("Proveedor(*)");

        txtCodigoProveedor.setEditable(false);

        txtNombreProveedor.setEditable(false);

        btnBuscarProveedor.setText("...");
        btnBuscarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProveedorActionPerformed(evt);
            }
        });

        jLabel3.setText("Impuesto(*)");

        txtImpuesto.setText("0.18");

        jLabel6.setText("Tipo Comprobante(*)");

        cbxTipoComprobante.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BOLETA", "FACTURA", "TICKET", "GUIA" }));

        jLabel7.setText("Serie Comprobante(*)");

        jLabel8.setText("Nro Comprobante(*)");

        jLabel9.setText("Articulo");

        txtCodigoArticulo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCodigoArticuloKeyReleased(evt);
            }
        });

        btnVerArticulo.setText("ver");
        btnVerArticulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerArticuloActionPerformed(evt);
            }
        });

        tablaDetalles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(tablaDetalles);

        jLabel10.setText("SubTotal");

        txtSubtotal.setEditable(false);

        jLabel12.setText("Impuesto");

        txtIGV.setEditable(false);

        jLabel13.setText("Total");

        txtTotal.setEditable(false);

        jButton1.setText("Quitar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout btnquitarLayout = new javax.swing.GroupLayout(btnquitar);
        btnquitar.setLayout(btnquitarLayout);
        btnquitarLayout.setHorizontalGroup(
            btnquitarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnquitarLayout.createSequentialGroup()
                .addGroup(btnquitarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(btnquitarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, btnquitarLayout.createSequentialGroup()
                            .addGap(45, 45, 45)
                            .addGroup(btnquitarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(btnquitarLayout.createSequentialGroup()
                                    .addComponent(jLabel9)
                                    .addGap(30, 30, 30)
                                    .addComponent(txtCodigoArticulo, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(26, 26, 26)
                                    .addComponent(btnVerArticulo)
                                    .addGap(244, 244, 244)
                                    .addComponent(jButton1))
                                .addGroup(btnquitarLayout.createSequentialGroup()
                                    .addGroup(btnquitarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(btnquitarLayout.createSequentialGroup()
                                            .addComponent(jLabel6)
                                            .addGap(18, 18, 18)
                                            .addComponent(cbxTipoComprobante, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(btnquitarLayout.createSequentialGroup()
                                            .addComponent(jLabel5)
                                            .addGap(18, 18, 18)
                                            .addComponent(txtCodigoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(txtNombreProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(btnquitarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(btnquitarLayout.createSequentialGroup()
                                            .addGap(66, 66, 66)
                                            .addComponent(jLabel7)
                                            .addGap(26, 26, 26)
                                            .addComponent(txtSerieComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(btnquitarLayout.createSequentialGroup()
                                            .addGap(28, 28, 28)
                                            .addComponent(btnBuscarProveedor)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel3)))
                                    .addGroup(btnquitarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(btnquitarLayout.createSequentialGroup()
                                            .addGap(28, 28, 28)
                                            .addComponent(txtImpuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(btnquitarLayout.createSequentialGroup()
                                            .addGap(38, 38, 38)
                                            .addComponent(jLabel8)
                                            .addGap(32, 32, 32)
                                            .addComponent(txtNroComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, btnquitarLayout.createSequentialGroup()
                            .addGap(23, 23, 23)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 926, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(btnquitarLayout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(btnquitarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(btnquitarLayout.createSequentialGroup()
                                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(103, 103, 103)
                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(btnquitarLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel4)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 471, Short.MAX_VALUE)
                        .addGroup(btnquitarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addGroup(btnquitarLayout.createSequentialGroup()
                                .addGroup(btnquitarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel13))
                                .addGap(28, 28, 28)
                                .addGroup(btnquitarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtIGV)
                                    .addComponent(txtSubtotal)
                                    .addComponent(txtTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))))))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        btnquitarLayout.setVerticalGroup(
            btnquitarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnquitarLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(btnquitarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtCodigoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarProveedor)
                    .addComponent(jLabel3)
                    .addComponent(txtImpuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(btnquitarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cbxTipoComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txtSerieComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txtNroComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(btnquitarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtCodigoArticulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnVerArticulo)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(btnquitarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGroup(btnquitarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(btnquitarLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(btnquitarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIGV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addGap(18, 18, 18)
                        .addGroup(btnquitarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(btnquitarLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(btnquitarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        tabGeneral.addTab("Mantenimiento", btnquitar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabGeneral)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabGeneral)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        this.listar(txtBuscar.getText(),false);
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        tabGeneral.setEnabledAt(1, true);
        tabGeneral.setEnabledAt(0, false);
        tabGeneral.setSelectedIndex(1);
        this.accion="guardar";
        btnGuardar.setText("Guardar");
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnDesactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesactivarActionPerformed
        if (tablaListado.getSelectedRowCount()==1) {
            String id=String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 0));
            String comprobante = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 5));
            String serie = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 6));
            String numero = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 7));
            
            if (JOptionPane.showConfirmDialog(this, "Deseas anular el registro : "+ comprobante+" "+serie+" - "+ numero+" ? ","Anular Registro",JOptionPane.YES_NO_OPTION)==0){
                String resp = this.control.anular(Integer.parseInt(id));
                if (resp.equals("OK")) {
                    this.mensajeOK("Registro Anulado");
                    this.listar("",false);
                }else{
                    this.mensajeError(resp);
                }
            }
            
        }else{
            this.mensajeError("Selecciones un registro a anular!");
        }
        
        /*
        ACTUALIZAR EL STOCK DESPUES DE ANULAR UN INGRESO AL ALMACÉN
        
        DELIMITER //
        CREATE TRIGGER tr_updStockIngresoAnular AFTER UPDATE ON ingreso 
        FOR EACH ROW BEGIN
        UPDATE articulo a
        JOIN detalle_ingreso di
        ON di.articulo_id = a.id
        AND di.ingreso_id= new.id
        SET a.stock = a.stock -di.cantidad;
        END;
        //
        DELIMITER ;
        
        
        */
    }//GEN-LAST:event_btnDesactivarActionPerformed

    private void cbxNumpaginaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxNumpaginaActionPerformed
        if (primeraCarga==false) {
            this.listar("", true);
        }
    }//GEN-LAST:event_cbxNumpaginaActionPerformed

    private void cbxtotalpaginaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxtotalpaginaActionPerformed
        this.paginar();
    }//GEN-LAST:event_cbxtotalpaginaActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        tabGeneral.setEnabledAt(1, false);
        tabGeneral.setEnabledAt(0, true);
        tabGeneral.setSelectedIndex(0);
        limpiar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        if (txtCodigoProveedor.getText().length()==0) {
            JOptionPane.showMessageDialog(this, "Debes Seleccionar un proveedor","Sistema",JOptionPane.WARNING_MESSAGE);
            txtCodigoProveedor.requestFocus();
            return;
        }
        
        if (txtSerieComprobante.getText().length()>7) {
            JOptionPane.showMessageDialog(this, "Debes ingresar una serie no mayor a 7 caracteres.","Sistema",JOptionPane.WARNING_MESSAGE);
            txtSerieComprobante.requestFocus();
            return;
        }
        
        if (txtNroComprobante.getText().length()==0 || txtNroComprobante.getText().length()>10) {
            JOptionPane.showMessageDialog(this, "Debes ingresar un numero de comprobante no mayor a 10 caracteres.","Sistema",JOptionPane.WARNING_MESSAGE);
            txtNroComprobante.requestFocus();
            return;
        }
        
        if (this.modeloDetalles.getRowCount()==0) {
            JOptionPane.showMessageDialog(this, "Debes agregar articulos al detalle","Sistema",JOptionPane.WARNING_MESSAGE);
            return;
        }

        String resp;
            //GUARDAR

        resp = this.control.insertar(Integer.parseInt(txtCodigoProveedor.getText()),(String)cbxTipoComprobante.getSelectedItem(),txtSerieComprobante.getText(),txtNroComprobante.getText(),Double.parseDouble(txtImpuesto.getText()),Double.parseDouble(txtTotal.getText().replace(",", ".")),modeloDetalles);        
        if (resp.equals("OK")) {
            this.mensajeOK("Registrado Correctamente");
            limpiar();
            listar("", false);
        } else {
            this.mensajeError(resp);
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
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnBuscarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProveedorActionPerformed
        frmSeleccionarProveedorDeCompra frmSPC = new frmSeleccionarProveedorDeCompra(contenedor, this, true);
        frmSPC.toFront();
    }//GEN-LAST:event_btnBuscarProveedorActionPerformed

    private void btnVerArticuloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerArticuloActionPerformed
        frmSeleccionarArticuloCompra frm = new frmSeleccionarArticuloCompra(contenedor, this, true);
        frm.toFront(); // que se muestre al frente
    }//GEN-LAST:event_btnVerArticuloActionPerformed

    private void txtCodigoArticuloKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoArticuloKeyReleased
        if (txtCodigoArticulo.getText().length()>0) {
            if (evt.getKeyCode()==KeyEvent.VK_ENTER) {
                entidad.Articulo art;
                art=this.control.obtenerArticuloCodigoIngreso(txtCodigoArticulo.getText());
                if (art==null) {
                    this.mensajeError("No existe un articulo con ese codigo");
                }else{
                    this.agregarDetalles(Integer.toString(art.getId()), art.getCodigo(), art.getNombre(), Double.toString(art.getPrecioVenta()));
                }
            }
        }else{
            this.mensajeError("Ingrese el codigo a buscar");
        }
    }//GEN-LAST:event_txtCodigoArticuloKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (tablaDetalles.getSelectedRowCount()==1) {
            this.modeloDetalles.removeRow(tablaDetalles.getSelectedRow());
            this.calcularTotales();
            
        }else{
            this.mensajeError("Seleccione el detalle a quitar");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnverIngresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnverIngresoActionPerformed
        if (tablaListado.getSelectedRowCount()==1) {
            String id=String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 0));
            String id_proveedor=String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 3));
            String nombre_proveedor=String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 4));
            String tipo_comprobante=String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 5));
            String serie_comprobante=String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 6));
            String nro_comprobante=String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 7));
            String impuesto=String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 9));
            
            txtCodigoProveedor.setText(id_proveedor);
            txtNombreProveedor.setText(nombre_proveedor);
            cbxTipoComprobante.setSelectedItem(tipo_comprobante);
            txtSerieComprobante.setText(serie_comprobante);
            txtNroComprobante.setText(nro_comprobante);
            txtImpuesto.setText(impuesto);
            
            this.modeloDetalles=control.listarDetalle(Integer.parseInt(id));
            tablaDetalles.setModel(modeloDetalles);
            this.calcularTotales();
            
            tabGeneral.setEnabledAt(1,true);
            tabGeneral.setEnabledAt(0,false);
            tabGeneral.setSelectedIndex(1);
            btnGuardar.setVisible(false);
            
        }else{
            this.mensajeError("Seleccione un ingreso a mostrar");
        }
    }//GEN-LAST:event_btnverIngresoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnBuscarProveedor;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnDesactivar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnVerArticulo;
    private javax.swing.JPanel btnquitar;
    private javax.swing.JButton btnverIngreso;
    private javax.swing.JComboBox<String> cbxNumpagina;
    private javax.swing.JComboBox<String> cbxTipoComprobante;
    private javax.swing.JComboBox<String> cbxtotalpagina;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane tabGeneral;
    private javax.swing.JTable tablaDetalles;
    private javax.swing.JTable tablaListado;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCodigoArticulo;
    public javax.swing.JTextField txtCodigoProveedor;
    private javax.swing.JTextField txtIGV;
    private javax.swing.JTextField txtImpuesto;
    private javax.swing.JLabel txtMostraRegistros;
    public javax.swing.JTextField txtNombreProveedor;
    private javax.swing.JTextField txtNroComprobante;
    private javax.swing.JTextField txtSerieComprobante;
    private javax.swing.JTextField txtSubtotal;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
