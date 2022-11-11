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
public class DetalleIngreso {
    private int id;
    private int ingresoID;
    private int articuloID;
    private String articuloCodigo;
    private String articuloNombre;
    private int cantidad;
    private double precio;
    private double subTotal;

    public DetalleIngreso() {
    }

    public DetalleIngreso(int articuloID, int cantidad, double precio) {
        this.articuloID = articuloID;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public DetalleIngreso(int articuloID, String articuloCodigo, String articuloNombre, int cantidad, double precio, double subTotal) {
        this.articuloID = articuloID;
        this.articuloCodigo = articuloCodigo;
        this.articuloNombre = articuloNombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.subTotal = subTotal;
    }
    
    public DetalleIngreso(int id, int ingresoID, int articuloID, String articuloCodigo, String articuloNombre, int cantidad, double precio, double subTotal) {
        this.id = id;
        this.ingresoID = ingresoID;
        this.articuloID = articuloID;
        this.articuloCodigo = articuloCodigo;
        this.articuloNombre = articuloNombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.subTotal = subTotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIngresoID() {
        return ingresoID;
    }

    public void setIngresoID(int ingresoID) {
        this.ingresoID = ingresoID;
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

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }
    
    
}
