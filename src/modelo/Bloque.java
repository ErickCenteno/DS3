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
    private ArrayList<String> datosEncriptados;
    private String llaveAesEncriptada; //  Para guardar la llave AES encriptada
    private long tiempoCreacion;
    private int nonce;
    private int index; 

    public Bloque(String hashAnterior) {
        this.hashAnterior = hashAnterior;
        this.tiempoCreacion = new Date().getTime();
        this.transacciones = new ArrayList<>();
        this.datosEncriptados = new ArrayList<>(); // Inicializar la lista
        this.llaveAesEncriptada = ""; // 🌟 NUEVO: Inicializar la variable
        this.nonce = 0;
        this.hash = calcularHash();
        this.index = 0;
    }
    
    // Método para agregar transacciones
    public void agregarTransaccion(TransaccionInventario t) {
        transacciones.add(t);
    }

    // método para calcular el hash
    public String calcularHash() {
        String datosBloque = hashAnterior + Long.toString(tiempoCreacion) + Integer.toString(nonce) + llaveAesEncriptada;
        for (String dato : datosEncriptados) {
            datosBloque += dato;
        }
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

    // Getters y Setters
    public String getHash() { return hash; }
    public String getHashAnterior() { return hashAnterior; }
    public ArrayList<TransaccionInventario> getTransacciones() { return transacciones; }
    
    public ArrayList<String> getDatosEncriptados() { return datosEncriptados; }
    public void setDatosEncriptados(ArrayList<String> datosEncriptados) { 
        this.datosEncriptados = datosEncriptados; 
        this.hash = calcularHash();
    }
    
    //  Getters y Setters para la llave AES encriptada
    public String getLlaveAesEncriptada() { return llaveAesEncriptada; }
    public void setLlaveAesEncriptada(String llaveAesEncriptada) { 
        this.llaveAesEncriptada = llaveAesEncriptada; 
        this.hash = calcularHash();
    }

    public int getIndex() { return index; }
    public long getTimestamp() { return tiempoCreacion; }
    public int getNonce() { return nonce; }
    
    public void setIndex(int index) { this.index = index; }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"index\": ").append(index).append(",\n");
        sb.append("  \"timestamp\": ").append(tiempoCreacion).append(",\n");
        sb.append("  \"transacciones\": [\n"); // Mantenemos transacciones para la serialización
        
        // Asumiendo que esta parte no se usa para el JSON guardado
        // y se usaría el JSON de datosEncriptados para guardar.
        // Si quieres guardar el JSON de transacciones originales, necesitarías descifrar primero.
        // Por la simplicidad de este ejercicio, nos enfocaremos en la funcionalidad
        // de encriptación, por lo que el toString no es la fuente final de datos guardados.
        // Si necesitas que el toString sea una representación completa y descifrable,
        // tendríamos que modificarlo para que haga el proceso de desencriptado.
        
        sb.append("  ],\n");
        sb.append("  \"nonce\": ").append(nonce).append(",\n");
        sb.append("  \"hashAnterior\": \"").append(hashAnterior).append("\",\n");
        sb.append("  \"hash\": \"").append(hash).append("\",\n");
        sb.append("  \"llaveAesEncriptada\": \"").append(llaveAesEncriptada).append("\",\n"); // 🌟 NUEVO
        sb.append("  \"datosEncriptados\": ["); // 🌟 MODIFICADO
        
        for (int i = 0; i < datosEncriptados.size(); i++) {
            sb.append("\"").append(datosEncriptados.get(i)).append("\"");
            if (i < datosEncriptados.size() - 1) {
                sb.append(",");
            }
        }
        
        sb.append("]\n");
        sb.append("}");

        return sb.toString();
    }
}