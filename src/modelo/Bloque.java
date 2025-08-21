
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
    private long tiempoCreacion; // Timestamp
    private int nonce; // Usado para la prueba de trabajo

    public Bloque(String hashAnterior) {
        this.hashAnterior = hashAnterior;
        this.tiempoCreacion = new Date().getTime();
        this.transacciones = new ArrayList<>();
        this.hash = calcularHash();
    }

    public void agregarTransaccion(TransaccionInventario t) {
        transacciones.add(t);
    }
    
    // Método para calcular el hash del bloque
    public String calcularHash() {
        String datosBloque = hashAnterior + Long.toString(tiempoCreacion) + Integer.toString(nonce) + transacciones.toString();
        return CriptoUtil.aplicarSha256(datosBloque);
    }

    // Método de prueba de trabajo para minar el bloque
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

        return "Hash: " + hashCorto + "\n" +
               "Hash Anterior: " + hashAnteriorCorto + "\n" +
               "Transacciones: " + transacciones.size() + "\n" +
               "Nonce: " + nonce + "\n" +
               "--------------------";
    }
}
