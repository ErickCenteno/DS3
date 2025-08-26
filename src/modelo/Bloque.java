
package modelo;
import java.util.ArrayList;
import java.util.Date;
/**
 *
 * @author Erick
 */
public class Bloque {
    private String hash;
    private String hashAnterior;
    private ArrayList<TransaccionInventario> transacciones;
    private long tiempoCreacion; 
    private int nonce;

    public Bloque(String hashAnterior) {
        this.hashAnterior = hashAnterior;
        this.tiempoCreacion = new Date().getTime();
        this.transacciones = new ArrayList<>();
        this.hash = calcularHash();
    }

    public void agregarTransaccion(TransaccionInventario t) {
        transacciones.add(t);
    }
    
    //  calcular el hash del bloque
    public String calcularHash() {
        String datosBloque = hashAnterior + Long.toString(tiempoCreacion) + Integer.toString(nonce) + transacciones.toString();
        return CriptoUtil.aplicarSha256(datosBloque);
    }

    // prueba de trabajo para minar el bloque
    public void minarBloque(int dificultad) {
        String prefijoDificultad = new String(new char[dificultad]).replace('\0', '0');
        while (!hash.substring(0, dificultad).equals(prefijoDificultad)) {
            nonce++;
            hash = calcularHash();
        }
        System.out.println("Bloque minado: " + hash);
    }

    // Getters
    public String getHash() { return hash; }
    public String getHashAnterior() { return hashAnterior; }
    public ArrayList<TransaccionInventario> getTransacciones() { return transacciones; }

     @Override
    public String toString() {
        String hashCorto = (hash.length() >= 10) ? hash.substring(0, 10) + "..." : hash;
        String hashAnteriorCorto = (hashAnterior != null && hashAnterior.length() >= 10) ? hashAnterior.substring(0, 10) + "..." : "N/A";

        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        // El index se define al agregar al blockchain
        sb.append("  \"timestamp\": ").append(tiempoCreacion).append(",\n");
        sb.append("  \"transacciones\": [");
        
        for (int i = 0; i < transacciones.size(); i++) {
            TransaccionInventario t = transacciones.get(i);
            sb.append("\n    {\n");
            sb.append("      \"farmaciaId\": \"").append(t.getFarmaciaId()).append("\",\n");
            sb.append("      \"nombreProducto\": \"").append(t.getProducto().getNombre()).append("\",\n");
            sb.append("      \"codigoProducto\": \"").append(t.getProducto().getCodigo()).append("\",\n");
            sb.append("      \"cantidad\": ").append(t.getProducto().getCantidad()).append(",\n");
            sb.append("      \"tipoMovimiento\": \"").append(t.getTipoMovimiento()).append("\"\n");
            sb.append("    }");
            if (i < transacciones.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("\n  ],\n");
        sb.append("  \"nonce\": ").append(nonce).append(",\n");
        sb.append("  \"hashAnterior\": \"").append(hashAnterior).append("\",\n");
        sb.append("  \"hash\": \"").append(hash).append("\"\n");
        sb.append("}");

        return sb.toString();
    }
}
