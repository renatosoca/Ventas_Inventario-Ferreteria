/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos.Interfaces;

import java.util.List;

/**
 *
 * @author rena_
 */
public interface CrudVentaInterface<T, D> {
    
    public List<T> listar(String texto,int totalPorPagina, int numPAgina); //para la paginación
    public List<D> listarDetalle(int id); //para los detalles de ventas
    public boolean insertar(T obj);
    public boolean anular(int id);
    public int total();
    public boolean existe(String texto1, String texto2);
    
    
}
