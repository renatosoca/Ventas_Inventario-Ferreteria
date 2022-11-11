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
public class Usuario {
    private int id;
    private int rolId;
    private String nombreRol;
    private String nombre;
    private String tipodocumento;
    private String numDocumento;
    private String direccion;
    private String telefono;
    private String email;
    private String clave;
    private boolean activo;

    public Usuario() {
    }

    public Usuario(int id, int rolId, String nombreRol, String nombre, String tipodocumento, String numDocumento, String direccion, String telefono, String email, boolean activo) {
        this.id = id;
        this.rolId = rolId;
        this.nombreRol = nombreRol;
        this.nombre = nombre;
        this.tipodocumento = tipodocumento;
        this.numDocumento = numDocumento;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.activo = activo;
    }

    

    public Usuario(int id, int rolId, String nombreRol, String nombre, String tipodocumento, String numDocumento, String direccion, String telefono, String email, String clave, boolean activo) {
        this.id = id;
        this.rolId = rolId;
        this.nombreRol = nombreRol;
        this.nombre = nombre;
        this.tipodocumento = tipodocumento;
        this.numDocumento = numDocumento;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.clave = clave;
        this.activo = activo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRolId() {
        return rolId;
    }

    public void setRodId(int rolId) {
        this.rolId = rolId;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipodocumento() {
        return tipodocumento;
    }

    public void setTipodocumento(String tipodocumento) {
        this.tipodocumento = tipodocumento;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", rodId=" + rolId + ", nombreRol=" + nombreRol + ", nombre=" + nombre + ", tipodocumento=" + tipodocumento + ", numDocumento=" + numDocumento + ", direccion=" + direccion + ", telefono=" + telefono + ", email=" + email + ", clave=" + clave + ", activo=" + activo + '}';
    }
    
    
}
