
package modelo;

/**
 *
 * @author Erick
 */
public abstract class Producto {
    protected String nombre;
    protected String codigo;
    protected int cantidad;

    public Producto(String nombre, String codigo, int cantidad) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.cantidad = cantidad;
    }
    
    public String getNombre() { return nombre; }
    public String getCodigo() { return codigo; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    
    public abstract String getTipo();
}