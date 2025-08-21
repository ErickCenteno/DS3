
package modelo;

/**
 *
 * @author Erick
 */
public class Producto {
    private String nombre;
    private String codigo;
    private int cantidad;

    public Producto(String nombre, String codigo, int cantidad) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.cantidad = cantidad;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getCodigo() { return codigo; }
    public int getCantidad() { return cantidad; }
    
    // Setter para actualizar la cantidad
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    
    @Override
    public String toString() {
        return nombre + " (" + codigo + ") - Cantidad: " + cantidad;
    }
}