
package modelo;

/**
 *
 * @author Erick
 */
public class Medicamento extends Producto {
    private String dosis;
    
    public Medicamento(String nombre, String codigo, int cantidad, String dosis) {
        super(nombre, codigo, cantidad);
        this.dosis = dosis;
    }
    
    public String getDosis() { return dosis; }
    
    @Override
    public String getTipo() {
        return "Medicamento";
    }
    
    @Override
    public String toString() {
        return "Med.: " + nombre + " (" + codigo + ") - Dosis: " + dosis + " - Cantidad: " + cantidad;
    }
}