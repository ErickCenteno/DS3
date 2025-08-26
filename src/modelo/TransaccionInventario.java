
package modelo;
import java.util.Date;
/**
 *
 * @author Erick
 */
public class TransaccionInventario {
    private String farmaciaId;
    private Producto producto;
    private String tipoMovimiento; // "ENTRADA", "SALIDA", "AJUSTE"
    private long tiempoCreacion; 
    private String hashTransaccion;

    // Constantes para los tipos de movimiento
    public static final String ENTRADA = "ENTRADA";
    public static final String SALIDA = "SALIDA";
    public static final String AJUSTE = "AJUSTE";

    public TransaccionInventario(String farmaciaId, Producto producto, String tipoMovimiento) {
        this.farmaciaId = farmaciaId;
        this.producto = producto;
        this.tipoMovimiento = tipoMovimiento;
        this.tiempoCreacion = new Date().getTime();
        this.hashTransaccion = calcularHash();
    }

    // Calcula el hash de la transaccion
    public String calcularHash() {
        String datosTransaccion = farmaciaId + producto.getNombre() + producto.getCodigo() + Integer.toString(producto.getCantidad()) + tipoMovimiento + Long.toString(tiempoCreacion);
        return CriptoUtil.aplicarSha256(datosTransaccion);
    }

    // Getters
    public String getFarmaciaId() { return farmaciaId; }
    public Producto getProducto() { return producto; }
    public String getTipoMovimiento() { return tipoMovimiento; }
    public long getTiempoCreacion() { return tiempoCreacion; }
    public String getHashTransaccion() { return hashTransaccion; }

    @Override
    public String toString() {
        String estado;
        if (tipoMovimiento.equals(ENTRADA)) {
            estado = "Entrada: +";
        } else if (tipoMovimiento.equals(SALIDA)) {
            estado = "Salida: -";
        } else {
            estado = "Ajuste: ";
        }
        return "ID: " + hashTransaccion.substring(0, 8) + "... | " + estado + producto.getCantidad() + " de " + producto.getNombre() + " (" + producto.getCodigo() + ")";
    }
}