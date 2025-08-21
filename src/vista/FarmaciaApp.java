
package vista;
import modelo.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
/**
 *
 * @author Erick
 */
public class FarmaciaApp extends JFrame {
    private Blockchain blockchain;
    private Bloque bloquePendiente;
    private ArrayList<String> listaFarmacias; // Lista de IDs de farmacias

    private JComboBox<String> comboFarmacias;
    private DefaultListModel<Bloque> listModelBloques;
    private JList<Bloque> listaBloques;
    private DefaultListModel<TransaccionInventario> listModelTransaccionesPendientes;
    private JList<TransaccionInventario> listaTransaccionesPendientes;
    private DefaultListModel<Producto> listModelInventario;
    private JList<Producto> listaInventario;

    private JLabel labelEstado;

    public FarmaciaApp() {
        blockchain = new Blockchain(4);
        bloquePendiente = new Bloque(blockchain.obtenerUltimoBloque().getHash());
        listaFarmacias = new ArrayList<>();
        
        // Inicializar algunas farmacias de ejemplo
        listaFarmacias.add("Farmacia A");
        listaFarmacias.add("Farmacia B");

        setTitle("Inventario de Farmacias con Blockchain");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Paneles principales
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        JPanel panelDerecho = new JPanel(new BorderLayout());

        // JComboBox para seleccionar la farmacia
        comboFarmacias = new JComboBox<>(listaFarmacias.toArray(new String[0]));
        comboFarmacias.addActionListener(e -> actualizarInventario());
        panelIzquierdo.add(comboFarmacias, BorderLayout.NORTH);

        // JList para mostrar el inventario de la farmacia seleccionada
        listModelInventario = new DefaultListModel<>();
        listaInventario = new JList<>(listModelInventario);
        listaInventario.setBorder(BorderFactory.createTitledBorder("Inventario Actual"));
        panelIzquierdo.add(new JScrollPane(listaInventario), BorderLayout.CENTER);

        // JList para bloques
        listModelBloques = new DefaultListModel<>();
        listaBloques = new JList<>(listModelBloques);
        listaBloques.setBorder(BorderFactory.createTitledBorder("Bloques de la Cadena"));
        listModelBloques.addElement(blockchain.getCadena().get(0));
        
        // JList para transacciones pendientes
        listModelTransaccionesPendientes = new DefaultListModel<>();
        listaTransaccionesPendientes = new JList<>(listModelTransaccionesPendientes);
        listaTransaccionesPendientes.setBorder(BorderFactory.createTitledBorder("Transacciones Pendientes"));
        
        JSplitPane splitPaneBloquesTransacciones = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(listaBloques), new JScrollPane(listaTransaccionesPendientes));
        splitPaneBloquesTransacciones.setDividerLocation(300);
        panelDerecho.add(splitPaneBloquesTransacciones, BorderLayout.CENTER);

        // Panel de botones y estado
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnRegistrarEntrada = new JButton("Registrar Entrada");
        JButton btnRegistrarSalida = new JButton("Registrar Salida");
        JButton btnMinarBloque = new JButton("Minar Nuevo Bloque");
        JButton btnValidarCadena = new JButton("Validar Cadena");

        btnRegistrarEntrada.addActionListener(e -> registrarMovimiento(TransaccionInventario.ENTRADA));
        btnRegistrarSalida.addActionListener(e -> registrarMovimiento(TransaccionInventario.SALIDA));
        btnMinarBloque.addActionListener(this::minarNuevoBloque);
        btnValidarCadena.addActionListener(this::validarCadena);

        panelBotones.add(btnRegistrarEntrada);
        panelBotones.add(btnRegistrarSalida);
        panelBotones.add(btnMinarBloque);
        panelBotones.add(btnValidarCadena);

        labelEstado = new JLabel("Estado: Listo para registrar transacciones.");
        labelEstado.setHorizontalAlignment(SwingConstants.CENTER);
        
