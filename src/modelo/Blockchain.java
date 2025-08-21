
package modelo;
import java.util.ArrayList;
/**
 *
 * @author Erick
 */
public class Blockchain {
    private ArrayList<Bloque> cadena;
    private int dificultad;

    public Blockchain(int dificultad) {
        this.dificultad = dificultad;
        this.cadena = new ArrayList<>();
        crearBloqueGenesis();
    }

    private void crearBloqueGenesis() {
        Bloque bloqueGenesis = new Bloque("0");
        bloqueGenesis.minarBloque(dificultad);
        cadena.add(bloqueGenesis);
    }
    
    public Bloque obtenerUltimoBloque() {
        return cadena.get(cadena.size() - 1);
    }

    public void agregarBloque(Bloque nuevoBloque) {
        nuevoBloque.minarBloque(dificultad);
        cadena.add(nuevoBloque);
    }

    public boolean esCadenaValida() {
        Bloque bloqueActual;
        Bloque bloqueAnterior;

        for (int i = 1; i < cadena.size(); i++) {
            bloqueActual = cadena.get(i);
            bloqueAnterior = cadena.get(i - 1);

            if (!bloqueActual.getHash().equals(bloqueActual.calcularHash())) {
                System.out.println("El hash del bloque actual no es válido.");
                return false;
            }

            if (!bloqueActual.getHashAnterior().equals(bloqueAnterior.getHash())) {
                System.out.println("El hash del bloque anterior no coincide.");
                return false;
            }
        }
        return true;
    }

    public ArrayList<Bloque> getCadena() {
        return cadena;
    }

    public int getDificultad() {
        return dificultad;
    }
}
