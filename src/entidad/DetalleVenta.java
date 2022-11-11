/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

/**
 *
 * @author rena_
 */
public class DetalleVenta {
    private int id;
    private int ventaID;
    private int articuloID;
    private String articuloCodigo;
    private String articuloNombre;
    private int articuloStock;
    private int cantidad;
    private double precio;
    private double descuento;
    private double subTotal;

    public DetalleVenta() {
    }

    public DetalleVenta(int articuloID, int cantidad, double precio, double descuento) {
        this.articuloID = articuloID;
        this.cantidad = cantidad;
        this.precio = precio;
        this.descuento = descuento;
    }
    

    public DetalleVenta(int articuloID, String articuloCodigo, String articuloNombre, int articuloStock, int cantidad, double precio, double descuento, double subTotal) {
        this.articuloID = articuloID;
        this.articuloCodigo = articuloCodigo;
        this.articuloNombre = articuloNombre;
        this.articuloStock = articuloStock;
        this.cantidad = cantidad;
        this.precio = precio;
        this.descuento = descuento;
        this.subTotal = subTotal;
    }
    

    public DetalleVenta(int id, int ventaID, int articuloID, String articuloCodigo, String articuloNombre, int articuloStock, int cantidad, double precio, double descuento, double subTotal) {
        this.id = id;
        this.ventaID = ventaID;
        this.articuloID = articuloID;
        this.articuloCodigo = articuloCodigo;
        this.articuloNombre = articuloNombre;
        this.articuloStock = articuloStock;
        this.cantidad = cantidad;
        this.precio = precio;
        this.descuento = descuento;
        this.subTotal = subTotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVentaID() {
        return ventaID;
    }

    public void setVentaID(int ventaID) {
        this.ventaID = ventaID;
    }

    public int getArticuloID() {
        return articuloID;
    }

    public void setArticuloID(int articuloID) {
        this.articuloID = articuloID;
    }

    public String getArticuloCodigo() {
        return articuloCodigo;
    }

    public void setArticuloCodigo(String articuloCodigo) {
        this.articuloCodigo = articuloCodigo;
    }

    public String getArticuloNombre() {
        return articuloNombre;
    }

    public void setArticuloNombre(String articuloNombre) {
        this.articuloNombre = articuloNombre;
    }

    public int getArticuloStock() {
        return articuloStock;
    }

    public void setArticuloStock(int articuloStock) {
        this.articuloStock = articuloStock;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }
    
    
}