        panelPrincipal.add(panelIzquierdo, BorderLayout.WEST);
        panelPrincipal.add(panelDerecho, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        panelPrincipal.add(labelEstado, BorderLayout.NORTH);

        add(panelPrincipal);
    }
    
    private void registrarMovimiento(String tipo) {
        String farmaciaSeleccionada = (String) comboFarmacias.getSelectedItem();
        if (farmaciaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una farmacia.");
            return;
        }
        
        String nombreProducto = JOptionPane.showInputDialog("Nombre del producto:");
        String codigoProducto = JOptionPane.showInputDialog("Código del producto:");
        int cantidad = Integer.parseInt(JOptionPane.showInputDialog("Cantidad a " + (tipo.equals(TransaccionInventario.ENTRADA) ? "ingresar" : "retirar") + ":"));

        TransaccionInventario transaccion = new TransaccionInventario(farmaciaSeleccionada, nombreProducto, codigoProducto, cantidad, tipo);
        bloquePendiente.agregarTransaccion(transaccion);
        listModelTransaccionesPendientes.addElement(transaccion);
        labelEstado.setText("Estado: Transacción agregada. A la espera de ser minada.");
    }

    private void minarNuevoBloque(ActionEvent e) {
        if (listModelTransaccionesPendientes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay transacciones pendientes para minar.");
            return;
        }

        labelEstado.setText("Estado: Minando el nuevo bloque...");
        bloquePendiente.minarBloque(blockchain.getDificultad());
        blockchain.agregarBloque(bloquePendiente);
        listModelBloques.addElement(bloquePendiente);

        listModelTransaccionesPendientes.clear();
        bloquePendiente = new Bloque(blockchain.obtenerUltimoBloque().getHash());
        
        actualizarInventario(); // Actualizar el inventario después de minar
        labelEstado.setText("Estado: Bloque minado con éxito.");
        JOptionPane.showMessageDialog(this, "¡Bloque minado con éxito!");
    }

    private void validarCadena(ActionEvent e) {
        boolean esValida = blockchain.esCadenaValida();
        if (esValida) {
            labelEstado.setText("Estado: La cadena de bloques es válida. ✔️");
            JOptionPane.showMessageDialog(this, "¡La cadena de bloques es válida!");
        } else {
            labelEstado.setText("Estado: La cadena de bloques es inválida. ❌");
            JOptionPane.showMessageDialog(this, "¡La cadena de bloques es inválida!");
        }
    }
    
    private void actualizarInventario() {
        String farmaciaSeleccionada = (String) comboFarmacias.getSelectedItem();
        if (farmaciaSeleccionada == null) return;
        
        listModelInventario.clear();
        HashMap<String, Producto> inventarioCalculado = new HashMap<>();
        
        for (Bloque bloque : blockchain.getCadena()) {
            for (TransaccionInventario transaccion : bloque.getTransacciones()) {
                if (transaccion.getFarmaciaId().equals(farmaciaSeleccionada)) {
                    String codigo = transaccion.getCodigoProducto();
                    
                    if (!inventarioCalculado.containsKey(codigo)) {
                        inventarioCalculado.put(codigo, new Producto(transaccion.getNombreProducto(), codigo, 0));
                    }
                    
                    Producto producto = inventarioCalculado.get(codigo);
                    int nuevaCantidad = producto.getCantidad();
                    
                    if (transaccion.getTipoMovimiento().equals(TransaccionInventario.ENTRADA)) {
                        nuevaCantidad += transaccion.getCantidad();
                    } else if (transaccion.getTipoMovimiento().equals(TransaccionInventario.SALIDA)) {
                        nuevaCantidad -= transaccion.getCantidad();
                    }
                    producto.setCantidad(nuevaCantidad);
                }
            }
        }
        
        for (Producto p : inventarioCalculado.values()) {
            listModelInventario.addElement(p);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FarmaciaApp().setVisible(true));
    }
}
